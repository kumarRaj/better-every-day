import { View, Text, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import { BottomTabBarProps } from '@react-navigation/bottom-tabs';
import Animated, { useAnimatedStyle, withTiming, useDerivedValue } from 'react-native-reanimated';
import Colors from '@/constants/Colors';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Platform } from 'react-native';

export default function TabBar({ state, descriptors, navigation }: BottomTabBarProps) {
  const insets = useSafeAreaInsets();
  const windowWidth = Dimensions.get('window').width;
  const tabWidth = windowWidth / state.routes.length;
  
  const indicatorPosition = useDerivedValue(() => {
    return state.index * tabWidth;
  });
  
  const animatedIndicatorStyle = useAnimatedStyle(() => {
    return {
      transform: [
        { translateX: withTiming(indicatorPosition.value, { duration: 250 }) },
      ],
    };
  });

  return (
    <View style={styles.tabBar}>
      <Animated.View
        style={[
          styles.indicator,
          { width: tabWidth },
          animatedIndicatorStyle,
        ]}
      />
      
      {state.routes.map((route, index) => {
        const { options } = descriptors[route.key];
        const isFocused = state.index === index;
        
        const onPress = () => {
          const event = navigation.emit({
            type: 'tabPress',
            target: route.key,
            canPreventDefault: true,
          });
          
          if (!isFocused && !event.defaultPrevented) {
            navigation.navigate(route.name);
          }
        };
        
        return (
          <TouchableOpacity
            key={route.key}
            accessibilityRole="button"
            accessibilityState={isFocused ? { selected: true } : {}}
            accessibilityLabel={options.tabBarAccessibilityLabel}
            onPress={onPress}
            style={[
              styles.tabItem,
              { paddingBottom: Platform.OS === 'ios' ? insets.bottom : 0 }
            ]}
          >
            {options.tabBarIcon?.({ 
              focused: isFocused, 
              color: isFocused ? Colors.primary : Colors.textSecondary, 
              size: 24 
            })}
            <Text
              style={[
                styles.tabLabel,
                { color: isFocused ? Colors.primary : Colors.textSecondary },
              ]}
            >
              {options.title}
            </Text>
          </TouchableOpacity>
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  tabBar: {
    flexDirection: 'row',
    backgroundColor: Colors.background,
    borderTopWidth: 1,
    borderTopColor: Colors.border,
    position: 'relative',
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: -2 },
    shadowOpacity: 0.05,
    shadowRadius: 4,
    elevation: 4,
  },
  tabItem: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingTop: 12,
    paddingBottom: 12,
  },
  tabLabel: {
    fontSize: 12,
    marginTop: 4,
    fontWeight: '500',
  },
  indicator: {
    height: 3,
    borderRadius: 1.5,
    backgroundColor: Colors.primary,
    position: 'absolute',
    top: 0,
  },
});