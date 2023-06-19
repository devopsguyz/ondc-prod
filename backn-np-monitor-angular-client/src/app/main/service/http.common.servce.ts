import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";

// import { environment } from "environments/environment";
import { User } from "app/auth/models";
import { ResponseRet } from "../model/response.model";
import { catchError, map, retry } from "rxjs/operators";
import { Observable, of, throwError } from "rxjs";
import { AuthenticationService } from "app/auth/service";

@Injectable({ providedIn: "root" })
export class HttpCommonService {
  /**
   *
   * @param {HttpClient} _http
   */
  constructor(private auth : AuthenticationService, private _http: HttpClient) {
    console.log(auth);
  }

  handleError(err: HttpErrorResponse): Observable<ResponseRet<any>> {
  //  debugger
    console.log(err);
    let ret: ResponseRet<any> = { status: "error", message: [] };
    return of(ret);
  }

  handleEmptyError(err: HttpErrorResponse): Observable<any> {
    console.log(err);
    if(err.status==401){
      this.auth.logoff();
    }
    return of([]);
  }
  /**
   * Get all users
   */
  get(url: string): Observable<ResponseRet<any>> {
    return this._http
      .get<ResponseRet<any>>(url)
      .pipe(
        map((response: ResponseRet<any>) => response),
        catchError((err: HttpErrorResponse) => this.handleError(err))
      );
  }

  getWithoutError(url: string): Observable<any> {
    return this._http
      .get<any>(url)
      .pipe(
        map((response: ResponseRet<any>) => response.message),
        catchError((err: HttpErrorResponse) => this.handleEmptyError(err))
      );
  } 

  getDomainDetailsWithoutError(url: string): Observable<any> {
    return this._http
      .get<any>(url)
      .pipe(
        map((response: ResponseRet<any>) => response),
        catchError((err: HttpErrorResponse) => this.handleEmptyError(err))
      );
  } 
  postWithoutError(url: string,body : any): Observable<any> {
    return this._http
      .post<any>(url,body)
      .pipe(
        map((response: ResponseRet<any>) => {
          console.log(response);
          return response;
        })
      );
  } 
}
