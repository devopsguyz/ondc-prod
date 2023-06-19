//const { Console } = require("console");


//, {    scope: './'}
if ('serviceWorker' in navigator) {
  console.log('Service worker registration ...');
          
  // navigator.serviceWorker.getRegistrations().then(function(registrations) { 
  //     for(let registration of registrations) { 
  //         Console.log("SW Un REg :"+registration)
  //         registration.unregister();
  //      } 

  // });  

  console.log('Service worker registration ...');
//  log()   
  navigator.serviceWorker.register('./sw.js', {    scope: './'})
  .then(registration => {

      console.log('Service worker registration completed');
      setInterval(()=>{
      registration.update()
      },1000*60*60);
  });
}