import { View, Text, StyleSheet, Switch, TouchableOpacity, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import Animated, { FadeIn } from 'react-native-reanimated';
import Header from '@/components/Header';
import Colors from '@/constants/Colors';
import { useHabits } from '@/contexts/HabitContext';
import { Bell, Moon, Info, Clock, ChevronRight, Trash2, CloudSun as CloudSync, Star } from 'lucide-react-native';
import { useState } from 'react';

export default function SettingsScreen() {
  const { resetToDefaults } = useHabits();
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);
  const [darkModeEnabled, setDarkModeEnabled] = useState(false);
  
  return (
    <SafeAreaView style={styles.container} edges={['top']}>
      <Header title="Settings" />
      
      <ScrollView style={styles.content}>
        <Animated.View entering={FadeIn.duration(400)}>
          <Text style={styles.sectionTitle}>App Settings</Text>
          
          <View style={styles.settingsGroup}>
            <View style={styles.settingRow}>
              <View style={styles.settingIconContainer}>
                <Bell size={20} color={Colors.primary} />
              </View>
              <View style={styles.settingTextContainer}>
                <Text style={styles.settingText}>Daily Reminders</Text>
              </View>
              <Switch
                value={notificationsEnabled}
                onValueChange={setNotificationsEnabled}
                trackColor={{ false: Colors.border, true: Colors.primaryLight }}
                thumbColor={notificationsEnabled ? Colors.primary : Colors.textSecondary}
              />
            </View>
            
            <View style={styles.separator} />
            
            <View style={styles.settingRow}>
              <View style={styles.settingIconContainer}>
                <Moon size={20} color={Colors.primary} />
              </View>
              <View style={styles.settingTextContainer}>
                <Text style={styles.settingText}>Dark Mode</Text>
              </View>
              <Switch
                value={darkModeEnabled}
                onValueChange={setDarkModeEnabled}
                trackColor={{ false: Colors.border, true: Colors.primaryLight }}
                thumbColor={darkModeEnabled ? Colors.primary : Colors.textSecondary}
              />
            </View>
            
            <View style={styles.separator} />
            
            <View style={styles.settingRow}>
              <View style={styles.settingIconContainer}>
                <Clock size={20} color={Colors.primary} />
              </View>
              <View style={styles.settingTextContainer}>
                <Text style={styles.settingText}>Reminder Time</Text>
                <Text style={styles.settingSubtext}>9:00 AM</Text>
              </View>
              <ChevronRight size={20} color={Colors.textSecondary} />
            </View>
          </View>
          
          <Text style={styles.sectionTitle}>Account</Text>
          
          <View style={styles.settingsGroup}>
            <View style={styles.settingRow}>
              <View style={styles.settingIconContainer}>
                <CloudSync size={20} color={Colors.primary} />
              </View>
              <View style={styles.settingTextContainer}>
                <Text style={styles.settingText}>Sync Data</Text>
                <Text style={styles.settingSubtext}>Last synced: Never</Text>
              </View>
              <ChevronRight size={20} color={Colors.textSecondary} />
            </View>
            
            <View style={styles.separator} />
            
            <View style={styles.settingRow}>
              <View style={styles.settingIconContainer}>
                <Star size={20} color={Colors.primary} />
              </View>
              <View style={styles.settingTextContainer}>
                <Text style={styles.settingText}>Rate App</Text>
              </View>
              <ChevronRight size={20} color={Colors.textSecondary} />
            </View>
            
            <View style={styles.separator} />
            
            <View style={styles.settingRow}>
              <View style={styles.settingIconContainer}>
                <Info size={20} color={Colors.primary} />
              </View>
              <View style={styles.settingTextContainer}>
                <Text style={styles.settingText}>About</Text>
                <Text style={styles.settingSubtext}>Version 1.0.0</Text>
              </View>
              <ChevronRight size={20} color={Colors.textSecondary} />
            </View>
          </View>
          
          <Text style={styles.sectionTitle}>Data</Text>
          
          <View style={styles.settingsGroup}>
            <TouchableOpacity style={styles.settingRow} onPress={resetToDefaults}>
              <View style={styles.settingIconContainer}>
                <Trash2 size={20} color={Colors.error} />
              </View>
              <View style={styles.settingTextContainer}>
                <Text style={[styles.settingText, { color: Colors.error }]}>Reset to Defaults</Text>
                <Text style={styles.settingSubtext}>Restore original habit suggestions</Text>
              </View>
            </TouchableOpacity>
          </View>
        </Animated.View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  content: {
    flex: 1,
    paddingHorizontal: 16,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: Colors.textSecondary,
    marginTop: 24,
    marginBottom: 8,
    marginLeft: 8,
  },
  settingsGroup: {
    backgroundColor: Colors.cardBackground,
    borderRadius: 12,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: Colors.border,
  },
  settingRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 16,
    paddingHorizontal: 16,
  },
  settingIconContainer: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: Colors.primaryLightest,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 16,
  },
  settingTextContainer: {
    flex: 1,
  },
  settingText: {
    fontSize: 16,
    fontWeight: '500',
    color: Colors.text,
  },
  settingSubtext: {
    fontSize: 14,
    color: Colors.textSecondary,
    marginTop: 2,
  },
  separator: {
    height: 1,
    backgroundColor: Colors.border,
    marginLeft: 56,
  },
});