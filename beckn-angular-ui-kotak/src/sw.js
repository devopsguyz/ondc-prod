//const { console } = require("console");

const VERSION = 'v3.0';


//log('Installing Service Worker');
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


// self.addEventListener('fetch', event => event.respondWith(cacheThenNetwork(event)));



// async function cacheThenNetwork(event) {

//     log('Intercepting request: ' +VERSION +' : '+ event.request.url);

//     const cache = await caches.open(getCacheName());

//     const cachedResponse = await cache.match(event.request);

//     if (cachedResponse) {
//         log('From Cache: ' + event.request.url);
     

//         return cachedResponse;
//     }

//     const networkResponse = await fetch(event.request);

//     log('Calling network: ' + event.request.url);

//     return networkResponse;


// }





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

















