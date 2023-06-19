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
import {
  LogsResponse,
  LookupResponse,
  ResponseRet,
} from "app/main/model/response.model";
import { APP_BASE_HREF } from "@angular/common";
import { type } from "os";

@Component({
  selector: "app-seller-buyer",
  templateUrl: "./seller-buyer.component.html",
  styleUrls: ["./seller-buyer.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class SellerBuyerComponent implements OnInit {
  data: Array<any> = [];
  dataBackup: Array<any> = [];
  end;
  start;


  public ColumnMode = ColumnMode;
  public selectBasic = [{ id: 1, name: "abc" }];
  public selectBasicLoading = false;
  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    altFormat: "j-m-Y",
    mode:'range'
  };

  form: FormGroup;
  searchFlag;
  type;
  typeSellerFlag;
  pagingFlag = false;
  public DefaultDateOptions: FlatpickrOptions = {
    defaultDate: "2019-03-19",
    altInput: true,
  };

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
    //this.Constants = Constants;
    this._coreTranslationService.translate(en, fr, de, pt);
    // this.fromDate = calendar.getToday();
    // this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
    this.form = formBuilder.group({
      date: "",
    });
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

  ngOnInit(): void {
    //load look type drop down list
    if (location.href.indexOf("buyer") != -1) {
      this.type = "buyer";
      this.typeSellerFlag=false;
    } else {
     this.typeSellerFlag=true;
      this.type = "seller";
    }
  }

  filterUpdate(event) {
    const val = event.target.value.toLowerCase();
    let tempData = [...this.dataBackup];
    // filter our data
    const temp = tempData.filter((d: any) => {
      var flag = false;
      Object.keys(d).forEach((item) => {
        try {
          flag = flag || d[item].toString().toLowerCase().indexOf(val) !== -1;
        } catch (error) {
          console.log(error)
        }
      });

      return flag;
    });
    this.data = temp;
    // this.rowsBuyerToGateway = temp;
    // this.tableRowDetails_buyer_to_Gateway.offset = 0;
    // Whenever the filter changes, always go back to the first page
  }
  searchWithDateAndType(    
    start: string,
    end: string
  ) {
    this.searchFlag = true;
    this.data = [];
    this.dataBackup = [];
    this.pagingFlag = false;
    this.apiAuditLogsService
      .getBuyerSellerCount(this.type, start, end)
      .subscribe((item) => {
        this.data = [];
        if (item) {
          this.data = item;
          this.dataBackup= item;
          this.searchFlag = false;
         
        }
        if (this.data.length == 0) {
          this.toastr.error("Records is not found.", "Error!", {
            toastClass: "toast ngx-toastr",
            closeButton: true,
            progressBar: true,
          });
        }
        this.searchFlag = false;
      });
  }
  
  searchAction() {
     let date = this.form.controls.date.value;
    let isValidation:boolean = true;
   
    if (!date || date.length < 2) {
      this.toastr.error("Please select proper date.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,       
     });
     isValidation = false;
   }
 
    if(isValidation){
      this.start = this.apiAuditLogsService.getFormatDate(date[0]);
      this.end =  this.apiAuditLogsService.getFormatDate(date[1]);
      this.searchWithDateAndType(this.start,this.end);
    }
    
    
  }
}
