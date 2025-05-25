import { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, FlatList, TextInput } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import Animated, { FadeIn, FadeOut, Layout } from 'react-native-reanimated';
import { useHabits } from '@/contexts/HabitContext';
import Header from '@/components/Header';
import Colors from '@/constants/Colors';
import HabitListItem from '@/components/HabitListItem';
import { Search, Plus, BookOpen, Filter, Archive, Edit2 } from 'lucide-react-native';
import { useRouter } from 'expo-router';

type Section = 'active' | 'archived';

export default function LibraryScreen() {
  const { habits, categories } = useHabits();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [activeSection, setActiveSection] = useState<Section>('active');
  const router = useRouter();

  const filteredHabits = habits.filter(habit => {
    const matchesSearch = habit.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         habit.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = selectedCategory ? habit.category === selectedCategory : true;
    const matchesSection = activeSection === 'active' ? !habit.isArchived : habit.isArchived;
    return matchesSearch && matchesCategory && matchesSection;
  });

  const renderCategoryPill = ({ item }: { item: string }) => {
    const isSelected = selectedCategory === item;
    return (
      <TouchableOpacity
        style={[
          styles.categoryPill,
          isSelected && { backgroundColor: Colors.primaryLight }
        ]}
        onPress={() => setSelectedCategory(isSelected ? null : item)}
      >
        <Text
          style={[
            styles.categoryPillText,
            isSelected && { color: Colors.primary }
          ]}
        >
          {item}
        </Text>
      </TouchableOpacity>
    );
  };

  const renderSectionHeader = () => (
    <View style={styles.sectionHeader}>
      <TouchableOpacity
        style={[
          styles.sectionButton,
          activeSection === 'active' && styles.activeSectionButton
        ]}
        onPress={() => setActiveSection('active')}
      >
        <Text style={[
          styles.sectionButtonText,
          activeSection === 'active' && styles.activeSectionButtonText
        ]}>
          Active Habits
        </Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={[
          styles.sectionButton,
          activeSection === 'archived' && styles.activeSectionButton
        ]}
        onPress={() => setActiveSection('archived')}
      >
        <Text style={[
          styles.sectionButtonText,
          activeSection === 'archived' && styles.activeSectionButtonText
        ]}>
          Archived
        </Text>
      </TouchableOpacity>
    </View>
  );

  return (
    <SafeAreaView style={styles.container} edges={['top']}>
      <Header title="Habit Library" />
      
      <Animated.View style={styles.searchContainer} layout={Layout}>
        <View style={styles.searchInputContainer}>
          <Search size={20} color={Colors.textSecondary} style={styles.searchIcon} />
          <TextInput
            style={styles.searchInput}
            placeholder="Search habits..."
            placeholderTextColor={Colors.textSecondary}
            value={searchQuery}
            onChangeText={setSearchQuery}
          />
        </View>
        
        <View style={styles.filterSection}>
          <View style={styles.filterHeader}>
            <Filter size={16} color={Colors.textSecondary} />
            <Text style={styles.filterText}>Categories</Text>
          </View>
          
          <FlatList
            data={categories}
            renderItem={renderCategoryPill}
            keyExtractor={item => item}
            horizontal
            showsHorizontalScrollIndicator={false}
            contentContainerStyle={styles.categoriesContainer}
          />
        </View>
      </Animated.View>

      {renderSectionHeader()}
      
      <Animated.FlatList
        data={filteredHabits}
        keyExtractor={item => item.id}
        renderItem={({ item }) => (
          <Animated.View
            entering={FadeIn.duration(300)}
            exiting={FadeOut.duration(200)}
            layout={Layout}
          >
            <HabitListItem 
              habit={item} 
              showArchiveButton={activeSection === 'active'}
              showUnarchiveButton={activeSection === 'archived'}
            />
          </Animated.View>
        )}
        contentContainerStyle={styles.habitsList}
        ItemSeparatorComponent={() => <View style={styles.separator} />}
        ListEmptyComponent={() => (
          <View style={styles.emptyContainer}>
            <BookOpen size={48} color={Colors.textSecondary} />
            <Text style={styles.emptyText}>No habits found</Text>
            <Text style={styles.emptySubtext}>
              {searchQuery || selectedCategory
                ? "Try adjusting your search or filter"
                : activeSection === 'active'
                  ? "Add your first habit to get started"
                  : "No archived habits yet"}
            </Text>
          </View>
        )}
      />
      
      {activeSection === 'active' && (
        <TouchableOpacity 
          style={styles.addButton}
          onPress={() => router.push('/(tabs)/add')}
        >
          <Plus size={24} color={Colors.white} />
        </TouchableOpacity>
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  searchContainer: {
    paddingHorizontal: 16,
    paddingBottom: 8,
  },
  searchInputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.cardBackground,
    borderRadius: 12,
    paddingHorizontal: 12,
    height: 48,
    marginBottom: 12,
  },
  searchIcon: {
    marginRight: 8,
  },
  searchInput: {
    flex: 1,
    fontSize: 16,
    color: Colors.text,
    height: 48,
  },
  filterSection: {
    marginBottom: 8,
  },
  filterHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  filterText: {
    fontSize: 14,
    color: Colors.textSecondary,
    marginLeft: 4,
  },
  categoriesContainer: {
    paddingRight: 16,
  },
  categoryPill: {
    backgroundColor: Colors.cardBackground,
    borderRadius: 20,
    paddingHorizontal: 16,
    paddingVertical: 8,
    marginRight: 8,
  },
  categoryPillText: {
    fontSize: 14,
    color: Colors.text,
  },
  sectionHeader: {
    flexDirection: 'row',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: Colors.border,
  },
  sectionButton: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    marginRight: 8,
    borderRadius: 20,
  },
  activeSectionButton: {
    backgroundColor: Colors.primaryLight,
  },
  sectionButtonText: {
    fontSize: 14,
    color: Colors.textSecondary,
  },
  activeSectionButtonText: {
    color: Colors.primary,
    fontWeight: '600',
  },
  habitsList: {
    paddingHorizontal: 16,
    paddingBottom: 100, // Extra padding for the add button
  },
  separator: {
    height: 1,
    backgroundColor: Colors.border,
    marginVertical: 8,
  },
  emptyContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 60,
    paddingHorizontal: 16,
  },
  emptyText: {
    fontSize: 18,
    fontWeight: '600',
    color: Colors.text,
    marginTop: 16,
  },
  emptySubtext: {
    fontSize: 16,
    color: Colors.textSecondary,
    textAlign: 'center',
    marginTop: 8,
  },
  addButton: {
    position: 'absolute',
    right: 24,
    bottom: 24,
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 8,
  },
});