# Task: Scaffold the Android Project Build System

## Context

You are setting up the build system for "Better Everyday", an Android habit tracker app
using Jetpack Compose + Material3 + Kotlin. All Kotlin source files already exist under:

```
android/app/src/main/java/com/bettereveryday/
```

The project has NO build files yet. Your job is to produce every file needed to make
`./gradlew assembleDebug` succeed and the app runnable in Android Studio / on a device.

**Package name**: `com.bettereveryday`
**App name**: `Better Everyday`
**Min SDK**: 26 (required for `java.time.LocalDate`)
**Target SDK**: 35
**Compile SDK**: 35
**Kotlin version**: 2.0.21
**AGP (Android Gradle Plugin) version**: 8.7.3

---

## Source files already present (do NOT create these)

```
app/src/main/java/com/bettereveryday/
  data/local/db/AppDatabase.kt
  data/local/db/dao/CompletionDao.kt
  data/local/db/dao/HabitDao.kt
  data/local/db/entity/CompletionEntity.kt
  data/local/db/entity/HabitEntity.kt
  data/prefs/UserPreferences.kt
  data/prefs/UserPreferencesRepository.kt
  ui/theme/ThemeSystem.kt
  ui/MainViewModel.kt
  ui/onboarding/OnboardingViewModel.kt
  ui/onboarding/OnboardingSummaryScreen.kt
  ui/onboarding/SplashScreen.kt
  ui/onboarding/WelcomeScreen.kt
  ui/onboarding/components/OnboardingScaffold.kt
  ui/onboarding/components/SelectionCard.kt
  ui/onboarding/components/WheelTimePicker.kt
  ui/onboarding/steps/ConsistencyLevelScreen.kt
  ui/onboarding/steps/FocusSelectionScreen.kt
  ui/onboarding/steps/HabitQuantityScreen.kt
  ui/onboarding/steps/NameEntryScreen.kt
  ui/onboarding/steps/NotificationsPermissionScreen.kt
  ui/onboarding/steps/ThemeSelectorScreen.kt
  ui/onboarding/steps/WakeUpTimeScreen.kt
  ui/onboarding/steps/WindDownTimeScreen.kt
  ui/goals/GoalsViewModel.kt
  ui/insights/InsightsViewModel.kt
  ui/profile/ProfileViewModel.kt
  ui/today/TodayViewModel.kt
```

The following files will be added by Batch 4 (not present yet — do not reference them,
but the build system must support them when they arrive):
```
  MainActivity.kt
  AppNavigation.kt
  ui/goals/GoalsScreen.kt, AddGoalSheet.kt, AddGoalViewModel.kt
  ui/insights/InsightsScreen.kt
  ui/profile/ProfileScreen.kt, EditProfileSheet.kt
  ui/today/TodayScreen.kt, HabitDetailScreen.kt, HabitDetailViewModel.kt
  ui/MainShell.kt
  notifications/AlarmScheduler.kt
  notifications/HabitReminderReceiver.kt
  notifications/BootCompletedReceiver.kt
```

---

## Files to produce

```
android/
  settings.gradle.kts
  build.gradle.kts                  (root/project-level)
  gradle.properties
  gradle/wrapper/gradle-wrapper.properties
  app/
    build.gradle.kts                (app module)
    src/
      main/
        AndroidManifest.xml
        res/
          values/
            strings.xml
            colors.xml
            themes.xml
          drawable/
            ic_launcher_background.xml    (simple solid coral #F26D55 background)
          mipmap-hdpi/   ← placeholder; provide ic_launcher.xml as adaptive icon
          mipmap-mdpi/
          mipmap-xhdpi/
          mipmap-xxhdpi/
          mipmap-xxxhdpi/
```

For the launcher icons, produce a single `res/mipmap-anydpi-v26/ic_launcher.xml` adaptive
icon file pointing to the background drawable and a foreground that is just the sun emoji
placeholder. The developer will replace it later.

---

## Dependency versions to use

```
Compose BOM: 2024.12.01
Navigation Compose: 2.8.5
Room: 2.6.1
DataStore: 1.1.1
Lifecycle ViewModel Compose: 2.8.7
Kotlin Coroutines: 1.9.0
Activity Compose: 1.9.3
Core KTX: 1.15.0
```

---

## app/build.gradle.kts — required dependencies

```kotlin
// Core
implementation("androidx.core:core-ktx:1.15.0")
implementation("androidx.activity:activity-compose:1.9.3")

// Compose BOM — import this, then reference compose libs without versions
implementation(platform("androidx.compose:compose-bom:2024.12.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.foundation:foundation")
implementation("androidx.compose.animation:animation")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")

// Lifecycle + ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.1.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
```

---

## AndroidManifest.xml — required entries

Permissions:
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
```

Application attributes:
```xml
android:name=".BetterEverydayApp"   <!-- not needed yet, omit -->
android:label="@string/app_name"
android:theme="@style/Theme.BetterEveryday"
android:supportsRtl="true"
```

Activity (MainActivity — not created yet but declare it):
```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:windowSoftInputMode="adjustResize">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

Receivers (not created yet but declare them):
```xml
<receiver android:name=".notifications.HabitReminderReceiver" android:exported="false" />
<receiver android:name=".notifications.BootCompletedReceiver" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
```

---

## res/values/themes.xml

The app uses Compose for all theming — the XML theme only needs to handle the system
window (status bar, edge-to-edge). Use a `Theme.Material3.DayNight.NoActionBar` parent
with a transparent status bar:

```xml
<style name="Theme.BetterEveryday" parent="Theme.Material3.DayNight.NoActionBar">
    <item name="android:statusBarColor">@android:color/transparent</item>
    <item name="android:navigationBarColor">@android:color/transparent</item>
    <item name="android:windowLightStatusBar">true</item>
</style>
```

---

## res/values/strings.xml

Minimum required:
```xml
<string name="app_name">Better Everyday</string>
<string name="notification_channel_name">Habit Reminders</string>
```

---

## res/values/colors.xml

Only what the XML theme system needs (Compose handles all app colors):
```xml
<color name="background_warm">#FAF6EE</color>
<color name="accent_coral">#F26D55</color>
```

---

## gradle.properties

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
```

---

## gradle/wrapper/gradle-wrapper.properties

Use Gradle 8.9:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
```

---

## Constraints

- Use **Kotlin DSL** (`*.gradle.kts`) throughout — not Groovy
- Use **`kapt`** for Room annotation processing (not KSP — keeps it simpler for now)
- The `compileSdk`, `minSdk`, `targetSdk` must be set as integers, not string references
- `composeOptions { kotlinCompilerExtensionVersion }` is NOT needed when using the Compose BOM with AGP 8.x — omit it
- Enable `buildFeatures { compose = true }` in `app/build.gradle.kts`
- The root `build.gradle.kts` should only contain `plugins {}` block with `apply false` — no `allprojects` or `subprojects` blocks
- No comments in the output files
