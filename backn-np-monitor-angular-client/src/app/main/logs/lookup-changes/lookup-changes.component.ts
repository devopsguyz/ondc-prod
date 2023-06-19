import { Component, OnInit,ViewChild, ViewEncapsulation } from '@angular/core';
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
import { LogsResponse, LookupResponse, ResponseRet } from "app/main/model/response.model";


@Component({
  selector: 'app-lookup-changes',
  templateUrl: './lookup-changes.component.html',
  styleUrls: ['./lookup-changes.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class LookupChangesComponent implements OnInit {
  public page1 = 4;
  data: Array<any> = [];
  dataBackup: Array<any> = [];
  _DefaultDateSnippetCode = {};
  form: FormGroup;
  error: boolean;
  searchFlag = false;
  public page = 2 ;
  public toppage = 1;
  public pageSize = 5;
  public Newpage = 1;
  Bottompage = 1;
  public toppageSize =4;
  selectIds = [];
  tempData = []; 
  records = 0;
  inputPage;
  end;
  start;
  lookupTypevalue:string
  map: Map<string, Array<Map<string, any>>>; 
  public ColumnMode = ColumnMode; 
  public selectBasic = [{ id: 1, name: "abc" }];
  public selectBasicLoading = false;
  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    mode: "single",
    altFormat: "j-m-Y",
  };

  onGetRowClass = (row) => {
    if (row.flag) {
      return "color-transaction-row-red";
    } else {
      return "color-transaction-row-green";
    }
  };

  pagingFlag=false;
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
    private toastr: ToastrService) {
    //this.Constants = Constants;
    this._coreTranslationService.translate(en, fr, de, pt);
    // this.fromDate = calendar.getToday();
    // this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
    this.form = formBuilder.group({ 
      date: "",
      selectLookupType: "",
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
   this.apiAuditLogsService.getLookupChangesByType().subscribe((item) =>{
    this.selectIds = [];
    if (item) {
      item.forEach((name) => {
        this.selectIds.push(name);
      });
    }
    this.selectBasicLoading = false;
   });  
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
  searchLookupsWithDateAndType(lookupTypevalue:string, start:string, end:string, page:any) {
    this.searchFlag = true;
    this.data = [];
    this.dataBackup=[];
    this.pagingFlag=false;
    this.apiAuditLogsService
      .getLookupChangesByTypeAndDate(lookupTypevalue, start, end,page)
      .subscribe((item) => {
        this.data = [];
        if (item) {
          this.records = item.message.records;
          this.searchFlag = false;
          this.toppage = page+1;
          this.toppageSize = item.message.pageSize;
          this.pagingFlag=true;
          // if (this.loadRows(item.map) == 0) console.log("Received");
          item.message.map.lookup_changes_data.forEach((id) => {
            let rec={  
              createdOn: id.createdOn,
              request: this.convertJSONOBJ(id.request),
              response: this.convertJSONOBJ(id.response),    
            };
            this.data.push(rec);
            this.dataBackup.push(rec);
          });
        }
        if(this.data.length==0){
          this.toastr.error("Records is not found.", "Error!", {
            toastClass: "toast ngx-toastr",
            closeButton: true,
            progressBar: true,
          });
        }
        this.searchFlag = false;
      });
  }

  loadPage(page) {
    console.log(page);
    this.searchLookupsWithDateAndType(this.lookupTypevalue, this.start, this.end, (page-1));
  }

  searchLookups() {
    this.lookupTypevalue = this.form.controls.selectLookupType.value;
    let date = this.form.controls.date.value;
    let isValidation:boolean = true;
   
    if (!date || date.length < 1) {
      this.toastr.error("Please select proper date.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,       
     });
     isValidation = false;
   }

   if (!this.lookupTypevalue || this.lookupTypevalue.trim() == "") {
      this.toastr.error("Please select lookup type.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
      isValidation = false;
    }   
    if(isValidation){
      this.start = this.apiAuditLogsService.getFormatDate(date[0]);
      this.end =  this.apiAuditLogsService.getFormatDate(date[0]);
      this.searchLookupsWithDateAndType(this.lookupTypevalue, this.start,this.end, 0);
    }
    
    
  }

}
