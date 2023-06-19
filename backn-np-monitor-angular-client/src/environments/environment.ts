// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
 
export const environment = {
  uiRole:"logs", //domain,logs
  applicationName: "Logs Monitoring",
  applicationDomainName: "Domain WhiteList",

  appTitle:"ONDC",
  hmr: false,
   apiUrl: 'http://localhost:8001/logs-monitoring-api', 
   apiAuditUrl: 'http://localhost:8001/logs-monitoring-api/audit', 
  apiRegistryUrl: 'http://localhost:8002/logs-monitoring-api/registry', 
  production:true,
  env: 'dev',  
  webApiRedirectURL:'http://localhost:4200/',
  urlOauth2:'http://75.119.158.145:8078/auth/realms/ONDC', 
  logoURL:'assets/images/logo/ondc.png', 
 oauth2clientToken:'k9Y0zRvy5ivCsYjc9c1mXLGMferM0ySg',
 scope: 'openid profile',
 clientId: 'ONDC_WEB',
  
  debug:true,
   

  
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
