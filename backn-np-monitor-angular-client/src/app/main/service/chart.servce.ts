import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

import { environment } from "environments/environment";
import { User } from "app/auth/models";
import { HttpCommonService } from "./http.common.servce";
import { Graph, ResponseRet } from "../model/response.model";
import { Observable } from "rxjs";
import { colors } from "app/colors.const";

@Injectable({ providedIn: "root" })
export class ChartService {
  /**
   *
   * @param {HttpClient} _http
   */
  constructor(private _http: HttpCommonService) {}
  private $trackBgColor = "#EBEBEB";
  private tooltipShadow = 'rgba(0, 0, 0, 0.25)';
  private warningColorShade = '#ffe802'; 
  private grid_line_color = 'rgba(200, 200, 200, 0.2)'; // RGBA color helps in dark layout
  private labelColor = '#6e6b7b';
  private lineChartDanger = '#ff4961';
  private lineChartPrimary = '#666ee8';

  private $stroke_color = '#ebe9f1';
  private $label_color = '#e7eef7';
  private $purple = '#df87f2';
  private $primary = '#7367F0';
  colorsIndex = [colors.solid.info, colors.solid.success, colors.solid.warning];
  getXYFromArray(data: Array<Graph<number>>) {
    let x = [];
    let y = [];
    data.forEach((item) => {
      x.push(item.x);
      y.push(item.y);
    });
    return [x, y];
  }
  /**
   * Get all users
   */
  createChartForApex(
    data: Array<Graph<any>>,
    seriesTtile: string,
    index: number
  ) {
    let dataArray = this.getXYFromArray(data);
    let statisticsLine = {
      chart: {
        height: 70,
        type: "line",
        toolbar: {
          show: false,
        },
        zoom: {
          enabled: false,
        },
      },
      grid: {
        // show: true,
        borderColor: this.$trackBgColor,
        strokeDashArray: 5,
        xaxis: {
          lines: {
            show: true,
          },
        },
        yaxis: {
          lines: {
            show: false,
          },
        },
        padding: {
          // left: 0,
          // right: 0,
          top: -30,
          bottom: -10,
        },
      },
      stroke: {
        width: 3,
      },
      colors: [this.colorsIndex[index]],
      series: [
        {
          name: seriesTtile,
          data: dataArray[1],
        },
      ],
      markers: {
        size: 2,
        colors: this.colorsIndex[index],
        strokeColors: this.colorsIndex[index],
        strokeWidth: 2,
        strokeOpacity: 1,
        strokeDashArray: 0,
        fillOpacity: 1,
        discrete: [
          {
            seriesIndex: 0,
            dataPointIndex: 5,
            fillColor: "#ffffff",
            strokeColor: this.colorsIndex[index],
            size: 5,
          },
        ],
        shape: "circle",
        radius: 2,
        hover: {
          size: 3,
        },
      },
      xaxis: {
        labels: {
          show: true,
          style: {
            fontSize: "0px",
          },
        },
        axisBorder: {
          show: false,
        },
        axisTicks: {
          show: false,
        },
        categories: dataArray[0],
      },
      yaxis: {
        show: false,
      },
      tooltip: {
        x: {
          show: false,
        },
      },
    };
    return statisticsLine;
  }
getLineChartApex(data: Array<Graph<number>>,
  seriesTtile: string){
    let dataArray = this.getXYFromArray(data);
 
  let line = {
    chart: {
      height: 270,
      toolbar: { show: false },
      type: 'line',
      dropShadow: {
        enabled: true,
        top: 20,
        left: 2,
        blur: 6,
        opacity: 0.2
      }
    },
    stroke: {
      curve: 'smooth',
      width: 4
    },
    grid: {
      borderColor: this.$label_color
    },
    legend: {
      show: false
    },
    colors: [this.$purple],
    fill: {
      type: 'gradient',
      gradient: {
        shade: 'dark',
        inverseColors: false,
        gradientToColors: [this.$primary],
        shadeIntensity: 1,
        type: 'horizontal',
        opacityFrom: 1,
        opacityTo: 1,
        stops: [0, 100, 100, 100]
      }
    },
    xaxis: {
      labels: {
        style: {
          colors: this.$stroke_color
        }
      },
      axisTicks: {
        show: false
      },
      categories: dataArray[0],
      axisBorder: {
        show: false
      },
      tickPlacement: 'on'
    },
    yaxis: {
      tickAmount: 5,
      labels: {
        formatter: function (val) {
          return <string>(val);
        }
      }
      
    },
    series: [
      {
        name: seriesTtile,
        data: dataArray[1],
      },
    ],
    tooltip: {
      x: { show: false }
    }
  };
return line;
}
  getLineChartJS( data: Array<Graph<number>>,
    seriesTtile: string) {
      let dataArray = this.getXYFromArray(data);
    let lineChart = {
      chartType: "line",
      options: {
        responsive: true,
        maintainAspectRatio: false,
        backgroundColor: false,
        hover: {
          mode: "label",
        },
        tooltips: {
          // Updated default tooltip UI
          shadowOffsetX: 1,
          shadowOffsetY: 1,
          shadowBlur: 8,
          shadowColor: this.tooltipShadow,
          backgroundColor: colors.solid.white,
          titleFontColor: colors.solid.black,
          bodyFontColor: colors.solid.black,
        },
        scales: {
          xAxes: [
            {
              display: true,
              scaleLabel: {
                display: true,
              },
              gridLines: {
                display: true,
                color: this.grid_line_color,
                zeroLineColor: this.grid_line_color,
              },
              categories:dataArray[0],
              ticks: {
                fontColor: this.labelColor,
              },
            },
          ],
          yAxes: [
            {
              display: true,
              scaleLabel: {
                display: true,
              },
              ticks: {
                stepSize: 100,
                min: 0,
                max: 400,
                fontColor: this.labelColor,
              },
              gridLines: {
                display: true,
                color: this.grid_line_color,
                zeroLineColor: this.grid_line_color,
              },
            },
          ],
        },
        layout: {
          padding: {
            top: -15,
            bottom: -25,
            left: -15,
          },
        },
        legend: {
          position: "top",
          align: "start",
          labels: {
            usePointStyle: true,
            padding: 25,
            boxWidth: 9,
          },
        },
      },

      labels: dataArray[0],
      datasets: [
        {
          data: dataArray[1],
          label: seriesTtile,
          borderColor: this.lineChartDanger,
          lineTension: 0.5,
          pointStyle: "circle",
          backgroundColor: this.lineChartDanger,
          fill: false,
          pointRadius: 1,
          pointHoverRadius: 5,
          pointHoverBorderWidth: 5,
          pointBorderColor: "transparent",
          pointHoverBorderColor: colors.solid.white,
          pointHoverBackgroundColor: this.lineChartDanger,
          pointShadowOffsetX: 1,
          pointShadowOffsetY: 1,
          pointShadowBlur: 5,
          pointShadowColor: this.tooltipShadow,
        },
        
      ],
    };
    return lineChart;
  }
}
