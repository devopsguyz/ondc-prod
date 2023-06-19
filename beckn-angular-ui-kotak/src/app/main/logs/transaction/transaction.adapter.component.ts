import { Component, Input, OnInit, ViewChild, ViewEncapsulation } from "@angular/core";
 
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
  selector: "app-transaction-adapter",
  templateUrl: "./transaction.adapter.component.html",
  styleUrls: ["./transaction.adapter.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class TransactionAdapterComponent implements OnInit {

  @Input()
  mapInput:Map<string, Array<Map<string, any>>>;

  @Input()
  type:string;

  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    mode: "range",
    altFormat: "j-m-Y",
  };

  onGetRowClass = (row) => {
    if (row.flag) {
      return "color-transaction-row-red";
    } else {
      return "color-transaction-row-green";
    }
  };

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
  @ViewChild("tableRowDetails_buyer_to_Gateway")
  tableRowDetails_buyer_to_Gateway: any;
  //rows=[];//

  @ViewChild("tableRowDetails_Gateway_to_Seller")
  tableRowDetails_Gateway_to_Seller: any;

  @ViewChild("tableRowDetails_SellerToGateway")
  tableRowDetails_SellerToGateway: any;

  @ViewChild("tableRowDetails_GatewayToBuyer")
  tableRowDetails_GatewayToBuyer: any;

  @ViewChild("tableRowDetails_AdapterToGateway")
  tableRowDetails_AdapterToGateway: any;

  @ViewChild("tableRowDetails_GatewayToAdapter")
  tableRowDetails_GatewayToAdapter: any;


  Constants: any;
  rowsBuyerToGateway = [];
  rowsGatewayToSeller = [];
  rowsSellerToGateway = [];
  rowsAdapterToGateway = [];
  rowsGatewayToAdapter = [];
  rowsrowsGatewayToBuyer = [];


  selectIds = [];
  tempData = [];
  searchFlag = false;
  isTypeSearch:boolean = false;

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
    
  }

  rowDetailsToggleExpand(row, table) {
    table.rowDetail.toggleExpandRow(row);
  }

  filterUpdate(event, key) {
    const val = event.target.value.toLowerCase();

    let tempData: Array<Map<string, any>> = this.map[key];

    // filter our data
    const temp = tempData.filter((d: any) => {
      let flag = !val;
      //console.log(d.toString());
      if (!flag) {
        Object.keys(d).forEach((item) => {
          try {
            flag = flag || d[item].toLowerCase().indexOf(val) !== -1;
          } catch (error) {}
        });
      }
      return flag;
    });

    // update the rows
    if (key == Constants.Buyer_to_Gateway) {
      this.rowsBuyerToGateway = temp;
      this.tableRowDetails_buyer_to_Gateway.offset = 0;
    } else if (key == Constants.Gateway_to_Seller) {
      this.rowsGatewayToSeller = temp;
      this.tableRowDetails_Gateway_to_Seller.offset = 0;
    } else if (key == Constants.Seller_to_Gateway) {
      this.rowsSellerToGateway = temp;
      this.tableRowDetails_SellerToGateway.offset = 0;
    } else if (key == Constants.Seller_to_Gateway) {
      this.rowsAdapterToGateway = temp;
      this.tableRowDetails_SellerToGateway.offset = 0;
    } else if (key == Constants.Gateway_to_Adapter) {
      this.rowsGatewayToAdapter = temp;
      this.tableRowDetails_SellerToGateway.offset = 0;
    }  
    // else if (key == Constants.Gateway_to_Buyer) {
    //   this.rowsrowsGatewayToBuyer = temp;
    //   this.tableRowDetails_GatewayToBuyer.offset = 0;
    // }
    // Whenever the filter changes, always go back to the first page
  }

  initRows() {
    this.rowsBuyerToGateway = [];
    this.rowsGatewayToSeller = [];
    this.rowsSellerToGateway = [];
    this.rowsAdapterToGateway = [];
    this.rowsGatewayToAdapter = [];
    //this.rowsrowsGatewayToBuyer = [];
  }

  convertJSONOBJ(item) {
    try {
      if (item.json) {
        var json = JSON.parse(item.json);
        item.json = JSON.stringify(json, null, 4);
  
      }
    } catch (error) {}
  }
  loadRows(item) {
    this.map = item;
    let rowsBuyerToGateway = this.map[Constants.Buyer_to_Gateway];
    let rowsGatewayToSeller = this.map[Constants.Gateway_to_Seller];
    let rowsSellerToGateway = this.map[Constants.Seller_to_Gateway];
    let rowsAdapterToGateway = this.map[Constants.Adapter_to_Gateway];
    let rowsGatewayToAdapter = this.map[Constants.Gateway_to_Adapter];
   // let rowsrowsGatewayToBuyer = this.map[Constants.Gateway_to_Buyer];
   if(!rowsBuyerToGateway){
     this.initRows();
     this.toastr.error("Server Error.", "Error!", {
      toastClass: "toast ngx-toastr",
      closeButton: true,
      progressBar: true,
    });
   }else{
  
    

    if (rowsBuyerToGateway.length > 0) {
      rowsBuyerToGateway.forEach((item) => {
        this.convertJSONOBJ(item);
        this.rowsBuyerToGateway.push(item);
      });
    }
    if (rowsGatewayToSeller.length > 0) {
      rowsGatewayToSeller.forEach((item) => {
        this.convertJSONOBJ(item);
        this.rowsGatewayToSeller.push(item);
      });
    }
    if (rowsSellerToGateway.length > 0) {
      rowsSellerToGateway.forEach((item) => {
        this.convertJSONOBJ(item);
        this.rowsSellerToGateway.push(item);
      });
    }

    // if (rowsrowsGatewayToBuyer.length > 0) {
    //   rowsrowsGatewayToBuyer.forEach((item) => {
    //     this.rowsrowsGatewayToBuyer.push(item);
    //   });
    // }
    if (rowsAdapterToGateway.length > 0) {
      rowsAdapterToGateway.forEach((item) => {
        this.convertJSONOBJ(item);
        this.rowsAdapterToGateway.push(item);
      });
    }
    if (rowsGatewayToAdapter.length > 0) {
      rowsGatewayToAdapter.forEach((item) => {
        this.convertJSONOBJ(item);
        this.rowsGatewayToAdapter.push(item);
      });
    }


    this.rowsBuyerToGateway=[...this.rowsBuyerToGateway];
    this.rowsGatewayToSeller=[...this.rowsGatewayToSeller ];
    this.rowsSellerToGateway=[...this.rowsSellerToGateway ];
    this.rowsAdapterToGateway=[...this.rowsAdapterToGateway ];
    this.rowsGatewayToAdapter=[...this.rowsGatewayToAdapter ];
   // this.rowsrowsGatewayToBuyer=[...this.rowsrowsGatewayToBuyer,...rowsrowsGatewayToBuyer];
    let val =
      this.rowsBuyerToGateway.length +
      this.rowsGatewayToSeller.length +
      this.rowsSellerToGateway.length +
      this.rowsAdapterToGateway.length +
      this.rowsGatewayToAdapter.length ;

     // + this.rowsrowsGatewayToBuyer.length;
    if (val == 0) {
      this.toastr.error("Transaction Id is not found.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    }
    return rowsBuyerToGateway.length +
    rowsGatewayToSeller.length +
    rowsSellerToGateway.length +
    rowsAdapterToGateway.length +
    rowsGatewayToAdapter.length;
    //+ rowsrowsGatewayToBuyer.length;
  }
   return 0;
  }
  

   

  /**
   * On init
   */
  ngOnInit() {
    this.selectBasicLoading = true;
    
    if(this.type == "search"){
      this.isTypeSearch = true;
    }else{
      this.isTypeSearch = false;
    }

    this.searchFlag = true;
    this.initRows();

 //   this.apiAuditLogsService.getByActionAndTansationId(type,value).subscribe((item) => {
      this.loadRows(this.mapInput);

      this.searchFlag = false;
   // });
    
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
