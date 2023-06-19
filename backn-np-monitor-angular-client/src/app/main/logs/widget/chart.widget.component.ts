import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { AuthenticationService } from "app/auth/service";
import { colors } from "app/colors.const";
import { Graph } from "../../model/response.model";
import { ApiAuditLogsService } from "../../service/api.audit.logs.servce";
import { ChartService } from "../../service/chart.servce";

@Component({
  selector: "app-logs-chart-widget",
  templateUrl: "./chart.widget.component.html",
  styleUrls: ["./chart.widget.component.scss"],
})
export class ChartWidgetComponent implements OnInit {
  @ViewChild("statisticsLineRef") statisticsLineRef: any;

  @Input()
  statisticsLine: any;
     
  constructor(
    private _authenticationService: AuthenticationService,
    private apiAuditLogsService: ApiAuditLogsService,
    private chartService: ChartService
  ) {}

  /**
   * On init
   */
  ngOnInit() {
    
  }
}
