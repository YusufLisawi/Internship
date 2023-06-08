import { Component, OnInit } from "@angular/core";
import { DashboardService } from "src/app/services/dashboard.service";
import { DashboardStatistics } from "@models/DashboardStatistics.model";
import { Chart } from "chart.js";
import { Session } from "@models/Session.model";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { DownloadPdfComponent } from "../download-pdf/download-pdf.component";
import { SessionService } from "@services/session.service";
@Component({
  selector: "app-component",
  templateUrl: "./component.component.html",
  styleUrls: ["./component.component.scss"],
})
export class ComponentComponent implements OnInit {
  public canvas: any;
  public ctx;
  public datasets: any;
  public data: any;
  public selectedSession: Session;
  public allSessions: Session[];
  public citiesChart: Chart;
  public universitiesChart: Chart;
  public genderChart: Chart;
  public diplomeChart: Chart;
  public dataImg1: any;
  dashboardData: DashboardStatistics;
  public admitedPreviousSession: number;
  public currentSession: Session;
  constructor(
    private dashboardService: DashboardService,
    private modalService: NgbModal,
    private sessionService: SessionService
  ) {}

  ngOnInit() {
    this.sessionService.getSessionsByStatus("current").subscribe((data) => {
      this.currentSession = data;
    });

    this.dashboardService
      .countAdmittedPreviousSession()
      .subscribe((data: number) => {
        this.admitedPreviousSession = data;
      });

    this.dashboardService
      .getStatisticsOfLatestSession()
      .subscribe((serverInput: DashboardStatistics) => {
        this.dashboardData = serverInput;
        var gradientBarChartConfiguration: any = {
          maintainAspectRatio: false,
          legend: {
            display: false,
          },
          tooltips: {
            backgroundColor: "#f5f5f5",
            titleFontColor: "#333",
            bodyFontColor: "#666",
            bodySpacing: 4,
            xPadding: 12,
            mode: "nearest",
            intersect: 0,
            position: "nearest",
          },
          responsive: true,
          scales: {
            yAxes: [
              {
                scaleLabel: {
                  display: true,
                  labelString: "Number of Candidates",
                  fontColor: "#666",
                  fontSize: 12,
                  fontStyle: "normal",
                },
                ticks: {
                  stepSize: 1,
                  precision: 0,
                  beginAtZero: true,
                  suggestedStep: 10,
                  suggestedMin: 0,
                  fontColor: "#9e9e9e",
                },
                gridLines: {
                  drawBorder: false,
                  color: "rgba(29,140,248,0.1)",
                },
              },
            ],
          },
        };

        /****************** CITIES  *******************/

        this.canvas = document.getElementById("CitiesChart");
        this.ctx = this.canvas.getContext("2d");
        var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);

        gradientStroke.addColorStop(1, "rgba(146, 129, 232,0.2)");
        gradientStroke.addColorStop(0.4, "rgba(146, 129, 232,0.0)");
        gradientStroke.addColorStop(0, "rgba(146, 129, 232,0)"); //blue colors

        this.citiesChart = new Chart(this.ctx, {
          type: "bar",
          data: {
            labels: Object.keys(this.dashboardData?.candidatesByCities),
            datasets: [
              {
                label: "Number of candidates",
                fill: true,
                backgroundColor: gradientStroke,
                hoverBackgroundColor: gradientStroke,
                borderColor: "#9281E8",
                borderWidth: 2,
                borderDash: [],
                borderDashOffset: 0.0,
                data: Object.values(this.dashboardData.candidatesByCities || 0),
              },
            ],
          },
          options: gradientBarChartConfiguration,
        });

        //this.citiesChartForPdf.t

        /****************** ORIGINAL SCHOOOLS  *******************/
        this.canvas = document.getElementById("UniversityChart");
        this.ctx = this.canvas.getContext("2d");
        var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);

        gradientStroke.addColorStop(1, "rgba(100,71,255,0.2)");
        gradientStroke.addColorStop(0.4, "rgba(100,71,255,0.0)");
        gradientStroke.addColorStop(0, "rgba(100,71,255,0)"); //blue colors

        this.universitiesChart = new Chart(this.ctx, {
          type: "bar",
          data: {
            labels: Object.keys(
              this.dashboardData.candidatesByUniversities || 0
            ),
            datasets: [
              {
                label: "Number of candidates",
                fill: true,
                backgroundColor: gradientStroke,
                hoverBackgroundColor: gradientStroke,
                borderColor: "#1f8ef1",
                borderWidth: 2,
                borderDash: [],
                borderDashOffset: 0.0,
                data: Object.values(
                  this.dashboardData.candidatesByUniversities || 0
                ),
              },
            ],
          },
          options: gradientBarChartConfiguration,
        });

        /****************** GENDER  *******************/

        this.canvas = document.getElementById("GendersChart");
        this.ctx = this.canvas.getContext("2d");
        this.genderChart = new Chart(this.ctx, {
          type: "doughnut",
          data: {
            labels: ["Males", "Females"],
            datasets: [
              {
                label: "Number of candidates",
                data: [
                  serverInput.maleCandidateNumber,
                  serverInput.femaleCandidateNumber,
                ],
                backgroundColor: ["#02386E", "#A31246"],
              },
            ],
          },
          options: {
            legend: {
              position: "bottom",
              onClick: (e) => {},
              reverse: true,
              labels: {
                usePointStyle: true,
                fontSize: 16,
                padding: 20,
                fontColor: "#9e9e9e",
              },
            },
          },
        });

        /***************** DIPLOME ************************/
        this.canvas = document.getElementById("DiplomeChart");
        this.ctx = this.canvas.getContext("2d");
        this.diplomeChart = new Chart(this.ctx, {
          type: "doughnut",
          data: {
            labels: ["Bac+2", "Bac+3", "Bac+5", "Bac+8"],
            datasets: [
              {
                label: "Number of candidates",
                data: [
                  this.dashboardData.candidatesByDiplome["BAC+2"],
                  this.dashboardData.candidatesByDiplome["BAC+3"],
                  this.dashboardData.candidatesByDiplome["BAC+5"],
                  this.dashboardData.candidatesByDiplome["BAC+8"],
                ],
                backgroundColor: ["#00172D", "#01779E", "#4897A9", "#72ABB3"],
              },
            ],
          },
          options: {
            legend: {
              position: "bottom",
              onClick: (e) => {},
              reverse: true,
              labels: {
                usePointStyle: true,
                fontSize: 16,
                padding: 20,
              },
            },
          },
        });
      });
  }

  openModal() {
    const modalRef = this.modalService.open(DownloadPdfComponent);
    modalRef.result.then((result) => {}).catch((error) => {});
  }
}
