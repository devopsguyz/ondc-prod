import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "environments/environment";
import { Observable, of, throwError } from "rxjs";
import { DomainDto, ResponseRet } from "../model/response.model";
import { catchError, map, retry } from "rxjs/operators";
import { HttpCommonService } from "./http.common.servce";

@Injectable({
  providedIn: "root",
})
export class ApiOnboardingServiceService {
  /**
   *
   * @param {HttpClient} _http
   */
  constructor(private _http: HttpCommonService) {}

  /**
   * Get all domain
   */
  getAllDomain(): Observable<Array<any>> {
    return this._http.getDomainDetailsWithoutError(
      `${environment.apiRegistryUrl}` + `/api/domain/list`
    );
  }

  saveDomain(body: DomainDto): Observable<any> {
    return this._http.postWithoutError(
      `${environment.apiRegistryUrl}` + `/api/domain/save`,
      body
    );
  }
}
