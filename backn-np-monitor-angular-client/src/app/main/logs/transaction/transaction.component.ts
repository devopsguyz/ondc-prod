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
import { AuthenticationService } from "app/auth/service";

@Component({
  selector: "app-transaction",
  templateUrl: "./transaction.component.html",
  styleUrls: ["./transaction.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class TransactionComponent implements OnInit {
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

  // onGetRowClass(row): string {
  //   if(row.flag){
  //     return "color-transaction-row-red";
  //   }else{
  //     return "color-transaction-row-green";
  //   }

  // }
  public selectBasic = [{ id: 1, name: "abc" }];
  public selectBasicLoading = false;
  pagingFlag=false;
  public DefaultDateOptions: FlatpickrOptions = {
    defaultDate: "2019-03-19",
    altInput: true,
  };
  _DefaultDateSnippetCode = {};
  form: FormGroup;
  DAY = 86400000;
  @ViewChild("tableRowDetails_buyer_to_Gateway")
  tableRowDetails_buyer_to_Gateway: any;
  //rows=[];

  @ViewChild("tableRowDetails_Gateway_to_Seller")
  tableRowDetails_Gateway_to_Seller: any;

  @ViewChild("tableRowDetails_SellerToGateway")
  tableRowDetails_SellerToGateway: any;
  @ViewChild("tableRowDetails_GatewayToBuyer")
  tableRowDetails_GatewayToBuyer: any;

  Constants: any;
  rowsBuyerToGateway = [];
  rowsGatewayToSeller = [];
  rowsSellerToGateway = [];
  rowsrowsGatewayToBuyer = [];

  selectIds = [];
  tempData = [];
  searchFlag = false;

  map: Map<string, Array<Map<string, any>>>;
  public ColumnMode = ColumnMode;
  error: boolean;

  public page = 4;
  public pageSize = 5;
  records = 0;
  end;
  start;
  refreshFlag=true;
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
    private toastr: ToastrService,
    private _authenticationService :AuthenticationService
  ) {
    this.Constants = Constants;
    this._coreTranslationService.translate(en, fr, de, pt);
    // this.fromDate = calendar.getToday();
    // this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
    this.form = formBuilder.group({
      transactionId: "",
      date: "",
      selectTranscationId: "",
    });
  
  }

  rowDetailsToggleExpand(row, table) {
    if (!row.readFlag) {
      row["readFlag"] = false;
      this.apiAuditLogsService
        .getAllTransactionDtl(row["json"], row["error"])
        .subscribe((item) => {
          if (item && item.length == 2) {
            row["json"] = this.convertJSONOBJ(item[0]);
            row["error"] = item[1];
          }
          row["readFlag"] = true;
          table.rowDetail.toggleExpandRow(row);
        });
    } else {
      table.rowDetail.toggleExpandRow(row);
    }
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
    } else if (key == Constants.Gateway_to_Buyer) {
      this.rowsrowsGatewayToBuyer = temp;
      this.tableRowDetails_GatewayToBuyer.offset = 0;
    }
    // Whenever the filter changes, always go back to the first page
  }

  initRows() {
    this.rowsBuyerToGateway = [];
    this.rowsGatewayToSeller = [];
    this.rowsSellerToGateway = [];
    this.rowsrowsGatewayToBuyer = [];
  }
  convertJSONOBJ(json) {
    try {
      if (json) {
        json = JSON.parse(json);
        json = JSON.stringify(json, null, 4);
      }
    } catch (error) {}
    return json;
  }

  loadRows(item) {
    this.map = item;
    let rowsBuyerToGateway = this.map[Constants.Buyer_to_Gateway];
    let rowsGatewayToSeller = this.map[Constants.Gateway_to_Seller];
    let rowsSellerToGateway = this.map[Constants.Seller_to_Gateway];
    let rowsrowsGatewayToBuyer = this.map[Constants.Gateway_to_Buyer];
    if (!rowsBuyerToGateway) {
      this.initRows();
      this.toastr.error("Server Error.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else {
      this.rowsBuyerToGateway = [
        ...this.rowsBuyerToGateway,
        ...rowsBuyerToGateway,
      ];
      this.rowsGatewayToSeller = [
        ...this.rowsGatewayToSeller,
        ...rowsGatewayToSeller,
      ];
      this.rowsSellerToGateway = [
        ...this.rowsSellerToGateway,
        ...rowsSellerToGateway,
      ];
      this.rowsrowsGatewayToBuyer = [
        ...this.rowsrowsGatewayToBuyer,
        ...rowsrowsGatewayToBuyer,
      ];
      let val =
        this.rowsBuyerToGateway.length +
        this.rowsGatewayToSeller.length +
        this.rowsSellerToGateway.length +
        this.rowsrowsGatewayToBuyer.length;
      if (val == 0) {
        this.toastr.error("Transaction Id is not found.", "Error!", {
          toastClass: "toast ngx-toastr",
          closeButton: true,
          progressBar: true,
        });
      }
      return (
        rowsBuyerToGateway.length +
        rowsGatewayToSeller.length +
        rowsSellerToGateway.length +
        rowsrowsGatewayToBuyer.length
      );
    }
    return 0;
  }
  searchTransactionWithId(value) {
    this.searchFlag = true;
    this.initRows();

    this.apiAuditLogsService.getByTansationId(value).subscribe((item) => {
      this.loadRows(item);

      this.searchFlag = false;
    });
  }

  searchTransactionWithDateRange(start, end, page) {
    this.initRows();
    this.start = start;
    this.end = end;
    this.searchFlag = true;
    this.pagingFlag=false;
    this.page = page;
    this.apiAuditLogsService
      .getByAuditbyDate(start, end, page)
      .subscribe((item) => {
        // this.loadRows(item);
        this.records = item.records;
        this.searchFlag = false;
        this.pageSize = item.pageSize;
        this.pagingFlag=true;
        if (this.loadRows(item.map) == 0) console.log("Received");
      });
  }

  loadPage(page) {
    this.searchTransactionWithDateRange(this.start, this.end, page);
  }
  searchTransaction() {
    let value = this.form.controls.transactionId.value;
    if (!value || value.trim() == "") {
      this.toastr.error("Please enter Transaction Id.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else {
      this.pagingFlag=false;
      this.searchTransactionWithId(value);
    }
  }

  searchTransactionDateRange() {
    let date = this.form.controls.date.value;

    if (!date || date.length < 1) {
      this.toastr.error("Please select proper date range.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else {
      console.log(this.apiAuditLogsService.getFormatDate(date[0]));
      //  console.log(this.apiAuditLogsService.getFormatDate(date[1]));
      this.searchTransactionWithDateRange(
        this.apiAuditLogsService.getFormatDate(date[0]),
        this.apiAuditLogsService.getFormatDate(date[0]),
        1
      );
      //this.searchTransactionWithId(value);
    }
  }
  searchSelectTransaction() {
    let value = this.form.controls.selectTranscationId.value;
    if (!value || !value.id || value.id.trim() == "") {
      this.toastr.error("Please enter Transaction Id.", "Error!", {
        toastClass: "toast ngx-toastr",
        closeButton: true,
        progressBar: true,
      });
    } else {
      this.searchTransactionWithId(value.id);
    }
  }

  /**
   * On init
   */
  ngOnInit() {
    
this.refrshSelect();
    this._authenticationService.subject.subscribe(e=>{
      this. refrshSelect();
    });
  }
  refrshSelect(){
    this.selectBasicLoading = true;
    this.apiAuditLogsService.getAllTransaction().subscribe((item) => {
      this.selectIds = [];
      if (item) {
        item.forEach((id) => {
          this.selectIds.push({ id: id, name: id });
        });
      }
      this.selectBasicLoading = false;
    });
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
