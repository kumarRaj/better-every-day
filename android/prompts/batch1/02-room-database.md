# Task: Build Room Database Schema

## Context

You are building the Android version of "Better Everyday", a habit tracker app.
This is **Batch 1, Task 2 of 3**. It runs in parallel with Tasks 1 and 3.

This task defines the local persistence layer. Every screen in the app reads from
or writes to this schema. Get the entities and DAOs right here — they will not
be changed once screens are built on top of them.

---

## Your Output

Produce these files under `android/app/src/main/java/com/bettereveryday/data/local/`:

```
db/
  AppDatabase.kt        — Room database class
  entity/
    HabitEntity.kt
    CompletionEntity.kt
  dao/
    HabitDao.kt
    CompletionDao.kt
```

Do NOT create repositories, ViewModels, use cases, or UI code.

---

## Domain Model (derive your schema from this)

### Habit

A habit is a recurring task the user wants to build. Key properties:

- Unique ID (auto-generated)
- Title (e.g. "Read 20 pages")
- Category emoji (e.g. "📚") — stored as a String
- Schedule type: `DAILY`, `WEEKDAYS`, or `WEEKENDS`
- Reminder time: stored as two integers — `reminderHour` (0–23), `reminderMinute` (0–59)
- Focus area: one of `MINDFULNESS`, `FITNESS`, `LEARNING`, `SLEEP`, `NUTRITION`, `PRODUCTIVITY`
- Created timestamp (epoch millis)
- Sort order (integer, for user-defined reordering)

### Completion

Records a single day's completion of a habit. Key properties:

- Unique ID (auto-generated)
- Foreign key → Habit ID
- Completed date: stored as `yyyy-MM-dd` String (e.g. `"2026-05-23"`)
- Completed timestamp (epoch millis)

---

## Required DAO Queries

### HabitDao

| Method | Return | Description |
|---|---|---|
| `getAllHabits()` | `Flow<List<HabitEntity>>` | All habits ordered by sortOrder |
| `getHabitById(id)` | `HabitEntity?` | Single habit by ID |
| `insertHabit(habit)` | `Long` | Insert, return new row ID |
| `updateHabit(habit)` | `Unit` | Full update |
| `deleteHabit(habit)` | `Unit` | Delete by entity |

### CompletionDao

| Method | Return | Description |
|---|---|---|
| `getCompletionsForDate(date)` | `Flow<List<CompletionEntity>>` | All completions for a given date string |
| `getCompletionsForHabit(habitId)` | `Flow<List<CompletionEntity>>` | All completions for one habit (for streak calc) |
| `getCompletionsInRange(start, end)` | `Flow<List<CompletionEntity>>` | Date range query for weekly Insights chart — `start` and `end` are `yyyy-MM-dd` strings |
| `insertCompletion(completion)` | `Long` | Insert completion |
| `deleteCompletion(habitId, date)` | `Unit` | Un-complete a habit for a given date (toggle off) |
| `isCompleted(habitId, date)` | `Boolean` | Check if a habit is done for a date |

---

## Constraints

- Use **Room 2.6+**
- Use **Kotlin coroutines + Flow** (no RxJava)
- `AppDatabase` must be a singleton — provide a `getInstance(context)` companion object method
- Enable foreign key constraints: `@Database` must include `foreignKeys` on `CompletionEntity` referencing `HabitEntity` with `CASCADE` delete
- All entities must have `@Entity` with explicit `tableName`
- No TypeConverters needed — store dates as strings, times as two integer columns
- No comments beyond what is necessary to understand a non-obvious decision
