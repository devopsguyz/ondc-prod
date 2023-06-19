import { Component, OnInit, ViewChild, ViewEncapsulation } from "@angular/core";

import { locale as en } from "../i18n/en";
import { locale as fr } from "../i18n/fr";
import { locale as de } from "../i18n/de";
import { locale as pt } from "../i18n/pt";

import { CoreTranslationService } from "@core/services/translation.service";
import {
  NgbCalendar,
  NgbDate,
  NgbDateParserFormatter,
} from "@ng-bootstrap/ng-bootstrap";
import { FlatpickrOptions } from "ng2-flatpickr";
import { FormBuilder, FormGroup } from "@angular/forms";
import { ColumnMode, id } from "@swimlane/ngx-datatable";
import { HttpCommonService } from "../../service/http.common.servce";
import { ApiAuditLogsService } from "../../service/api.audit.logs.servce";
import { Constants } from "../../utl/constants";
import { ToastrService } from "ngx-toastr";
import { reduce } from "rxjs/operators";
import { LogsResponse, ResponseRet } from "app/main/model/response.model";

@Component({
  selector: "app-logssubscriberid",
  templateUrl: "./logs_subscriber_id.component.html",
  styleUrls: ["./logs_subscriber_id.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class LogsSubscriberIdComponent implements OnInit {
  data: Array<any> = [];
  dataBackup: Array<any> = [];
  form: FormGroup;
  error: boolean;
  searchFlag = false;
  public page = 4;
  public pageSize = 5;
 
  /**
   *
   * @param {CoreTranslationService} _coreTranslationService
   */
  constructor(
    private _coreTranslationService: CoreTranslationService,
    private calendar: NgbCalendar,
    public formatter: NgbDateParserFormatter,
    private formBuilder: FormBuilder,
    private apiAuditLogsService: ApiAuditLogsService,
    private toastr: ToastrService
  ) {
    this._coreTranslationService.translate(en, fr, de, pt);
    // this.fromDate = calendar.getToday();
    // this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
    this.form = formBuilder.group({
      subscribeId: "",
    });
  }

  rowDetailsToggleExpand(row, table) {
    table.rowDetail.toggleExpandRow(row);
  }

  filterUpdate(event) {
    const val = event.target.value.toLowerCase();
    let tempData  = [...this.dataBackup];
    // filter our data
    const temp = tempData.filter((d: any) => {
       var flag=false;
        Object.keys(d).forEach((item) => {
          try {
            flag = flag || d[item].toLowerCase().indexOf(val) !== -1;
          } catch (error) {}
        });
      
      return flag;
    });
    this.data=temp;
    // this.rowsBuyerToGateway = temp;
    // this.tableRowDetails_buyer_to_Gateway.offset = 0;
    // Whenever the filter changes, always go back to the first page
  }

  convertJSONOBJ(item) {
    try {
      if (item) {
        item = JSON.stringify(item, null, 4);
        return item;
      }
    } catch (error) {}
    return "";
  }

  searchTransactionWithId(value) {
  //  debugger
    this.searchFlag = true;
    this.data = [];

    this.apiAuditLogsService
      .getLogsBySubsribeId(value)
      .subscribe((item: ResponseRet<Array<LogsResponse>>) => {
        this.data = [];
        if (item.message)
          item.message.forEach((item) => {
            this.data.push({
              createdOn: item.createdOn,
              request: this.convertJSONOBJ(item.request),
              response: this.convertJSONOBJ(item.response),
            });
          });
          this.dataBackup=[...this.data]
        this.searchFlag = false;
        if(this.data.length==0){
          this.toastr.error("Records is not found.", "Error!", {
            toastClass: "toast ngx-toastr",
            closeButton: true,
            progressBar: true,
          });
        }
      });
  }

  searchTransaction() {
    let value = this.form.controls.subscribeId.value;
    if (!value || value.trim() == "") {
      this.toastr.error("Please enter subscriber Id.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else {
      this.searchTransactionWithId(value);
    }
  }

  /**
   * On init
   */
  ngOnInit() {}
}
