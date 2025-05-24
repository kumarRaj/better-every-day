import { View, Text, StyleSheet } from 'react-native';
import { Habit } from '@/types/habits';
import Colors from '@/constants/Colors';
import Animated, { FadeIn } from 'react-native-reanimated';
import CategoryBadge from '@/components/CategoryBadge';

type HabitCardProps = {
  habit: Habit;
};

export default function HabitCard({ habit }: HabitCardProps) {
  return (
    <Animated.View 
      entering={FadeIn.duration(400)}
      style={styles.container}
    >
      <View style={styles.header}>
        <CategoryBadge category={habit.category} />
      </View>
      
      <View style={styles.content}>
        <Text style={styles.title}>{habit.title}</Text>
        <Text style={styles.description}>{habit.description}</Text>
      </View>
      
      <View style={styles.footer}>
        <Text style={styles.footerText}>Today's Suggestion</Text>
      </View>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.cardBackground,
    borderRadius: 16,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: Colors.border,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 2,
  },
  header: {
    paddingHorizontal: 16,
    paddingTop: 16,
    paddingBottom: 8,
  },
  content: {
    padding: 16,
    paddingTop: 8,
  },
  title: {
    fontSize: 22,
    fontWeight: '700',
    color: Colors.text,
    marginBottom: 12,
  },
  description: {
    fontSize: 16,
    lineHeight: 24,
    color: Colors.textSecondary,
  },
  footer: {
    paddingHorizontal: 16,
    paddingVertical: 12,
    backgroundColor: Colors.primaryLightest,
    borderTopWidth: 1,
    borderTopColor: Colors.border,
  },
  footerText: {
    color: Colors.primary,
    fontSize: 14,
    fontWeight: '500',
    textAlign: 'center',
  },
});