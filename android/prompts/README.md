# LLM Prompt Templates — Better Everyday Android

## Execution Order

```
Batch 1 (run all 3 in parallel)
├── 01-theme-system.md       → ThemeSystem.kt
├── 02-room-database.md      → Room entities + DAOs
└── 03-datastore-prefs.md    → DataStore preferences

        ↓ (wait for all 3 to finish)

Batch 2 (run all in parallel — each needs ThemeSystem + DB + Prefs)
├── Splash screen
├── Welcome screen
├── Onboarding Step 1 — Name Entry
├── Onboarding Step 2 — Focus Selection
├── Onboarding Step 3 — Consistency Level
├── Onboarding Step 4 — Habit Quantity
├── Onboarding Step 5 — Wake Up Time
├── Onboarding Step 6 — Wind Down Time
├── Onboarding Step 7 — Theme Selector
├── Onboarding Step 8 — Notifications
└── Onboarding Summary screen

        ↓

Batch 3 (run all 4 in parallel — needs completed screens + DB)
├── Today tab (Dashboard)
├── Goals tab
├── Insights tab
└── Profile tab

        ↓

Batch 4 (modals/sheets — need their parent screens)
├── Edit Profile sheet
├── Add Goal sheet
└── Habit detail editor

        ↓

Final: Navigation wiring + App entry point routing
```

## How to Use Each Prompt

1. Start a new Claude Code session (or worktree) per parallel task
2. Paste the full contents of the `.md` prompt file as your first message
3. After the LLM produces the file(s), paste the **actual generated code** (not a description) into the prompt for every dependent task in the next batch

## Critical Rule

Never describe what a file does in a downstream prompt — always paste the actual file content.
Naming drift between agents is the biggest source of bugs in parallel LLM workflows.
