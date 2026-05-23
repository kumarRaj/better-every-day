# Task: First-run smoke test and bug fixing

## Context

The "Better Everyday" Android app has just been built for the first time. All screens,
ViewModels, navigation, and the notification layer are in place. This task is to run
the app on a device or emulator, walk through every flow, and fix any runtime bugs found.

The app is a habit tracker with:
- An 8-step onboarding flow
- A Today/Goals/Insights/Profile tab dashboard
- Habit creation/editing via a bottom sheet
- Daily notifications via `AlarmManager`

---

## Test flows — execute in order

### Flow 1: Full onboarding
1. Launch the app cold → Splash screen shows for ~1.5s
2. Welcome screen → tap "Let's go →"
3. Step 1: Enter a name (e.g. "Raj") → button shows "Hi Raj! Continue →"
4. Step 2: Select 2+ focus areas → button enables
5. Step 3: Select a consistency level
6. Step 4: Select habit quantity
7. Step 5: Scroll the wake-up time picker → button disables while scrolling, re-enables on snap
8. Step 6: Same for wind-down time
9. Step 7: Tap each theme → entire app re-themes instantly (chevron, button, progress bar)
10. Step 8: Tap "Enable Notifications" → system dialog appears → tap Allow → green badge appears → button changes to "All set! Continue →"
11. Summary screen: verify all selected options appear as chips
12. Tap "Start my journey →" → lands on Today tab dashboard

**Expected**: No crashes. Each step's back button returns to the previous step. Progress bar fills correctly.

### Flow 2: Today tab
1. Dashboard shows greeting with correct name and today's date
2. Progress ring shows 0%
3. "Next Up" card shows if any habits exist (empty state on first launch is fine)
4. Tap a habit row → navigates to Habit Detail screen
5. Tap the checkbox on a habit → ring animates to new %, title gets strikethrough, "Next Up" updates

**Expected**: State persists — kill and relaunch the app, completed habits remain checked.

### Flow 3: Add a habit
1. Goals tab → tap "+ Add Goal"
2. Sheet slides up
3. Enter title, select emoji, select focus area, select schedule, set reminder time
4. Tap Save → habit appears in Goals list and Today list (if schedule matches today)
5. Verify notification is scheduled: check via `adb shell dumpsys alarm | grep bettereveryday`

**Expected**: Habit persists after app kill/relaunch.

### Flow 4: Edit and delete a habit
1. Goals tab → tap ✏️ on a habit → Edit Goal sheet opens pre-populated
2. Change the title → Save → Goals list updates
3. Re-open edit → tap "Delete Habit" → confirm → habit removed from all lists

**Expected**: Alarm is cancelled on delete (verify via adb dumpsys alarm).

### Flow 5: Insights tab
1. Complete some habits on different days (can use adb to insert completions directly if needed)
2. Insights tab → weekly bar chart shows correct filled/unfilled bars
3. Streak leaders shows habits ranked by streak
4. Frequency breakdown shows correct counts

### Flow 6: Profile tab
1. Avatar shows first letter of name in gradient circle
2. Tap Name row → Edit Profile sheet slides up
3. Change name → Save → avatar and greeting on Today tab update
4. Toggle birthdate on → date picker expands → select a date → Save → profile shows formatted date
5. Tap a different theme → entire app re-themes instantly

### Flow 7: Onboarding skip (returning user)
1. Complete onboarding once
2. Kill and relaunch → app goes straight to Today tab (no splash → onboarding)

**Expected**: `onboardingComplete = true` in DataStore routes directly to `"main"`.

### Flow 8: Notification delivery
1. Create a habit with a reminder time 2 minutes from now
2. Wait → notification appears in system tray
3. Verify title format: e.g. "📚 Time to Read 20 pages"

---

## Known issues to check specifically

- **Tab bar overlap**: scroll to bottom of Today/Goals/Insights lists — content should
  not be hidden behind the floating tab bar
- **WheelTimePicker**: time columns snap cleanly to a value; no half-item visible;
  initial position shows correct pre-selected time centered
- **Theme persistence**: select Ocean theme, kill app, relaunch → Ocean theme active
- **Empty states**: Today tab with 0 habits shows "A new day to grow." and no "Next Up" card

---

## For each bug found

1. Note the screen, action, and observed vs expected behaviour
2. Fix the root cause — do not add `try/catch` to suppress crashes
3. Re-test the flow after fixing

## Constraints

- Fix runtime bugs only — do not redesign screens or add new features
- All fixes must preserve existing ViewModel state contracts
- No comments added unless fixing a subtle invariant that would surprise a future reader
