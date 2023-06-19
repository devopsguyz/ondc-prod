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

@Component({
  selector: "app-transaction",
  templateUrl: "./transaction.component.html",
  styleUrls: ["./transaction.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class TransactionComponent implements OnInit {
  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    mode: "range",
    altFormat: "j-m-Y",
  };
  mapInput:Map<string, Array<Map<string, any>>>;
  type:string;
  // onGetRowClass(row): string {
  //   if(row.flag){
  //     return "color-transaction-row-red";
  //   }else{
  //     return "color-transaction-row-green";
  //   }

  // }
  public selectBasic = [{ id: 1, name: "abc" }];
  public selectBasicLoading = false;

  public DefaultDateOptions: FlatpickrOptions = {
    defaultDate: "2019-03-19",
    altInput: true,
  };
  _DefaultDateSnippetCode = {};
  form: FormGroup;
  DAY = 86400000;

  Constants: any;

  selectIds = [];
  tempData = [];
  searchFlag = false;
  isTypeSearch: boolean = false;

  map: Map<string, Array<Map<string, any>>>;
  public ColumnMode = ColumnMode;
  error: boolean;
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
    this.Constants = Constants;
    this._coreTranslationService.translate(en, fr, de, pt);
    // this.fromDate = calendar.getToday();
    // this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
    this.form = formBuilder.group({
      transactionId: "",
      date: "",
      selectTranscationId: "",
      typeValue: "search",
      serverValue: "",
    });
  }
 
  searchByActionAndTansationId(type, value, server) {
    this.searchFlag = true; 
    this.apiAuditLogsService
      .getByActionAndTansationId(type, value, server)
      .subscribe((item) => {
         this.mapInput=item;
        this.type=type;
        this.searchFlag = false;
      });
   
  }
 
  searchTransaction() {
    let value = this.form.controls.transactionId.value;
    let type = this.form.controls.typeValue.value;
    let server = 'api_audit'//this.form.controls.serverValue.value;
     
    if (!value || value.trim() == "") {
      this.toastr.error("Please enter Transaction Id.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else { 
      this.searchByActionAndTansationId(type, value, server);
    }
  }

  /**
   * On init
   */
  ngOnInit() {
    this.selectBasicLoading = true;
  }
}
function str(str: any) {
  throw new Error("Function not implemented.");
}

function forrowsBuyerToGateway() {
  throw new Error("Function not implemented.");
}

// function getRowClass(row: any) {
//   throw new Error("Function not implemented.");
// }

function row(row: any) {
  throw new Error("Function not implemented.");
}
