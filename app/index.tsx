import { Redirect } from 'expo-router';

export default function Index() {
  return <Redirect href="/(tabs)" />;
}

if (typeof window !== 'undefined' && 'serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('./service-worker.js').then(
      (registration) => {
        console.log('Service Worker registered with scope:', registration.scope);
      },
      (error) => {
        console.error('Service Worker registration failed:', error);
      }
    );
  });
}