import { createContext, useState, useContext, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Notifications from 'expo-notifications';
import { ReminderSettings, ReminderContextType } from '@/types/reminders';
import { useHabits } from './HabitContext';
import * as Device from 'expo-device';

const ReminderContext = createContext<ReminderContextType>({
  reminderSettings: { enabled: false, time: '09:00' },
  updateReminderSettings: () => {
  },
  scheduleNotification: async () => {
  },
  cancelNotification: async () => {
  }
});

export const useReminders = () => useContext(ReminderContext);

const REMINDER_STORAGE_KEY = 'reminder_settings';

export const ReminderProvider = ({ children }: { children: React.ReactNode }) => {
  const [reminderSettings, setReminderSettings] = useState<ReminderSettings>({
    enabled: false,
    time: '09:00'
  });
  const { habits } = useHabits();

  // Load reminder settings from storage
  useEffect(() => {
    const loadReminderSettings = async () => {
      try {
        const storedSettings = await AsyncStorage.getItem(REMINDER_STORAGE_KEY);
        if (storedSettings) {
          const parsedSettings = JSON.parse(storedSettings);
          setReminderSettings(parsedSettings);
        }
      } catch (error) {
        console.error('Error loading reminder settings:', error);
      }
    };

    loadReminderSettings();
  }, []);

  // Save reminder settings to storage
  useEffect(() => {
    const saveReminderSettings = async () => {
      try {
        await AsyncStorage.setItem(REMINDER_STORAGE_KEY, JSON.stringify(reminderSettings));
      } catch (error) {
        console.error('Error saving reminder settings:', error);
      }
    };

    saveReminderSettings();
  }, [reminderSettings]);

  // Request notification permissions
  const requestPermissions = async () => {
    // Only request permissions on iOS and Android
    if (Device.isDevice) {
      const { status } = await Notifications.requestPermissionsAsync();
      return status === 'granted';
    }
    // For web or other platforms, assume permission is granted
    return true;
  };

  // Get a random habit for the notification
  const getRandomHabit = () => {
    const activeHabits = habits.filter(habit => !habit.isArchived);
    if (activeHabits.length === 0) return null;

    const randomIndex = Math.floor(Math.random() * activeHabits.length);
    return activeHabits[randomIndex];
  };

  // Schedule daily notification
  const scheduleNotification = async () => {
    try {
      const hasPermission = await requestPermissions();
      if (!hasPermission) {
        console.log('Notification permission denied');
        return;
      }

      // Cancel existing notifications
      await Notifications.cancelAllScheduledNotificationsAsync();

      if (!reminderSettings.enabled) return;

      const [hours, minutes] = reminderSettings.time.split(':').map(Number);
      const habit = getRandomHabit();

      if (!habit) {
        console.log('No habits available for notification');
        return;
      }

      // Create notification content
      const notificationContent = {
        title: 'Time for a new habit! 🌟',
        body: `Today's suggestion: ${habit.title}`,
        data: { habitId: habit.id },
        sound: true
      };

      // Log notification content for debugging
      console.debug('Notification content:', notificationContent);

      // Schedule local notification for the specified time
      const trigger = {
        type: Notifications.SchedulableTriggerInputTypes.TIME_INTERVAL,
        seconds: 180,
        channelId: 'default',
      };

      const notificationId = await Notifications.scheduleNotificationAsync({
        content: notificationContent,
        trigger
      });

      console.log(`Notification scheduled for ${reminderSettings.time} with ID: ${notificationId}`);
    } catch (error) {
      console.error('Error scheduling notification:', error);
    }
  };

  // Cancel all scheduled notifications
  const cancelNotification = async () => {
    try {
      await Notifications.cancelAllScheduledNotificationsAsync();
      console.log('All notifications cancelled');
    } catch (error) {
      console.error('Error cancelling notifications:', error);
    }
  };

  // Update reminder settings
  const updateReminderSettings = (newSettings: Partial<ReminderSettings>) => {
    const updatedSettings = { ...reminderSettings, ...newSettings };
    setReminderSettings(updatedSettings);

    // Schedule or cancel notifications based on new settings
    if (updatedSettings.enabled) {
      scheduleNotification();
    } else {
      cancelNotification();
    }
  };

  // Schedule notification when settings change
  useEffect(() => {
    if (reminderSettings.enabled) {
      scheduleNotification();
    } else {
      cancelNotification();
    }
  }, [reminderSettings.enabled, reminderSettings.time, habits]);

  const value = {
    reminderSettings,
    updateReminderSettings,
    scheduleNotification,
    cancelNotification
  };

  return (
    <ReminderContext.Provider value={value}>
      {children}
    </ReminderContext.Provider>
  );
};
