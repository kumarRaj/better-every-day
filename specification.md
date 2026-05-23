# Better Everyday - Developer Specification

This document contains a highly granular, developer-ready specification for the "Better Everyday" habit tracker application, reconstructed directly from high-resolution screenshots of the onboarding and dashboard flows.

---

## 1. Global Visual System & Design Tokens

### Background Color
- **Base Background**: Warm cream (`#FAF6EE` or soft warm off-white `#FFFDFB`). This background is consistent across almost all screens, providing a warm, friendly, and non-sterile aesthetic.

### Fonts & Typography
- **Font Family**: Clean sans-serif (e.g., system SF Pro on iOS / Inter / Roboto).
- **Font Weights**:
  - **Bold/Black**: Used for screen headings, primary brand markings, and highlights.
  - **Medium/Regular**: Used for description copy, button text, list items, and input labels.
- **Typography Scale**:
  - **Brand Header**: ~32pt bold.
  - **Screen Header**: ~24pt to 28pt bold.
  - **Subtitle / Section Header**: ~18pt to 20pt medium/bold.
  - **Body / List Text**: ~16pt regular.
  - **Small / Muted Text**: ~14pt regular (muted grey).

### Colors & Dynamic Themes
The application features dynamic re-theming depending on user choice. Selecting a theme instantly alters the color palette of all UI components (progress bar, icons, buttons, borders, active navigation states).
- **Default Accent (Coral)**: Approx `#F26D55` (used for primary buttons, progress bars, active states, and splash branding).
- **Active Selection Borders/Tints**: Matching the theme color but at a lower opacity or with distinct borders.
- **Muted Gray**: Approx `#8E8E93` (used for inactive segments, subheadings, and placeholders).

#### Dynamic Theme Palettes & Accent Specifications:
| Theme Name | Primary/Accent Color | Gradient Swatch Spectrum | Accent Emoji |
| :--- | :--- | :--- | :--- |
| **Sunrise** | `#F0783C` (Orange) | Vivid Orange $\rightarrow$ Light Yellow | 🌅 |
| **Ocean** | `#2C9EB4` (Teal) | Dark Cyan $\rightarrow$ Aqua Blue | 🌊 |
| **Forest** | `#2E9F6E` / `#34A853` (Green) | Forest Green $\rightarrow$ Mint Green | 🌿 |
| **Lavender** / **Cosmos** | `#9873E8` (Purple) | Deep Lavender $\rightarrow$ Pastel Lilac | 💜 / 🌌 |
| **Midnight** | `#4C75F2` (Blue) | Indigo Blue $\rightarrow$ Soft Blue | 🌙 |
| **Rose** / **Blossom** | `#E86CA0` (Pink) | Deep Magenta $\rightarrow$ Soft Rose | 🌸 |

---

## 2. Onboarding & Welcome Flow

All onboarding screens (except Splash & Welcome) follow a standardized mobile viewport layout with the following persistent structural zones:
- **Progress Indicator**: Consists of 8 horizontal rounded dash segments. Filled dashes use the active theme color; unfilled dashes use a light desaturated version.
- **Step Counter**: Small, muted gray text formatted as `"Step X of 8"` aligned to the top-right below the progress bar.
- **Back Navigation**: A simple left chevron (`<`) in active theme color on the left side, aligned vertically with the step counter.
- **Footer Action Button**: Large full-width button with highly rounded corners (`border-radius: 16px`) at the bottom.

### Splash Screen (`screenshot_00.png`)
- **Background**: Base Warm Cream.
- **Layout**: Centered vertically and horizontally.
- **Elements**:
  - **Icon**: Large Sun emoji ☀️.
  - **Brand Typography**: "Better Everyday" in large, bold Default Accent (Coral).

### Welcome Screen (`screenshot_06.png`)
- **Background**: Base Warm Cream.
- **Layout**: 
  - Centered graphic & copy block.
  - Bottom-anchored action button with standard safe-area padding.
