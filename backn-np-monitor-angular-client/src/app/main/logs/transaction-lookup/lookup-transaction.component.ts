import { ViewEncapsulation } from '@angular/compiler';
import { Component, OnInit, ViewChild } from '@angular/core'
import { colors } from 'app/colors.const';
import { ApiAuditLogsService } from '../../service/api.audit.logs.servce';
import { ChartService } from '../../service/chart.servce';

@Component({
  selector: 'app-lookup-transaction',
  templateUrl: './lookup-transaction.component.html',
  styleUrls: ['./lookup-transaction.component.scss']
})
export class LookupTransactionComponent implements OnInit {
 startDay:string;
 startWeek:string;
 startMonth:string;
 startYear:string;

 startDayTime:Date;
 startWeekTime:Date;
 startMonthTime:Date;
 startYearTime:Date;
 endTime:Date;
 end:string;


 


  constructor(  private apiAuditLogsService: ApiAuditLogsService,private chartService:ChartService) {
  let dt=new Date();

  this.startDayTime= this.getStartDay(10);
  this.startWeekTime=this.getStartDay(28);
  this.startMonthTime=this.getStartDay(90)
  this.startYearTime=this.getStartDay(365);

  this.startDay=this.apiAuditLogsService.getFormatDate( this.startDayTime);
  this.startWeek=this.apiAuditLogsService.getFormatDate( this.startWeekTime);
  this.startMonth=this.apiAuditLogsService.getFormatDate( this.startMonthTime);
  this.startYear=this.apiAuditLogsService.getFormatDate( this.startYearTime);
 this.endTime=dt;
  this.end=  this.apiAuditLogsService.getFormatDate(dt);

  }

  getStartDay(days:number){
   return new Date(new Date().getTime() - (days * 24 * 60 * 60 * 1000)); 
  }

  getStartMaonth(date:Date){
    
   }

  public contentHeader: object
 
  /**
   * On init
   */
  ngOnInit() {
   
  }

}
