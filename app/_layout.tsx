import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import { useFrameworkReady } from '@/hooks/useFrameworkReady';
import { useNotifications } from '@/hooks/useNotifications';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import { HabitProvider } from '@/contexts/HabitContext';
import { ReminderProvider } from '@/contexts/ReminderContext';

export default function RootLayout() {
  useFrameworkReady();
  useNotifications();

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <HabitProvider>
        <ReminderProvider>
          <Stack screenOptions={{ headerShown: false }}>
            <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
            <Stack.Screen name="+not-found" />
          </Stack>
          <StatusBar style="auto" />
        </ReminderProvider>
      </HabitProvider>
    </GestureHandlerRootView>
  );
}