- **Elements**:
  - **Main Graphic**: Large orange-to-coral circular gradient disc with soft drop-shadow/glow. Inside the disc is a centered Sun emoji ☀️.
  - **Title**: "Better Everyday" in heavy, bold black sans-serif.
  - **Tagline**: "Small steps. Big life." in a medium-sized gray/muted font.
  - **Description**: "Build lasting habits with gentle nudges, powerful insights, and a little motivation." in light gray/muted, centered body font (spanned across 2 lines).
  - **Button**: Full-width (with margins) rounded primary action button in Default Accent (Coral). 
    - Text: "Let's go →" in bold white.

### Name Entry Screen (Onboarding Step 1 - `screenshot_10.png`)
- **Header Section**: Step 1 of 8 (only 1st progress segment filled).
- **Layout**: Centered vertically and horizontally.
- **Elements**:
  - **Hero Emoji**: Large waving hand emoji (👋) with motion indicators.
  - **Title**: "What's your name?" (Large, Bold, Black).
  - **Subtitle**: "We'll use it to make everything feel personal." (Regular, Grey).
  - **Name Input Field**:
    - Highly rounded corners (`border-radius: 16px`).
    - Background: Clean off-white.
    - Border: Solid thin line in the active theme color.
    - Font: Centered, regular weight, black text.
- **Button State**:
  - Text dynamically updates: `"Hi [Name]! Continue ➔"` (e.g., "Hi Raj! Continue ➔").
  - If input is empty, fallback to `"Continue ➔"` and disable interaction.

### Focus Selection Screen (Onboarding Step 2 - `screenshot_13.png`, `screenshot_15.png`, `screenshot_16.png`)
- **Header Section**: Step 2 of 8 (segments 1 and 2 filled).
- **Layout**:
  - Large target/bullseye emoji (🎯) centered at top.
  - Title: "What's your main focus?"
  - Subtitle: "Pick one or more areas you want to improve."
  - **Grid Layout**: Symmetrical 2-column, 3-row grid containing 6 cards:
    1. 🧘 **Mindfulness**
    2. 💪 **Fitness**
    3. 📚 **Learning**
    4. 😴 / 💤 **Sleep**
    5. 🥗 **Nutrition**
    6. 💼 **Productivity**
- **Card States**:
  - **Unselected**: Light gray/off-white background, no border, bold dark charcoal text below centered emoji.
  - **Selected**: Translucent active theme tint background, $2\text{px}$ solid active theme color border, bold dark charcoal text.
- **Button State**:
  - Disabled state if 0 selected: Background is a translucent/desaturated tint, text is `"Select at least one"`.
  - Active state if $\ge$ 1 selected: Solid active theme color background, text is `"Continue ➔"`.

### Consistency Level (Onboarding Step 3 - `screenshot_17.png`)
- **Header Section**: Step 3 of 8 (segments 1 to 3 filled).
- **Layout**:
  - Large bar chart emoji (📊) centered at top.
  - Title: "How consistent are you?"
  - Subtitle: "Be honest — we'll tailor the experience just for you."
  - **List Layout**: Vertical stack of 3 full-width rounded list items:
    1. **Just starting out**: 🌱 (Seedling) | Subtext: "No worries — everyone starts somewhere"
    2. **I have some routines**: 🌿 (Herb/sprig) | Subtext: "Great! Let's level up together"
    3. **I'm pretty disciplined**: 🌳 (Deciduous tree) | Subtext: "Impressive — let's push further"
- **List Card States**:
  - **Unselected**: Soft light-grey background, no border. Left-aligned emoji, followed by vertical stack of Title and Subtext.
  - **Selected**: Light translucent active theme background tint, $2\text{px}$ solid active theme border, and a checkmark badge (`✓`) aligned vertically on the far right.
- **Button State**: Active once any single option is selected.

### Habit Quantity Preference (Onboarding Step 4 - `screenshot_19.png`, `screenshot_20.png`)
- **Header Section**: Step 4 of 8 (segments 1 to 4 filled).
- **Layout**:
  - Centered illustrative icon card containing white numbers `1 2` on the top line and `3 4` on the bottom line.
  - Title: "How many habits?"
  - Subtitle: "We'll seed your first set of goals based on this."
  - **List Layout**: Vertical stack of 3 selection cards:
    1. **Focused**: 🎯 (Target Emoji) | Inline Subtitle: `(1–2 habits)` | Description: `Quality over quantity`
    2. **Balanced**: ⚖️ (Balance Scales Emoji) | Inline Subtitle: `(3–5 habits)` | Description: `A healthy variety`
    3. **Ambitious**: 🚀 (Rocket Emoji) | Inline Subtitle: `(6+ habits)` | Description: `Go big or go home`
