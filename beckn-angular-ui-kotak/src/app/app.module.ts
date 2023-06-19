import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';


import 'hammerjs';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrModule } from 'ngx-toastr'; // For auth after login toast

import { CoreModule } from '@core/core.module';
import { CoreCommonModule } from '@core/common.module';
import { CoreSidebarModule, CoreThemeCustomizerModule } from '@core/components';

import { coreConfig } from 'app/app-config';

import { AppComponent } from 'app/app.component';
import { LayoutModule } from 'app/layout/layout.module';
import { LogsModule } from 'app/main/logs/logs.module';
import { OAuthModule } from 'angular-oauth2-oidc';
import {ServiceWorkerModule, SwRegistrationOptions} from '@angular/service-worker';
import { environment } from '../environments/environment';
 
const appRoutes: Routes = [
  {
    path: '',
    component: AppComponent,//Error 404 - Page not found
  },
  {
    path: 'pages',
    loadChildren: () => import('./main/pages/pages.module').then(m => m.PagesModule)
  },
 
  {
    path: '**',
    redirectTo: '/pages/miscellaneous/error' //Error 404 - Page not found
  }
];

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, {
      scrollPositionRestoration: 'enabled', // Add options right here
      relativeLinkResolution: 'legacy',
      useHash: false
    }),
    TranslateModule.forRoot(),

    
    //NgBootstrap
    NgbModule,
    ToastrModule.forRoot(),
    // Core modules
    CoreModule.forRoot(coreConfig),
    CoreCommonModule,
    CoreSidebarModule,
    CoreThemeCustomizerModule,
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: [environment.apiUrl],
        sendAccessToken: true
      }
    }),
    // App modules
    LayoutModule,
    LogsModule,
    // ServiceWorkerModule.register('ngsw-worker.js' ),
  ],
  // providers: [
  //   {
  //     provide: SwRegistrationOptions,
  //     useFactory: () => ({enabled: environment.production,scope:"./"}),
  //   },
  // ],
  bootstrap: [AppComponent]
})
export class AppModule {}
