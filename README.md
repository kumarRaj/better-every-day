# HabitSync

A beautiful, modern habit tracking application built with Expo and React Native that helps users develop and maintain positive habits through daily suggestions and tracking.

## Features

- 📱 Cross-platform support (iOS, Android, Web)
- 🎯 Daily habit suggestions
- 📚 Customizable habit library
- 🏷️ Category-based organization
- ⭐ Favorite habits system
- 🔍 Search and filter capabilities
- 🌙 Dark mode support
- 🔔 Daily reminders with customizable time (mobile only)
- 💾 Persistent storage with AsyncStorage
- ✨ Beautiful animations with Reanimated

## Tech Stack

- Expo SDK 53
- React Native
- Expo Router for navigation
- React Native Reanimated for animations
- Expo Notifications for daily reminders
- Lucide Icons
- AsyncStorage for data persistence
- TypeScript for type safety

## Project Structure

```
├── app/                   # Application routes
│   ├── _layout.tsx       # Root layout
│   ├── (tabs)/           # Tab-based navigation
│   │   ├── _layout.tsx   # Tab configuration
│   │   ├── index.tsx     # Today's habits
│   │   ├── library.tsx   # Habit library
│   │   ├── add.tsx       # Add new habits
│   │   └── settings.tsx  # App settings
├── components/           # Reusable components
├── constants/           # App constants and defaults
├── contexts/           # React contexts
├── hooks/             # Custom hooks
└── types/            # TypeScript definitions
```

## Features in Detail

### Today Screen
- Random habit suggestions
- Favorite/unfavorite habits
- Beautiful card-based UI
- Smooth animations between habits

### Library Screen
- Complete habit catalog
- Search functionality
- Category-based filtering
- List/grid view options
- Quick actions for each habit

### Add Habit Screen
- Intuitive habit creation
- Category selection
- Description support
- Form validation
- Haptic feedback (mobile only)

### Settings Screen
- Theme customization
- Daily reminder preferences with time selection
- Data management
- App information
- Reset functionality

## Design Philosophy

The app follows these key design principles:
- Clean and minimalist UI
- Smooth animations and transitions
- Consistent visual hierarchy
- Intuitive navigation
- Responsive layout across devices

## Development Setup

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm run dev
```

3. Open the app:
- Web: Open the provided local URL in your browser
- iOS/Android: Scan the QR code with Expo Go

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.