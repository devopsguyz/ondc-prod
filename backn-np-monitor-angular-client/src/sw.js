 

const VERSION = 'v3.1';

 
console.log("Installing Service Worker")

self.addEventListener('install', event => event.waitUntil(installServiceWorker()));
 
async function installServiceWorker() {
    console.log("Service Worker installation started " + VERSION);

    const cache = await caches.open(getCacheName());

   
    try {
        cache.addAll([
            'main.js',
            'polyfills.js',
            'runtime.js',
            'styles.css'
        ]);

      
    } catch (error) {
        console.log(error)
    }
    self.skipWaiting();
}

self.addEventListener('activate', async  () => {

    console.log('Service Worker activated ' + VERSION);

    const cacheKeys = await caches.keys();

    cacheKeys.forEach(cacheKey => {

        console.log('Service Worker cacheKey ' + cacheKey);
        if (cacheKey !== getCacheName()) {
            console.log('Service Worker cacheKey delete ' + cacheKey);
            caches.delete(cacheKey);
        }
    });

    return clients.claim();
});
  
function getCacheName() {
    return "app-cache-" + VERSION;
}


function log(message, ...data) {
    if (data.length > 0) {
        console.log(VERSION, message, data);
    }
    else {
        console.log(message);
    }
}

















