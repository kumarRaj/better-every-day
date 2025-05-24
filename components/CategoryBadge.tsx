import { View, Text, StyleSheet } from 'react-native';
import Colors from '@/constants/Colors';

type CategoryBadgeProps = {
  category: string;
  small?: boolean;
};

// Function to generate a consistent color based on the category name
const getCategoryColor = (category: string) => {
  const categories: Record<string, { bg: string; text: string }> = {
    'Mindfulness': { bg: Colors.primaryLightest, text: Colors.primary },
    'Health': { bg: '#E5FCEF', text: '#34C759' }, // Green
    'Fitness': { bg: '#FFF2E8', text: '#FF9500' }, // Orange
    'Learning': { bg: Colors.secondaryLightest, text: Colors.secondary },
    'Productivity': { bg: '#F0F0F0', text: '#545454' }, // Gray
    'Relationships': { bg: '#FFE8EA', text: '#FF2D55' }, // Pink
    'Career': { bg: '#FFF9E6', text: '#FFCC00' }, // Yellow
    'Personal Growth': { bg: '#E9F5FF', text: '#64D2FF' }, // Light blue
  };
  
  return categories[category] || { bg: Colors.primaryLightest, text: Colors.primary };
};

export default function CategoryBadge({ category, small = false }: CategoryBadgeProps) {
  const colors = getCategoryColor(category);
  
  return (
    <View 
      style={[
        styles.container, 
        { backgroundColor: colors.bg },
        small && styles.smallContainer
      ]}
    >
      <Text 
        style={[
          styles.text, 
          { color: colors.text },
          small && styles.smallText
        ]}
      >
        {category}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignSelf: 'flex-start',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  smallContainer: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  text: {
    fontSize: 14,
    fontWeight: '600',
  },
  smallText: {
    fontSize: 12,
  },
});