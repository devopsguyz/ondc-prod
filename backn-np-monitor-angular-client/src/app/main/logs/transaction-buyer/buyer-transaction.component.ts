import { Component, OnInit, ViewChild } from "@angular/core";
import { FormControl } from "@angular/forms";
import { AuthenticationService } from "app/auth/service";
import { BuyerSellerResponse, Graph } from "app/main/model/response.model";
import { ApiAuditLogsService } from "app/main/service/api.audit.logs.servce";
import { ChartService } from "app/main/service/chart.servce";
import { FlatpickrOptions } from "ng2-flatpickr";
@Component({
  selector: "app-buyer-transaction",
  templateUrl: "./buyer-transaction.component.html",
  styleUrls: ["./buyer-transaction.component.scss"],
})
export class BuyerTransactionComponent implements OnInit {
  start:string; 
  endTime: Date;
  end: string;
  flag=false;
  date=new FormControl();
  countArraySave:Array<BuyerSellerResponse>=[];
  countArray:Array<BuyerSellerResponse>=[];
  @ViewChild("tableObj")
  tableObj: any;


  startDay:string;
  startWeek:string;
  startMonth:string;
  startYear:string;
  refreshFlag=true;
  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    mode: "range",
    altInputClass:
      "form-control flat-picker bg-transparent border-0 shadow-none flatpickr-input",

    defaultDate: "4-01-2022 to 4-05-2022",
    dateFormat: "d-m-Y",
    altFormat: "j-m-Y",
    clickOpens:false,
  };

  constructor(
    private apiAuditLogsService: ApiAuditLogsService,
    private chartService: ChartService,private _authenticationService:AuthenticationService
  ) {
    let dt = new Date();
     let startTime = this.getStartDay(365);
     this.start = this.apiAuditLogsService.getFormatDate(startTime);
    this.endTime = dt;
    this.end = this.apiAuditLogsService.getFormatDate(dt);


    let startDayTime = this.getStartDay(10);
    let startWeekTime = this.getStartDay(28);
    let startMonthTime = this.getStartDay(90);
    let startYearTime = this.getStartDay(365);
    this.startDay = this.apiAuditLogsService.getFormatDate(startDayTime);
    this.startWeek = this.apiAuditLogsService.getFormatDate(startWeekTime);
    this.startMonth = this.apiAuditLogsService.getFormatDate(
      startMonthTime);
    this.startYear = this.apiAuditLogsService.getFormatDate(startYearTime);

    this._authenticationService.subject.subscribe(e=>{
      this.refreshFlag=false;
      setTimeout(()=>{
        this.refreshFlag=true;
      },10)
    });
  }
  getStartDay(days: number) {
    return new Date(new Date().getTime() - days * 24 * 60 * 60 * 1000);
  }
  onSerach(){
    let date =this.date.value;

    if (!date || date.length < 2) {      
    } else {
      this.start=this.apiAuditLogsService.getFormatDate(date[0]);
      this.end=this.apiAuditLogsService.getFormatDate(date[1]); 
      this.DateRangeOptions.defaultDate = this.start + " to " + this.end;
    }

    this.flag = true;
    //this.DateRangeOptions.defaultDate = [this.start, this.end];
    this.apiAuditLogsService
      .getDashboardSellerBuyerBytype('buyer', this.start, this.end)
      .subscribe((item: Array<BuyerSellerResponse>) => {
       this.countArray=item;  
       this.countArraySave=item;
        this.flag = false;
      });
  }
  getStartMonth(date: Date) {}
  ngOnInit() {
    //this.lineChart=this.chartService.getLineChartJS();
    this.DateRangeOptions.defaultDate = this.start + " to " + this.end;
    setTimeout(
      () => {
        this.onSerach();
      },
      !this._authenticationService.isLogin() ? 1000 : 1
    );
  }

  filterUpdate(event) {
    const val = event.target.value.toLowerCase();

    let tempData = this.countArraySave;

    // filter our data
    const temp = tempData.filter((d: any) => {
      let flag = !val;
      //console.log(d.toString());
      if (!flag) {
        Object.keys(d).forEach((item) => {
          try {
            flag = flag || d[item].toLowerCase().indexOf(val) !== -1;
          } catch (error) { }
        });
      }
      return flag;
    });

      this.countArray = temp;
      this.tableObj.offset = 0;
    
    // Whenever the filter changes, always go back to the first page
  }
}
