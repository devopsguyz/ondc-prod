import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

import { environment } from "environments/environment";
import { User } from "app/auth/models";
import { HttpCommonService } from "./http.common.servce";
import { BuyerSellerResponse, FileRespons, Graph, LogsResponse, LookupResponse, ResponseRet, TransactionMapResponse } from "../model/response.model";
import { Observable } from "rxjs";
import { AuthenticationService } from "app/auth/service";

@Injectable({ providedIn: "root" })
export class ApiAuditLogsService {
  /**
   *
   * @param {HttpClient} _http
   */
  constructor(private _http: HttpCommonService,private auth:AuthenticationService) { }

  /**
   * Get all users
   */
  getAllTransaction(): Observable<Array<string>> {
    let url = `${environment.apiAuditUrl}` + `/api/all/transaction?db=`+this.auth.selectDbType;
    return this._http.getWithoutError(url);
  }

  getAllTransactionDtl(jsonId, errorId): Observable<Array<string>> {
    let url = `${environment.apiAuditUrl}` + `/api/transaction/get/${jsonId}/${errorId}?db=`+this.auth.selectDbType;
    return this._http.getWithoutError(url);
  }

  /**
   * Get user by id
   */
  getByTansationId(
    id: string
  ): Observable<Map<string, Array<Map<string, any>>>> {
    let url = `${environment.apiAuditUrl}` + `/api/get/transaction/${id}?db=`+this.auth.selectDbType;
    return this._http.getWithoutError(url);
  }


  getLogsBySubsribeId(
    id: string
  ): Observable<ResponseRet<Array<LogsResponse>>> {
    let url = `${environment.apiRegistryUrl}` + `/api/dashboard/logs?db=`+this.auth.selectDbType;
    return this._http.postWithoutError(url, { subsciberId: id });
  }
  getLookupChangesByTypeAndDate(type: string, start:string, end:string, page){
      let url = `${environment.apiRegistryUrl}` + `/api/dashboard/lookup/auto/${start}/${end}/${page}?db=`+this.auth.selectDbType;
      return this._http.postWithoutError(url,{'type':type}); 
    }
  getLookupChangesByType():Observable<Array<string>>{
    //debugger
    let url = `${environment.apiRegistryUrl}` + `/api/lookup/type`;
    return this._http.getWithoutError(url); 
  }
  

  getByAuditbyDate(
    start: string, end: string, page: number
  ): Observable<TransactionMapResponse> {
    let url = `${environment.apiAuditUrl}` + `/api/get/transaction/by/date/${start}/${end}/${page}?db=`+this.auth.selectDbType;
    return this._http.getWithoutError(url);
  }

  getTarnasactionDtl() {

  }

  getDashboardBytype(api: string,
    type: string, start: string, end: string, select: string
  ): Observable<Array<Graph<number>>> {
    if (api == 'transaction')
      return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/${select}/${type}/${start}/${end}?db=`+this.auth.selectDbType);
    else if (api == 'lookup')
      return this._http.getWithoutError(`${environment.apiRegistryUrl}` + `/api/dashboard/lookup/${select}/${type}/${start}/${end}?db=`+this.auth.selectDbType);
    else if (api == 'seller')
      return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/seller/${select}/${type}/${start}/${end}?db=`+this.auth.selectDbType);
    else if (api == 'buyer')
      return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/buyer/${select}/${type}/${start}/${end}?db=`+this.auth.selectDbType);

  }

  getDashboardSellerBuyerBytype(api: string, start: string, end: string
  ): Observable<Array<BuyerSellerResponse>> {
    if (api == 'seller')
      return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/grid/seller/${start}/${end}?db=`+this.auth.selectDbType);
    else if (api == 'buyer')
      return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/grid/buyer/${start}/${end}?db=`+this.auth.selectDbType);


  }
  getFormatDate(date): string {
    let month = date.getMonth() + 1;
    let dateStr = (
      (date.getDate() < 10 ? "0" + date.getDate() : "" + date.getDate()) +
      "-" +
      (month < 10 ? "0" + month : "" + month) +
      "-" +
      date.getFullYear()

    );
    return dateStr;
  }

  // downloadFileSerivce(start: string, end: string): Observable<FileRespons> {
  //   return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/audit/dashboard/summary/report/${start}/${end}`);
  // }
  downloadFileSerivce(start: string, end: string) {
    return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/summary/report/${start}/${end}?db=`+this.auth.selectDbType);
  }

  viewSummeryReportByDirName(dirName: string) {
    return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/summary/report/${dirName}?db=`+this.auth.selectDbType);
  }

  downloadSummeryReportByFileName(dirName: string, fileName: string): Observable<FileRespons> {
    return this._http.getWithoutError(`${environment.apiAuditUrl}` + `/api/dashboard/summary/report/downloads/${dirName}/${fileName}?db=`+this.auth.selectDbType);
  }


  downloadFile(file: FileRespons) {

    let c = this.base64ToArrayBuffer(file.blob);
    var blobdata = new Blob([c], {
      type: "application/octet-stream"
    });
    var downloadUrl = URL.createObjectURL(blobdata);
    var a = document.createElement("a");
    a.href = downloadUrl;
    a.download = file.fileName;
    a.click();

  }
  base64ToArrayBuffer(base64: string) {

    var binary_string = window.atob(base64);
    var len = binary_string.length;
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
      bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes.buffer;
  }
  getByAuditbyLogs(
    date: any
  ): Observable<Array<Array<String>>> {
    let url = `${environment.apiRegistryUrl}` + `/api/dashboard/performance/${date}`;
    return this._http.getWithoutError(url);
  }

  getBuyerSellerCount(type: string, start:string, end:string){
    let url = `${environment.apiAuditUrl}` + `/api/${type}/grid/${start}/${end}?db=`+this.auth.selectDbType;
    return this._http.getWithoutError(url); 
  }
}