- **Card States**:
  - **Unselected**: Soft grey background, no border.
  - **Selected**: Translucent active theme tint, $2\text{px}$ solid active theme border, and a circular checkmark badge (`✓`) on the right.
- **Button State**: Active once selected.

### Wake Up Time (Onboarding Step 5 - `screenshot_22.png`, `screenshot_23.png`)
- **Header Section**: Step 5 of 8 (segments 1 to 5 filled).
- **Layout**:
  - Centered sunrise emoji (🌅) in a rounded card.
  - Title: "When do you wake up?"
  - Subtitle: "We'll schedule morning habits around this time."
  - **Time Picker**: Wheel-style container with rounded corners and a soft off-white background.
- **Picker Interaction**:
  - Two vertical scrolling columns: Hours on the left (24-hour style), Minutes on the right.
  - Translucent horizontal capsule overlaying the centered row (e.g., `08` and `00`).
  - Scrolling fades out the values near top and bottom edges.
- **Button State**: Transitions to a disabled state during active wheel scrolling and snaps back to active once the wheel locks into a value.

### Wind Down Time (Onboarding Step 6 - `screenshot_24.png`)
- **Header Section**: Step 6 of 8 (segments 1 to 6 filled).
- **Layout**:
  - Centered yellow crescent moon emoji (🌙).
  - Title: "When do you wind down?"
  - Subtitle: "Evening habits will be nudged around this time."
  - **Time Picker**: Centered card with rounded corners containing Hours/Minutes scrolling columns (similar to Step 5).
- **Button State**: Active once value snaps.

### Vibe / Theme Selector (Onboarding Step 7 - `screenshot_25.png`, `screenshot_29.png`)
- **Header Section**: Step 7 of 8 (segments 1 to 7 filled).
- **Layout**:
  - Centered artist palette emoji (🎨).
  - Title: "Pick your vibe"
  - Subtitle: "Choose a colour theme that feels like you."
  - **Grid Layout**: 2-column x 3-row grid of theme cards.
- **Theme Card Details**:
  - White rounded card with a rounded gradient swatch on top, dynamic emoji indicator, and text label below.
  - Themes: **Sunrise**, **Ocean**, **Forest**, **Lavender**, **Midnight**, **Rose** (detailed in Section 1).
- **Selection Behavior**:
  - Tapping a card applies a $2\text{px}$ solid border matching the theme color, a light background tint, and a circular checkmark badge (`✓`) on the top-right corner of the swatch.
  - **Dynamic Theme Swap**: The entire app instantly updates its accent colors (navigation chevrons, active progress segments, primary buttons) to the newly selected theme.

### Notification Permission Invitation (Onboarding Step 8 - `screenshot_31.png`, `screenshot_33.png`)
- **Header Section**: Step 8 of 8 (all 8 segments filled).
- **Layout**:
  - Centered 3D metallic gold bell illustration/emoji.
  - Title: "Stay on track"
  - Subtitle: "We'll send gentle reminders at exactly the right time — never spam."
  - **Feature Card**: Rounded card containing a circular active theme icon with a clock symbol, bold title "Timely reminders", and description "Get nudged at the right moment".
- **Interaction Flow**:
  - Tapping **"Enable Notifications"** triggers the native iOS/system notification permission prompt.
  - Upon acceptance/denial, the UI updates:
    - Displays a green success badge: `✔ Notifications enabled!`
    - Primary CTA button changes to `All set! Continue ➔`.

