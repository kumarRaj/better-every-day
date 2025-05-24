self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open('better-everyday-cache').then((cache) => {
      return cache.addAll([
        '/',
        '/index.html',
        '/assets/images/icon-192.png',
        '/assets/images/icon-512.png',
        // Add other assets you want to cache
      ]);
    })
  );
});

self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => {
      return response || fetch(event.request);
    })
  );
});