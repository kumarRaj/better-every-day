import { useState } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  TextInput, 
  TouchableOpacity, 
  ScrollView, 
  KeyboardAvoidingView, 
  Platform 
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useRouter } from 'expo-router';
import { useHabits } from '@/contexts/HabitContext';
import Animated, { FadeIn } from 'react-native-reanimated';
import Colors from '@/constants/Colors';
import Header from '@/components/Header';
import { Save, X } from 'lucide-react-native';
import { useHapticFeedback } from '@/hooks/useHapticFeedback';

export default function AddHabitScreen() {
  const { categories, addHabit } = useHabits();
  const router = useRouter();
  const { triggerFeedback } = useHapticFeedback();
  
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [category, setCategory] = useState(categories[0] || 'General');
  const [error, setError] = useState<string | null>(null);

  const handleAddHabit = () => {
    if (!title.trim()) {
      setError('Please enter a habit title');
      return;
    }
    
    addHabit({
      id: Date.now().toString(),
      title: title.trim(),
      description: description.trim() || 'No description',
      category,
      isFavorite: false,
      isArchived: false,
      createdAt: new Date().toISOString(),
    });
    
    triggerFeedback('success');
    router.navigate('/(tabs)');
  };

  return (
    <SafeAreaView style={styles.container} edges={['top']}>
      <KeyboardAvoidingView 
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <Header 
          title="Add New Habit" 
          rightButton={{
            icon: <X size={24} color={Colors.text} />,
            onPress: () => router.back()
          }}
        />
        
        <ScrollView style={styles.scrollView}>
          <Animated.View 
            entering={FadeIn.duration(400)}
            style={styles.formContainer}
          >
            <View style={styles.inputContainer}>
              <Text style={styles.label}>Habit Title</Text>
              <TextInput
                style={styles.input}
                value={title}
                onChangeText={(text) => {
                  setTitle(text);
                  setError(null);
                }}
                placeholder="e.g., Read for 20 minutes"
                placeholderTextColor={Colors.textTertiary}
                maxLength={50}
              />
              {error && <Text style={styles.errorText}>{error}</Text>}
              <Text style={styles.characterCount}>
                {title.length}/50
              </Text>
            </View>
            
            <View style={styles.inputContainer}>
              <Text style={styles.label}>Description</Text>
              <TextInput
                style={[styles.input, styles.textArea]}
                value={description}
                onChangeText={setDescription}
                placeholder="Why is this habit important to you?"
                placeholderTextColor={Colors.textTertiary}
                multiline
                maxLength={200}
              />
              <Text style={styles.characterCount}>
                {description.length}/200
              </Text>
            </View>
            
            <View style={styles.inputContainer}>
              <Text style={styles.label}>Category</Text>
              <View style={styles.categoriesContainer}>
                {categories.map((cat) => (
                  <TouchableOpacity
                    key={cat}
                    style={[
                      styles.categoryPill,
                      cat === category && { backgroundColor: Colors.primaryLight }
                    ]}
                    onPress={() => {
                      setCategory(cat);
                      triggerFeedback('light');
                    }}
                  >
                    <Text
                      style={[
                        styles.categoryPillText,
                        cat === category && { color: Colors.primary }
                      ]}
                    >
                      {cat}
                    </Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>
          </Animated.View>
        </ScrollView>
        
        <View style={styles.bottomBar}>
          <TouchableOpacity
            style={styles.saveButton}
            onPress={handleAddHabit}
          >
            <Save size={20} color={Colors.white} style={{ marginRight: 8 }} />
            <Text style={styles.saveButtonText}>Save Habit</Text>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  scrollView: {
    flex: 1,
  },
  formContainer: {
    padding: 16,
  },
  inputContainer: {
    marginBottom: 24,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: Colors.text,
    marginBottom: 8,
  },
  input: {
    backgroundColor: Colors.cardBackground,
    borderRadius: 12,
    padding: 16,
    fontSize: 16,
    color: Colors.text,
    borderWidth: 1,
    borderColor: Colors.border,
  },
  textArea: {
    height: 120,
    textAlignVertical: 'top',
  },
  errorText: {
    color: Colors.error,
    marginTop: 8,
    fontSize: 14,
  },
  characterCount: {
    fontSize: 12,
    color: Colors.textSecondary,
    textAlign: 'right',
    marginTop: 4,
  },
  categoriesContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 8,
  },
  categoryPill: {
    backgroundColor: Colors.cardBackground,
    borderRadius: 20,
    paddingHorizontal: 16,
    paddingVertical: 8,
    margin: 4,
    borderWidth: 1,
    borderColor: Colors.border,
  },
  categoryPillText: {
    fontSize: 14,
    color: Colors.text,
  },
  bottomBar: {
    padding: 16,
    borderTopWidth: 1,
    borderTopColor: Colors.border,
    backgroundColor: Colors.background,
  },
  saveButton: {
    backgroundColor: Colors.primary,
    borderRadius: 30,
    padding: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  saveButtonText: {
    color: Colors.white,
    fontSize: 16,
    fontWeight: '600',
  },
});