### Onboarding Summary Screen (`screenshot_34.png`)
- **Header**: Navigation and progress bars are entirely removed.
- **Layout**:
  - Centered celebratory party popper emoji (🎉) with radial festive elements.
  - Title: `"You're all set, [Name]!"` (e.g., "You're all set, Raj!")
  - Subtitle: `"Your personalised habit journey starts now."`
  - **Summary Chips**: Pill-shaped badges wrapping symmetrically in a centered container displaying the user's custom settings (e.g., `👋 Raj`, `📚 Learning`, `🌱 Just starting out`, `🎯 Focused`).
  - **Button**: Large solid active theme color CTA: `"Start my journey ➔"`.
- **Interaction**: Pressing the button persists the onboarding state and routes the user to the Main Dashboard.

---

## 3. Main Application Experience

### Global Shell & Floating Tab Bar
The core experience is anchored by a persistent, modern, floating capsule-style tab bar at the bottom.
- **Layout**: Centered capsule with rounded corners and shadow, hosting 4 tabs:
  1. ☀️ **Today**
  2. 🎯 **Goals**
  3. 📈 **Insights**
  4. 👤 **Profile**
- **States**: Active tab is highlighted with a soft-tint background pill matching the active theme color and the icon/text tinted in solid active theme color. Inactive tabs are dark grey.

---

### Dashboard ("Today" Tab - `screenshot_36.png`, `screenshot_41.png`, `screenshot_55.png`)

Displays daily progress, high-priority "Next Up" task, and today's schedule.

#### 1. Header Progress Card
- Full-width card with active theme diagonal gradient background.
- Top-left: Current date in uppercase (e.g., `SATURDAY, MAY 23`).
- Mid-left: Greeting `Hello,` and `[Name] ☀️` in bold white.
- Left-center: Circular progress indicator displaying current completion percentage.
- Right-center: Text indicators (e.g., `"0 of 2 done"` with subtitle `"A new day to grow."` OR `"1 of 2 done"` with subtitle `"Strong momentum today."`).

#### 2. Next Up Card
- High-visibility card with a light grey card background (`#F2F2F7`).
- Circular bubble containing the category emoji (e.g., `🎓` or `📚`).
- Heading: `"NEXT UP"` (small, bold, active theme color).
- Title: Bold task title (e.g., `"Watch a lesson"` or `"Read 20 pages"`).
- Subtitle: `"Reminder at HH:MM"` (e.g., `"Reminder at 09:30"`).
- Right-aligned: Green bell icon (`🔔`).

#### 3. Today's Reminders List
- Section title: `"Today's Reminders"` (bold, black).
- Vertical stack of tasks:
  - **Pending State**:
    - Left-aligned emoji bubble.
    - Title: Bold black text (e.g., `"Read 20 pages"`).
    - Subtitle: Clock icon + Time + Frequency + Bell icon (e.g., `🕒 20:30 · Daily 🔔`).
    - Right-aligned: Empty circular checkbox outline.
  - **Completed State**:
    - Text color changes to muted grey with a strikethrough (e.g., `Watch a lesson`).
    - Subtitle metadata updates (e.g., clock and bell icons replaced by streak fire icon: `🔥 1d`).
    - Right-aligned: Solid green/active theme checkbox filled with a white checkmark (`✓`).

#### 4. Interaction & Transition Logic
- Tapping a pending checkbox toggles completion state: updates progress circle (e.g., from `0%` to `50%`), increments completed counter, strikes through text, animates checkmark, and slides the next pending task into the "Next Up" slot.
- Tapping a reminder row navigates to the detailed editor for that habit.

---

### Goals Management ("Goals" Tab - `screenshot_43.png`)

Provides a clean overview of active habits, quick statistics, and entry for new goals.

#### 1. Quick Statistics Row
- A horizontal scroll/flex row containing three cards:
  1. **Total Goals**: Target icon `🎯` | Count | Subtext `Total`
  2. **Best Streak**: Fire icon `🔥` | Streak Length (e.g., `1d`) | Subtext `Best Streak`
  3. **Completed Today**: Check icon `✓` | Done Count (e.g., `1`) | Subtext `Done Today`

