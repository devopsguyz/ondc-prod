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
import { Graph } from "../../model/response.model";
import { ApiAuditLogsService } from "../../service/api.audit.logs.servce";
import { ChartService } from "../../service/chart.servce";
import { locale as en } from "../i18n/en";
import { locale as fr } from "../i18n/fr";
import { locale as de } from "../i18n/de";
import { locale as pt } from "../i18n/pt";
import { FormControl } from "@angular/forms";
@Component({
  selector: "app-logs-chart-line",
  templateUrl: "./chart.line.component.html",
  styleUrls: ["./chart.line.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class ChartLineComponent implements OnInit {
  @ViewChild("lineChartRef") lineChartRef: any;
  @Input()
  lineChart: any;
  public isMenuToggled = false;

  constructor(
    private _coreTranslationService: CoreTranslationService,
    private _authenticationService: AuthenticationService,
    private apiAuditLogsService: ApiAuditLogsService,
    private chartService: ChartService
  ) {
    this._coreTranslationService.translate(en, fr, de, pt);
 
  }
 
  /**
   * On init
   */
  ngOnInit() {
    ;
   
  }
  ngAfterViewInit() {
    this.lineChart.chart.width = this.lineChartRef?.nativeElement.offsetWidth;
        
  }
  refresh() {
   }
}
