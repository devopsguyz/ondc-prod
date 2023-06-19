
import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { CoreTranslationService } from "@core/services/translation.service";
import {
  NgbCalendar,
  NgbDateParserFormatter,
} from "@ng-bootstrap/ng-bootstrap";
import { ApiAuditLogsService } from "app/main/service/api.audit.logs.servce";
import { Constants } from "app/main/utl/constants";
import { FlatpickrOptions } from "ng2-flatpickr";
import { ToastrService } from "ngx-toastr";
import { ViewEncapsulation } from "@angular/core";
import { ColumnMode } from "@swimlane/ngx-datatable";
import { HttpClientModule } from "@angular/common/http";
import { FileRespons } from "../../model/response.model";
import { locale as en } from "../i18n/en";
import { locale as fr } from "../i18n/fr";
import { locale as de } from "../i18n/de";
import { locale as pt } from "../i18n/pt";
@Component({
  selector: "app-summary-report",
  templateUrl: "./summary-report.component.html",
  styleUrls: ["./summary-report.component.scss"],
})
export class SummaryReportComponent implements OnInit {

  //dirName = '140920221254-2f77b37b-494d-4227-8852-3443922dc5f6';
  dirName ;//= '1663575866553-d23a7863-fcd7-4a83-8b08-d968f9d0ad99';
  rowsSummeryReports = [];
  rows  = [];
  viewMsg=false;
 

  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    mode: "single",
    altFormat: "j-m-Y",
  };
  _DefaultDateSnippetCode = {};
  form: FormGroup;
  DAY = 86400000;
  searchFlag = false;
  map: Map<string, Array<Map<string, any>>>;
  public ColumnMode = ColumnMode;

  /**
   *
   * @param {CoreTranslationService} _coreTranslationService
   */
  constructor(
    private _coreTranslationService: CoreTranslationService,
    private calendar: NgbCalendar,
    public formatter: NgbDateParserFormatter,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private apiAuditLogsService: ApiAuditLogsService
  ) {
    this._coreTranslationService.translate(en, fr, de, pt);
    this.form = formBuilder.group({
      date: "",
    });
  }

  viewSummeryReport(dirName) {
    this.searchFlag = false;
    this.rowsSummeryReports=[];
    this.apiAuditLogsService
      .viewSummeryReportByDirName(dirName)
      .subscribe((response) => {
        this.searchFlag = false;
        response[this.dirName].map(x => this.rowsSummeryReports.push({ 'FileName' :x.toString()}));
        this.rows = this.rowsSummeryReports;
      });  
  }

  downloadSummeryReport(FileName) {
    this.apiAuditLogsService
      .downloadSummeryReportByFileName(this.dirName,FileName)
      .subscribe((item: FileRespons) => {
        this.searchFlag = false;
        this.apiAuditLogsService.downloadFile(item);
      });
  }

  searchTransactionDateRange() {
    let date = this.form.controls.date.value;
    this.rows =[];
    if (!date || date.length < 1) {
      this.toastr.error("Please select proper date.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else {
      this.searchFlag = true;
      console.log(this.apiAuditLogsService.getFormatDate(date[0]));
      // console.log(this.apiAuditLogsService.getFormatDate(date[1]));
      this.apiAuditLogsService
        .downloadFileSerivce(
          this.apiAuditLogsService.getFormatDate(date[0]),
          this.apiAuditLogsService.getFormatDate(date[0])
        )
        .subscribe((item) => {
          //item = 'file is under process.....-1663575866553-d23a7863-fcd7-4a83-8b08-d968f9d0ad99';
          this.viewMsg=true;
          this.dirName=item.substr(27);
          this.searchFlag = false;
          this.viewSummeryReport(this.dirName);
          //this.apiAuditLogsService.downloadFile(item);
        });
      //this.searchTransactionWithId(value);
    }
  }

  ngOnInit(): void {
    
  }

  
}