#### 2. Goals List
- Custom rows displaying every configured goal:
  - Left: Emojis inside colored bubbles (`🎓`, `📚`).
  - Center: Goal Title, Schedule (`🕒 09:30 · Weekdays`), and Streak stats (`🔥 1 day streak` in active theme green).
  - Upper-Right: A round active theme button with a white pencil icon (`✏️`) to trigger modifications.
  - Lower-Right: Small checked circle badge (`✓`) if completed today, or notification bell (`🔔`) if pending.

#### 3. Floating Action Button (FAB)
- Text: `+ Add Goal` (bold white).
- Styling: Rounded pill, solid active theme color gradient, floating in the bottom-right corner.
- Interaction: Tapping the FAB transitions to the New Goal wizard sheet.

---

### Insights & Analytics ("Insights" Tab - `screenshot_47.png`)

Provides deeper analytics, weekly performance, and streak leaderboards.

#### 1. Weekly Progress Card ("This Week")
- Title: `📅 This Week`
- Vertical bar chart representing days: `Sun`, `Mon`, `Tue`, `Wed`, `Thu`, `Fri`, `Sat`.
- Y-Axis ticks on the right edge (e.g., `0`, `1`, `2`, `3`).
- **Bars styling**: Unfilled targeted quota is represented by soft, light-tint translucent green bars; actual completed habits are stacked on top in solid vibrant theme green.

#### 2. Streak Leaders Card
- Title: `🔥 Streak Leaders`
- Ranks active habits by streak count:
  - Displays category emoji, habit title, horizontal fill-bar representing streak progression relative to other tasks, and the streak count (e.g., `🔥 1d` or `🔥 0d`).

---

### Profile & Settings ("Profile" Tab - `screenshot_50.png`, `screenshot_60.png`)

Displays user stats, personal info, and global theme configuration.

#### 1. User Profile Card
- Centered, large circular avatar with high-contrast active theme gradient background and a bold white initial (e.g., `R`) in the center.
- Display Name in bold black below avatar.
- Subtitle: `Building better habits every day`.

#### 2. Personal Info List Group
- Header: `👤 Personal Info` (small, bold, active theme color).
- Highly rounded card block containing:
  - **Name Row**: Left-aligned ID card icon 📇, label `Name`, right-aligned value (e.g., `Raj`), and navigation chevron `>`.
  - **Birthdate Row**: Left-aligned birthday cake icon 🎂, label `Birthdate`, right-aligned value (e.g., `23. May 1995` or placeholder `"Add your birthdate"`), and navigation chevron `>`.

#### 3. Appearance Section
- Header: `🎨 Appearance` (small, bold, active theme color).
- Symmetrical grid of theme option cards (Sunrise, Ocean, Forest, Lavender, Midnight, Rose).
- **Selection Behavior**:
  - Selected theme gets a solid $2\text{px}$ active theme colored border and a white-ring checkmark badge (`✓`) inside a solid circle on the top-right corner of the color swatch.
  - Tapping any card instantly updates the global style system.

---

### Edit Profile Screen / Modal (`screenshot_54.png`, `screenshot_62.png`)

Presented as a clean, slide-up bottom sheet modal overlaying a dimmed background.

#### 1. Header Bar
- Left Action: `Cancel` (flat active theme color text button).
- Center Title: `Edit Profile` (bold black).
- Right Action: `Save` (bold active theme color text button).

#### 2. Editable Elements
- **Avatar Display**: Read-only circular gradient avatar matching the Profile screen.
- **Name Input Field**:
  - Header: `👤 Name` (active theme color).
  - Text input container pre-populated with current name (e.g., `"Raj"`), displaying a clean input box with an ID-card icon 📇 and an active input insertion point.
- **Birthdate Configuration**:
  - Header: `Birthdate` (active theme color, preceded by 🎂 emoji).
  - Row displays toggle switch on the right side.
  - **Toggle ON State**: Green track background, handle aligned right. Expands a Wheel Date Picker panel below.
  - **Toggle OFF State**: Grey track background, collapses Date Picker panel.
  - **Wheel Date Picker Panel**:
    - Light-grey rounded background card.
    - Three independent vertical scrolling columns: Day, Month, Year (e.g., `23` `May` `2001`).
    - Semi-transparent horizontal belt overlaying the active selected center row. Text above and below fades out with an opacity gradient mask.
