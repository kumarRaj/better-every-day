# Task: Build Navigation Wiring + MainActivity

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 4, Task 5 of 5**.
This task must be done AFTER all other Batch 4 tasks are complete, as it wires
everything together.

This task connects all screens via Navigation Compose, implements the onboarding→dashboard
routing decision at launch, and creates the `MainActivity`.

---

## Batch 1 Foundation

```kotlin
// AppDatabase.getInstance(context): AppDatabase
// UserPreferencesRepository(context): userPreferences: Flow<UserPreferences>
//   where UserPreferences.onboardingComplete: Boolean
//         UserPreferences.selectedTheme: String

enum class AppTheme { Sunrise, Ocean, Forest, Lavender, Midnight, Rose }
@Composable fun BetterEverydayTheme(theme: AppTheme, content: @Composable () -> Unit)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
```

## All screens and their signatures (from Batches 2, 3, 4)

```kotlin
// Onboarding
fun SplashScreen(onNavigationReady: () -> Unit)
fun WelcomeScreen(onGetStarted: () -> Unit)
fun NameEntryScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun FocusSelectionScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun ConsistencyLevelScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun HabitQuantityScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun WakeUpTimeScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun WindDownTimeScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun ThemeSelectorScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun NotificationsPermissionScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)
fun OnboardingSummaryScreen(viewModel: OnboardingViewModel, onStartJourney: () -> Unit)

// Main app
fun MainShell(prefsRepository: UserPreferencesRepository, db: AppDatabase)

// Sheets (shown as modal bottom sheets, not full nav destinations)
fun EditProfileSheet(viewModel: ProfileViewModel, onDismiss: () -> Unit)
fun AddGoalSheet(viewModel: AddGoalViewModel, habitId: Long? = null, onDismiss: () -> Unit)

// Detail screen
fun HabitDetailScreen(viewModel: HabitDetailViewModel, onBack: () -> Unit, onEdit: (Long) -> Unit)
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/
  MainActivity.kt
  AppNavigation.kt
```

---

## AppNavigation

Top-level `@Composable` function that owns the `NavController` and the global
`BetterEverydayTheme` wrapper.

```kotlin
@Composable
fun AppNavigation(
    prefsRepository: UserPreferencesRepository,
    db: AppDatabase,
)
```

### Theme state
Observe `prefsRepository.userPreferences` and map `selectedTheme` string → `AppTheme`.
Wrap all content in `BetterEverydayTheme(theme = activeTheme)`.

### Launch routing
Before showing any screen, collect `onboardingComplete` from prefs:
- Show `SplashScreen` while determining the route (it auto-dismisses after 1.5s)
- After splash: if `onboardingComplete == true` → navigate to `"main"`, else → `"welcome"`
- Use a `startDestination = "splash"` nav graph

### Route map

```
splash          → SplashScreen
welcome         → WelcomeScreen
onboarding/1    → NameEntryScreen
onboarding/2    → FocusSelectionScreen
onboarding/3    → ConsistencyLevelScreen
onboarding/4    → HabitQuantityScreen
onboarding/5    → WakeUpTimeScreen
onboarding/6    → WindDownTimeScreen
onboarding/7    → ThemeSelectorScreen
onboarding/8    → NotificationsPermissionScreen
onboarding/summary → OnboardingSummaryScreen
main            → MainShell (hosts Today/Goals/Insights/Profile tabs)
habit/{habitId} → HabitDetailScreen
```

### Onboarding navigation rules
- Each step's `onContinue` → `navController.navigate("onboarding/${step + 1}")` (or `"onboarding/summary"` after step 8)
- Each step's `onBack` → `navController.popBackStack()`
- `WelcomeScreen.onGetStarted` → `"onboarding/1"`
- `OnboardingSummaryScreen.onStartJourney` → `navController.navigate("main") { popUpTo("splash") { inclusive = true } }`

### MainShell wiring
Pass these lambdas down through `MainShell` to the tab screens:
- `onHabitClick: (Long) -> Unit` → `navController.navigate("habit/$habitId")`
- `onAddGoal: () -> Unit` → show `AddGoalSheet` (manage `var showAddGoal by remember { mutableStateOf(false) }` in `AppNavigation`)
- `onEditHabit: (Long) -> Unit` → show `AddGoalSheet(habitId = id)`
- `onEditProfile: () -> Unit` → show `EditProfileSheet`

Bottom sheets (`EditProfileSheet`, `AddGoalSheet`) are managed as local boolean state
in `AppNavigation`, not as nav destinations — they overlay the current screen.

### HabitDetailScreen
- `onBack` → `navController.popBackStack()`
- `onEdit(id)` → show `AddGoalSheet(habitId = id)`

### OnboardingViewModel scope
Create a single `OnboardingViewModel` instance at the `AppNavigation` level using
`viewModel()` and pass it to all onboarding screens — this preserves state across steps.

---

## MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val prefsRepository = remember { UserPreferencesRepository(applicationContext) }
            val db = remember { AppDatabase.getInstance(applicationContext) }
            AppNavigation(prefsRepository = prefsRepository, db = db)
        }
    }
}
```

Also set up the notification channel at app start (call once in `onCreate`):
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        "habit_reminders",
        "Habit Reminders",
        NotificationManager.IMPORTANCE_HIGH,
    ).apply { description = "Daily habit reminder notifications" }
    getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
}
```

---

## Constraints

- Use `androidx.navigation.compose.NavHost` + `composable(route)` DSL
- Use `rememberNavController()`
- Collect `onboardingComplete` with `collectAsState()` — avoid reading it blocking
- The `OnboardingViewModel` must be shared across all onboarding steps (single instance)
- No preview composables, no comments
