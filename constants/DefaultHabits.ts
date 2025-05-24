import { Habit } from '@/types/habits';

const DefaultHabits: Habit[] = [
  {
    id: '1',
    title: 'Meditate for 10 minutes',
    description: 'Take time to clear your mind and focus on your breath. Just 10 minutes of mindfulness can reduce stress and improve focus.',
    category: 'Mindfulness',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '2',
    title: 'Drink 8 glasses of water',
    description: 'Stay hydrated throughout the day. Proper hydration improves cognitive function, energy levels, and overall well-being.',
    category: 'Health',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '3',
    title: 'Read for 20 minutes',
    description: 'Reading regularly expands your knowledge, reduces stress, and improves focus and concentration.',
    category: 'Learning',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '4',
    title: 'Practice gratitude',
    description: 'Write down three things you\'re grateful for today. Gratitude practice increases happiness and reduces negative emotions.',
    category: 'Mindfulness',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '5',
    title: 'Take a 30-minute walk',
    description: 'Walking improves cardiovascular health, boosts creativity, and helps clear your mind.',
    category: 'Fitness',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '6',
    title: 'Learn one new word',
    description: 'Expanding your vocabulary improves communication skills and cognitive function over time.',
    category: 'Learning',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '7',
    title: 'Stretch for 5 minutes',
    description: 'Regular stretching increases flexibility, improves posture, and helps prevent injuries.',
    category: 'Fitness',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '8',
    title: 'Write in a journal',
    description: 'Journaling helps process emotions, gain clarity, and track personal growth over time.',
    category: 'Mindfulness',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '9',
    title: 'Plan your day',
    description: 'Take 5 minutes to plan your priorities. This simple habit increases productivity and reduces stress.',
    category: 'Productivity',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '10',
    title: 'Eat a piece of fruit',
    description: 'Fruits provide essential vitamins, minerals, and fiber that support overall health.',
    category: 'Health',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '11',
    title: 'Practice deep breathing',
    description: 'Deep, intentional breathing reduces stress, lowers blood pressure, and improves mental clarity.',
    category: 'Mindfulness',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  },
  {
    id: '12',
    title: 'Learn something new for 15 minutes',
    description: 'Consistent learning keeps your mind sharp and expands your knowledge base over time.',
    category: 'Learning',
    isFavorite: false,
    createdAt: new Date().toISOString(),
  }
];

export const DefaultCategories = [
  'Mindfulness',
  'Health',
  'Fitness',
  'Learning',
  'Productivity',
  'Relationships',
  'Career',
  'Personal Growth'
];

export default DefaultHabits;