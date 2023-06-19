import { ViewEncapsulation } from "@angular/compiler";
import { Component, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "app/auth/service";
import { colors } from "app/colors.const";
import { environment } from "environments/environment";
import { ApiAuditLogsService } from "../service/api.audit.logs.servce";
import { ChartService } from "../service/chart.servce";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"],
})
export class HomeComponent implements OnInit {
  startDay: string;
  startWeek: string;
  startMonth: string;
  startYear: string;

  startDayTime: Date;
  startWeekTime: Date;
  startMonthTime: Date;
  startYearTime: Date;
  endTime: Date;
  end: string;
  refreshFlag=true;
  constructor(
    private _router: Router,
    private apiAuditLogsService: ApiAuditLogsService,
    private chartService: ChartService,
    private _authenticationService:AuthenticationService
  ) {
    let dt = new Date();

    this._authenticationService.subject.subscribe(e=>{
      this.refreshFlag=false;
      setTimeout(()=>{
        this.refreshFlag=true;
      },10)
    });

    if (environment.uiRole == "domain") {
      this._router.navigate(["/domain_whitelist"]);
    } else {
      this.startDayTime = this.getStartDay(10);
      this.startWeekTime = this.getStartDay(28);
      this.startMonthTime = this.getStartDay(90);
      this.startYearTime = this.getStartDay(365);

      this.startDay = this.apiAuditLogsService.getFormatDate(this.startDayTime);
      this.startWeek = this.apiAuditLogsService.getFormatDate(
        this.startWeekTime
      );
      this.startMonth = this.apiAuditLogsService.getFormatDate(
        this.startMonthTime
      );
      this.startYear = this.apiAuditLogsService.getFormatDate(
        this.startYearTime
      );
      this.endTime = dt;
      this.end = this.apiAuditLogsService.getFormatDate(dt);
    }
  }

  getStartDay(days: number) {
    return new Date(new Date().getTime() - days * 24 * 60 * 60 * 1000);
  }

  getStartMaonth(date: Date) {}

  public contentHeader: object;

  /**
   * On init
   */
  ngOnInit() {}
}
