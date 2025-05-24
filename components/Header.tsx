import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import Animated, { FadeIn } from 'react-native-reanimated';
import Colors from '@/constants/Colors';

type HeaderProps = {
  title: string;
  rightButton?: {
    icon: React.ReactNode;
    onPress: () => void;
  };
};

export default function Header({ title, rightButton }: HeaderProps) {
  const insets = useSafeAreaInsets();
  
  return (
    <Animated.View 
      entering={FadeIn.duration(400)}
      style={[
        styles.container,
        { paddingTop: 16 + (insets.top ? 0 : 16) } // Add extra padding if no safe area
      ]}
    >
      <View style={styles.content}>
        <Text style={styles.title}>{title}</Text>
        
        {rightButton && (
          <TouchableOpacity style={styles.button} onPress={rightButton.onPress}>
            {rightButton.icon}
          </TouchableOpacity>
        )}
      </View>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.background,
    paddingBottom: 16,
    paddingHorizontal: 16,
    borderBottomWidth: 1,
    borderBottomColor: Colors.border,
  },
  content: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  title: {
    fontSize: 28,
    fontWeight: '700',
    color: Colors.text,
  },
  button: {
    width: 40,
    height: 40,
    borderRadius: 20,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: Colors.cardBackground,
    borderWidth: 1,
    borderColor: Colors.border,
  },
});