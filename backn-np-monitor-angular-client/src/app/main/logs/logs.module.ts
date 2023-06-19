import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { CoreCommonModule } from '@core/common.module';

import { ContentHeaderModule } from 'app/layout/components/content-header/content-header.module';
import {  NgApexchartsModule } from 'ng-apexcharts';
import { TransactionComponent } from './transaction/transaction.component';
import { HomeComponent } from './home.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng2FlatpickrModule } from 'ng2-flatpickr';
import { CardSnippetModule } from '@core/components/card-snippet/card-snippet.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { ToastrModule } from 'ngx-toastr'; 
import { ChartWidgetComponent } from './widget/chart.widget.component';
import { ChartsModule } from 'ng2-charts';
 
import { CommonModule } from '@angular/common';
import { DivChartLineComponent } from './linechart/div.chart.line.component';
import {DivChartBuyerSellerLineComponent}  from './linechart/div.chart.line.buyer.seller.component';
import { DivChartWidgetComponent } from './widget/div.chart.widget.component';
import { ChartLineComponent } from './linechart/chart.line.component';
import { SummaryReportComponent } from './transaction-summary/summary-report.component';
import { SellerTransactionComponent } from './transaction-seller/seller-transaction.component';
import { BuyerTransactionComponent } from './transaction-buyer/buyer-transaction.component';
import { LookupTransactionComponent } from './transaction-lookup/lookup-transaction.component';
import { DomainWhitelistComponent } from './domain-whitelist/domain-whitelist.component';
import { LogsSubscriberIdComponent } from './logs_subscriber_id/logs_subscriber_id.component';
import { LookupChangesComponent } from './lookup-changes/lookup-changes.component';
import { ServerPerformanceComponent } from './server-performance/server-performance.component';
import { SellerBuyerComponent } from './sellerbuyer/seller-buyer.component';
 
const routes = [
  {
    path: 'transactions',
    component: TransactionComponent,
    data: { animation: 'transaction' }
  },
  {
    path: 'home',
    component: HomeComponent,
    data: { animation: 'home' }
  },
  {
    path: 'summary_report',
    component:SummaryReportComponent ,
    data: { animation: 'home' }
  },

  {
    path: 'seller_transaction',
    component:SellerTransactionComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'buyer_transaction',
    component:BuyerTransactionComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'lookup_transaction',
    component:LookupTransactionComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'domain_whitelist',
    component:DomainWhitelistComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'subscribe_logs',
    component:LogsSubscriberIdComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'lookup_Changes',
    component:LookupChangesComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'server_performance',
    component:ServerPerformanceComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'seller',
    component:SellerBuyerComponent ,
    data: { animation: 'home' }
  },
  {
    path: 'buyer',
    component:SellerBuyerComponent ,
    data: { animation: 'home' }
  },
];

@NgModule({
  declarations: [DivChartBuyerSellerLineComponent,LookupTransactionComponent,LogsSubscriberIdComponent,ServerPerformanceComponent
    ,TransactionComponent,SummaryReportComponent,BuyerTransactionComponent, HomeComponent,
    ChartWidgetComponent,DivChartWidgetComponent,
    SellerBuyerComponent, SellerTransactionComponent,ChartLineComponent,DivChartLineComponent, DomainWhitelistComponent, LookupChangesComponent],
  imports: [CommonModule,RouterModule.forChild(routes), ContentHeaderModule, TranslateModule, 
    CoreCommonModule,
    NgbModule,
    FormsModule,
    Ng2FlatpickrModule,
    ReactiveFormsModule,
    NgSelectModule,
    ToastrModule,
    ChartsModule,
    NgxDatatableModule,NgApexchartsModule,
    CardSnippetModule],
  exports: [DivChartBuyerSellerLineComponent,LookupTransactionComponent,SellerBuyerComponent,LogsSubscriberIdComponent,TransactionComponent,SummaryReportComponent,BuyerTransactionComponent,SellerTransactionComponent, HomeComponent,ChartWidgetComponent,DivChartWidgetComponent, ChartLineComponent,DivChartLineComponent]
})
export class LogsModule {}
