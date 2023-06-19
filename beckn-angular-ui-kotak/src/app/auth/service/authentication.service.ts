import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import { map } from "rxjs/operators";

import { environment } from "environments/environment";
import { User, Role } from "app/auth/models";
import { ToastrService } from "ngx-toastr";
import { AuthConfig, JwksValidationHandler, OAuthService } from "angular-oauth2-oidc";
import { debug } from "console";
import { BreakPointRegistry } from "@angular/flex-layout";
import { Router } from "@angular/router";
@Injectable({ providedIn: "root" })
export class AuthenticationService {
  // public
  public currentUser: Observable<User>;
  public currentKeycloakUser: any;
  // private
  private currentUserSubject: BehaviorSubject<User>;
  public loginFlag = false;
  authConfig: AuthConfig = {
    issuer: environment.urlOauth2,
    redirectUri: environment.webApiRedirectURL,
    clientId: environment.clientId,
    scope: environment.scope,
    responseType: "code", 
    requireHttps: false,
    // at_hash is not present in id token in older versions of keycloak.
    // use the following property only if needed!
    // disableAtHashCheck: true,
    showDebugInformation: true,
  };

  /**
   *
   * @param {HttpClient} _http
   * @param {ToastrService} _toastrService
   */
  constructor(
    private _router: Router,
    private _toastrService: ToastrService,
    private oauthService: OAuthService
  ) {
    this.currentUserSubject = new BehaviorSubject<User>(
      JSON.parse(localStorage.getItem("currentUser"))
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  // getter: currentUserValue
  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  /**
   *  Confirms if user is admin
   */
  get isAdmin() {
    return (
      this.currentUser && this.currentUserSubject.value.role === Role.Admin
    );
  }

  /**
   *  Confirms if user is client
   */
  get isClient() {
    return (
      this.currentUser && this.currentUserSubject.value.role === Role.Client
    );
  }

  
  /**
   * User logout
   *
   */
  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem("currentUser");
    // notify

    this.logoff();
    this.currentUserSubject.next(null);
    this.loginFlag = false;

    // this.oauthService.loadDiscoveryDocumentAndTryLogin();
  }

  public loginKeyClock() {
    this.oauthService.initLoginFlow();
  }

  public logoff() {
    this.oauthService.revokeTokenAndLogout();
    //this.oauthService.logOut();
    window.sessionStorage.clear();
    setTimeout(()=>{
      this.configure();
    },1000)
    
  }

  public isLogin() {
    if (this.oauthService.getIdentityClaims()) {
      this.loginFlag = true;

      this.currentKeycloakUser = this.oauthService.getIdentityClaims();
      // console.log(item);

      //});
    } else {
      this.loginFlag = false;
    }
    return this.oauthService.getAccessToken() != undefined;
  }

  getUserNameShort() {
    return this.currentKeycloakUser &&
      this.currentKeycloakUser["preferred_username"]
      ? this.currentKeycloakUser["preferred_username"].substr(0, 1)
      : "";
  }
  public getUserName() {
    return this.currentKeycloakUser
      ? this.currentKeycloakUser["preferred_username"]
      : "";
  }

  loginRedirect(cnt) {
    this.loginFlag = this.isLogin();
    // debugger;
    console.log("wait for login: "+cnt);
    
    if(!window.sessionStorage.getItem("refresh_token")){
      this.oauthService.initLoginFlow();
      
    }
    if (!this.loginFlag && cnt == 10) {
      this.oauthService.initLoginFlow();
      return;
    }
    if (this.loginFlag) {
      return;
    } else {
      cnt++;
    }
    if (cnt <= 10) {
      setTimeout(() => {
        this.loginRedirect(cnt);
      }, 1000);
    }
  }

  public checkUserNew() {
   
        this.loginFlag = true;    
        
       
         this._router.navigate(['/transactions']);
   
  }
  
  public configure( ) {
   

    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
    this.oauthService.configure(this.authConfig);

    this.oauthService.loadDiscoveryDocumentAndTryLogin().then((response) => {
      this.oauthService.setupAutomaticSilentRefresh();
      if (
        this.oauthService.hasValidAccessToken() &&
        this.oauthService.hasValidIdToken()
      ) {
        this.checkUserNew();
      } else {
        if (window.sessionStorage.getItem('refresh_token')) {
          this.oauthService
            .refreshToken()
            .then((value) => {
              console.log(value);
              this.checkUserNew();
            })
            .catch((e) => {
              this.oauthService.initLoginFlow();
              return;
            });
          //this.router.navigate(['/login']);
        } else {
         this.oauthService.initLoginFlow();
          return;
        }
      }
      this.oauthService.events.subscribe((event) => {
        console.log(event);
      });

      //  this.loginRedirect(0);
    });

    // this.oauthService.initLoginFlow();
  }

  // public configure() {
  //   var cnt = 0;

  //   this.oauthService.configure(this.authConfig);
  //   this.oauthService.setupAutomaticSilentRefresh();

  //   this.oauthService.loadDiscoveryDocumentAndTryLogin().then((response) => {
  //      this.loginRedirect(0);
  //   });
  // }
}
