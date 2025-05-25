import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Habit } from '@/types/habits';
import Colors from '@/constants/Colors';
import CategoryBadge from '@/components/CategoryBadge';
import { Heart, ChevronRight, Archive, Edit2 } from 'lucide-react-native';
import { useHabits } from '@/contexts/HabitContext';
import { useHapticFeedback } from '@/hooks/useHapticFeedback';
import { useRouter } from 'expo-router';

type HabitListItemProps = {
  habit: Habit;
  showArchiveButton?: boolean;
  showUnarchiveButton?: boolean;
};

export default function HabitListItem({ 
  habit, 
  showArchiveButton = false,
  showUnarchiveButton = false 
}: HabitListItemProps) {
  const { toggleFavorite, toggleArchive } = useHabits();
  const { triggerFeedback } = useHapticFeedback();
  const router = useRouter();
  
  const handleFavoritePress = () => {
    toggleFavorite(habit.id);
    triggerFeedback('light');
  };

  const handleArchivePress = () => {
    toggleArchive(habit.id);
    triggerFeedback('light');
  };

  const handleEditPress = () => {
    router.push({
      pathname: '/(tabs)/edit',
      params: { id: habit.id }
    });
  };
  
  return (
    <View style={styles.container}>
      <View style={styles.mainContent}>
        <View style={styles.textContainer}>
          <Text style={styles.title}>{habit.title}</Text>
          <Text 
            numberOfLines={2}
            ellipsizeMode="tail"
            style={styles.description}
          >
            {habit.description}
          </Text>
          <CategoryBadge category={habit.category} small />
        </View>
        
        <View style={styles.actionsContainer}>
          {showArchiveButton && (
            <TouchableOpacity
              style={styles.actionButton}
              onPress={handleArchivePress}
            >
              <Archive size={20} color={Colors.textSecondary} />
            </TouchableOpacity>
          )}
          
          {showUnarchiveButton && (
            <TouchableOpacity
              style={styles.actionButton}
              onPress={handleArchivePress}
            >
              <Archive size={20} color={Colors.primary} />
            </TouchableOpacity>
          )}

          <TouchableOpacity
            style={styles.actionButton}
            onPress={handleEditPress}
          >
            <Edit2 size={20} color={Colors.textSecondary} />
          </TouchableOpacity>
          
          <TouchableOpacity
            style={styles.actionButton}
            onPress={handleFavoritePress}
          >
            <Heart 
              size={20} 
              color={habit.isFavorite ? Colors.accent : Colors.textSecondary}
              fill={habit.isFavorite ? Colors.accent : 'transparent'}
            />
          </TouchableOpacity>
          
          <ChevronRight size={20} color={Colors.textSecondary} />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.cardBackground,
    borderRadius: 12,
    overflow: 'hidden',
    marginVertical: 6,
    borderWidth: 1,
    borderColor: Colors.border,
  },
  mainContent: {
    flexDirection: 'row',
    padding: 16,
  },
  textContainer: {
    flex: 1,
  },
  title: {
    fontSize: 16,
    fontWeight: '600',
    color: Colors.text,
    marginBottom: 4,
  },
  description: {
    fontSize: 14,
    color: Colors.textSecondary,
    marginBottom: 8,
  },
  actionsContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    paddingLeft: 16,
    flexDirection: 'row',
  },
  actionButton: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: Colors.cardBackground,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 8,
    borderWidth: 1,
    borderColor: Colors.border,
  },
});