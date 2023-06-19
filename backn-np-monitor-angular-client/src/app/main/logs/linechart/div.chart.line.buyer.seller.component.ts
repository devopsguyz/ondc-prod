import {
  Component,
  Input,
  OnInit,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import { CoreTranslationService } from "@core/services/translation.service";
import { AuthenticationService } from "app/auth/service";
import { colors } from "app/colors.const";
import { FlatpickrOptions } from "ng2-flatpickr";
import { BuyerSellerResponse, Graph } from "../../model/response.model";
import { ApiAuditLogsService } from "../../service/api.audit.logs.servce";
import { ChartService } from "../../service/chart.servce";
import { locale as en } from "../i18n/en";
import { locale as fr } from "../i18n/fr";
import { locale as de } from "../i18n/de";
import { locale as pt } from "../i18n/pt";
import { FormControl } from "@angular/forms";
import { ToastrService } from "ngx-toastr";
@Component({
  selector: "app-logs-div-chart-line-buyer-seller",
  templateUrl: "./div.chart.line.buyer.seller.component.html",
  styleUrls: ["./div.chart.line.buyer.seller.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class DivChartBuyerSellerLineComponent implements OnInit {
  @ViewChild("ng2FlatPicker") ng2FlatPicker: any;
  @Input()
  seriesTitle: string;

  @Input()
  title: string;

  @Input()
  type: string;

  @Input()
  index: number = 0;

  @Input()
  start: string;

  @Input()
  end: string;

  @Input()
  endTime: string;

  @Input()
  api: string;

  date: FormControl;

  public radioModel = "DAYS";
  lineChart;
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

  // Lifecycle Hooks
  // -----------------------------------------------------------------------------------------------------
  public statisticsLine;
  public isMenuToggled = false;
  public flag = false;
  constructor(
    private _coreTranslationService: CoreTranslationService,
    private _authenticationService: AuthenticationService,
    private apiAuditLogsService: ApiAuditLogsService,
    private chartService: ChartService,
    private toastr: ToastrService
  ) {
    this._coreTranslationService.translate(en, fr, de, pt);
    this.date = new FormControl("");

    //this.date.setValue([this.startTime,this.endTime]);
    this.date.valueChanges.subscribe((value) => {
      console.log(value);
    });
  }
  ngAfterViewInit() {}
  /**
   * On init
   */
  ngOnInit() {
    //this.lineChart=this.chartService.getLineChartJS();
    this.DateRangeOptions.defaultDate = this.start + " to " + this.end;
    setTimeout(
      () => {
        this.refresh();
      },
      !this._authenticationService.isLogin() ? 1000 : 1
    );
  }

  reload() {
    this.type = this.radioModel;
    //this.start=
    let date = this.date.value;

    if (!date || date.length < 2) {
    } else {
      this.start = this.apiAuditLogsService.getFormatDate(date[0]);
      this.end = this.apiAuditLogsService.getFormatDate(date[1]);
      this.DateRangeOptions.defaultDate = this.start + " to " + this.end;
    }

    this.refresh();
  }
  refresh() {
    this.flag = false;
    //this.DateRangeOptions.defaultDate = [this.start, this.end];
    this.apiAuditLogsService
      .getDashboardSellerBuyerBytype(this.api, this.start, this.end)
      .subscribe((item: Array<BuyerSellerResponse>) => {
        let graph: Array<Graph<number>>=[];
        if (item.length > 0) {
          graph = item
            .slice(0, item.length >= 10 ? 10 : item.length)
            .map((element) => {
              return {
                x: element.buyerId ? element.buyerId : element.sellerId,
                y: element.count,
              };
            });

           
        }
          this.lineChart = this.chartService.getLineChartApex(
            graph,
              this.seriesTitle
            );
        
        this.flag = true;
      });
  }
}
