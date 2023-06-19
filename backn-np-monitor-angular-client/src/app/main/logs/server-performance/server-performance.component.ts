import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CoreTranslationService } from '@core/services/translation.service';
import { NgbCalendar, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { ResponseRet, LogsResponse } from 'app/main/model/response.model';
import { ApiAuditLogsService } from 'app/main/service/api.audit.logs.servce';
import { FlatpickrOptions } from 'ng2-flatpickr';
import { ToastrService } from 'ngx-toastr';
import { locale as en } from "../i18n/en";
import { locale as fr } from "../i18n/fr";
import { locale as de } from "../i18n/de";
import { locale as pt } from "../i18n/pt";
@Component({
  selector: 'app-server-performance',
  templateUrl: './server-performance.component.html',
  styleUrls: ['./server-performance.component.scss']
})
export class ServerPerformanceComponent implements OnInit {

  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    mode: "single",
    altFormat: "j-m-Y",
  };
  data: Array<any> = [];
  dataBackup: Array<any> = [];
  form: FormGroup;
  error: boolean;
  searchFlag = false;
  logsByDate = false;
  logsById = false;
  public page = 4;
  public pageSize = 5;
  responsePerformance =[];

  dataTablecolumns = [
    { prop: 'time', name :"Time" },
    { prop: 'avgResponseTime',name: 'Response' },
    { prop: 'countRecords',name: 'No. of Counts' }
  ];
  
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
      date: "",
    });
  }

  rowDetailsToggleExpand(row, table) {
    table.rowDetail.toggleExpandRow(row);
  }

  filterUpdate(event) {
    const val = event.target.value.toLowerCase();
    let tempData = [...this.dataBackup];
    // filter our data
    const temp = tempData.filter((d: any) => {
      var flag = false;
      Object.keys(d).forEach((item) => {
        try {
          flag = flag || d[item].toLowerCase().indexOf(val) !== -1;
        } catch (error) { }
      });

      return flag;
    });
    this.data = temp;
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
    } catch (error) { }
    return "";
  }

  searchTransactionWithId(value) {
    debugger
    this.searchFlag = true;
    this.logsByDate = false;
    this.logsById = true;
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
        this.dataBackup = [...this.data]
        this.searchFlag = false;
      });
  }

  searchTransaction() {
    let value = this.form.controls.subscribeId.value;
    if (!value || value.trim() == "") {
      this.toastr.error("Please enter Transaction Id.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else {
      this.searchTransactionWithId(value);
    }
  }

  searchLogsDateRange(): void {
    let date = this.form.controls.date.value;
    this.searchFlag = true;
    this.logsByDate = true;
    this.logsById = false;
    this.apiAuditLogsService
      .getByAuditbyLogs(this.apiAuditLogsService.getFormatDate(date[0]))
      .subscribe((res) => {
        let response = res
        this.responsePerformance=response;
      });
    this.searchFlag = false;
  }
  
  ngOnInit() {
    
  }



}//main class
