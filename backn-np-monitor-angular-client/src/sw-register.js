 
if ('serviceWorker' in navigator) {
    console.log('Service worker registration ...');
            
   
    console.log('Service worker registration ...');
  
    navigator.serviceWorker.register('./sw.js', {    scope: './'})
    .then(registration => {

        console.log('Service worker registration completed');
        setInterval(()=>{
        registration.update()
        },1000*60*60);
    });
}