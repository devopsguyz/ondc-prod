import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { AuthenticationService } from "app/auth/service";
import { colors } from "app/colors.const";
import { Graph } from "../../model/response.model";
import { ApiAuditLogsService } from "../../service/api.audit.logs.servce";
import { ChartService } from "../../service/chart.servce";

@Component({
  selector: "app-logs-div-chart-widget",
  templateUrl: "./div.chart.widget.component.html",
  styleUrls: ["./div.chart.widget.component.scss"],
})
export class DivChartWidgetComponent implements OnInit {
  @ViewChild("statisticsLineRef") statisticsLineRef: any;

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
  api: string;
  // Lifecycle Hooks
  // -----------------------------------------------------------------------------------------------------
  public statisticsLine;
  public isMenuToggled = false;
  public flag = false;
  constructor(
    private _authenticationService: AuthenticationService,
    private apiAuditLogsService: ApiAuditLogsService,
    private chartService: ChartService
  ) {}

  /**
   * On init
   */
  ngOnInit() {
    setTimeout(
      () => {
        this.apiAuditLogsService
          .getDashboardBytype(this.api,this.type,this.start,this.end,'auto')
          .subscribe((item: Array<Graph<number>>) => {
            if (item && item.length > 0) {
              item = item.reverse();
            } else {
              item = [];
            }
            this.statisticsLine = this.chartService.createChartForApex(
              item,
              this.seriesTitle,
              this.index
            );
            this.flag = true;
          });

        
      },
      !this._authenticationService.isLogin() ? 1000 : 1
    );
  }
}
