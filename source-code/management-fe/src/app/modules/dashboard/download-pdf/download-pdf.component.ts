import { Component, OnInit } from "@angular/core";
import { DashboardStatistics } from "@models/DashboardStatistics.model";
import { Session } from "@models/Session.model";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { DashboardService } from "@services/dashboard.service";
import { SessionService } from "@services/session.service";
import * as Chart from "chart.js";
import jsPDF from "jspdf";

@Component({
  selector: "app-download-pdf",
  templateUrl: "./download-pdf.component.html",
  styleUrls: ["./download-pdf.component.scss"],
})
export class DownloadPdfComponent implements OnInit {
  public canvas: any;
  public ctx;

  public datasets: any;
  public data: any;
  dashboardData: DashboardStatistics;

  public selectedSession: Session;
  public allSessions: Session[];

  public dataImg1: any;

  public citiesChartForPdf: Chart;
  public universitiesChartForPdf: Chart;
  public genderChartForPdf: Chart;
  public diplomeChartForPdf: Chart;

  constructor(
    private dashboardService: DashboardService,
    private sessionService: SessionService,
    private activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    this.sessionService.getSessionsByStatus("current").subscribe((data) => {
      this.selectedSession = data;
      this.generateStatistics(this.selectedSession);

      this.sessionService.getAll().subscribe((sessions) => {
        sessions = sessions.filter((session) => {
          let isCurrent = session.sessionId === this.selectedSession.sessionId;
          return !isCurrent;
        });
        this.allSessions = [this.selectedSession, ...sessions];
      });
    });
  }

  public updateData(selectedSessionId: number) {
    this.dashboardService
      .getStatistics(selectedSessionId)
      .subscribe((serverInput) => {
        this.selectedSession = this.allSessions.filter(
          (s) => s.sessionId == selectedSessionId
        )[0];
        this.dashboardData = serverInput;

        //Diplome
        this.diplomeChartForPdf.data.datasets[0].labels = [
          "Bac+2",
          "Bac+3",
          "Bac+5",
          "Bac+8",
        ];
        this.diplomeChartForPdf.data.datasets[0].data = [
          this.dashboardData.candidatesByDiplome["BAC+2"],
          this.dashboardData.candidatesByDiplome["BAC+3"],
          this.dashboardData.candidatesByDiplome["BAC+5"],
          this.dashboardData.candidatesByDiplome["BAC+8"],
        ];
        this.diplomeChartForPdf.update();

        //Gender
        this.genderChartForPdf.data.datasets[0].data = [
          serverInput.maleCandidateNumber,
          serverInput.femaleCandidateNumber,
        ];
        this.genderChartForPdf.update();

        //University
        this.universitiesChartForPdf.data.labels = Object.keys(
          this.dashboardData.candidatesByUniversities
        );
        this.universitiesChartForPdf.data.datasets[0].data = Object.values(
          this.dashboardData.candidatesByUniversities
        );
        this.universitiesChartForPdf.update();

        //City
        this.citiesChartForPdf.data.labels = Object.keys(
          this.dashboardData.candidatesByCities
        );
        this.citiesChartForPdf.data.datasets[0].data = Object.values(
          this.dashboardData.candidatesByCities
        );
        this.citiesChartForPdf.update();
      });
  }

  public downloadPDF() {
    let diplomeImg = this.diplomeChartForPdf.toBase64Image();
    let genderImg = this.genderChartForPdf.toBase64Image();
    let universitiesImg = this.universitiesChartForPdf.toBase64Image();
    let citiesImg = this.citiesChartForPdf.toBase64Image();
    let logoImg = this.canvas.toDataURL("image/png");
    let x: number, y: number, sp: number;
    const diplomeData = [
      {
        color: "#00172D",
        label: "Bac+2",
        value: this.dashboardData.candidatesByDiplome["BAC+2"] || 0,
      },
      {
        color: "#01779E",
        label: "Bac+3",
        value: this.dashboardData.candidatesByDiplome["BAC+3"] || 0,
      },
      {
        color: "#4897A9",
        label: "Bac+5",
        value: this.dashboardData.candidatesByDiplome["BAC+5"] || 0,
      },
      {
        color: "#72ABB3",
        label: "Bac+8",
        value: this.dashboardData.candidatesByDiplome["BAC+8"] || 0,
      },
    ];
    let pdf = new jsPDF("p", "mm", "a4");
    const pdfWidth = pdf.internal.pageSize.getWidth();

    pdf.addImage(
      "../../../../assets/img/nttdata_logo_header.png",
      "png",
      0,
      0,
      200,
      0
    );

    pdf.setFontSize(30);
    pdf.setFont("times", "bold");

    const title = this.selectedSession.sessionName;
    const titleWidth = pdf.getTextWidth(title);
    const startTitle = (pdfWidth - titleWidth) / 2;

    pdf.text(title, startTitle, 45);

    pdf.setFontSize(12);
    pdf.setFont("times", "normal");

    y = 60;
    sp = 6;
    pdf.text("Date\t\t  :  " + this.selectedSession.sessionDate, 30, y);
    pdf.text(
      "Technologies    :  " + this.selectedSession.technology,
      30,
      y + sp
    );

    pdf.text(
      "Total number of candidates\t:  " + this.dashboardData.candidateNumber,
      120,
      y
    );

    if (this.selectedSession.type == "internship") {
      pdf.text(
        "Admitted candidates\t\t   :  " +
          this.dashboardData.admittedCandidateNumber,
        120,
        y + sp
      );
    } else {
      pdf.text(
        "Preselected candidates\t\t:  " +
          this.dashboardData.preselectedCandidateNumber,
        120,
        y + sp
      );
      pdf.text(
        "Admitted candidates\t\t   :  " +
          this.dashboardData.admittedCandidateNumber,
        120,
        y + sp * 2
      );
    }

    pdf.setFontSize(12);
    var bulletChar = "\u2022";

    x = (210 - 120) / 2;
    y = 90;
    pdf.text(bulletChar + " Admetted candidates gender :", 30, y);
    pdf.addImage(genderImg, "PNG", x, y + 10, 120, 0);
    pdf.setTextColor("#02386E");
    pdf.text(
      "Males     : " + this.dashboardData.maleCandidateNumber,
      140,
      y + 20
    );
    pdf.setTextColor("#A31246");
    pdf.text(
      "Females  : " + this.dashboardData.femaleCandidateNumber,
      140,
      y + 26
    );
    pdf.setTextColor("#000000");

    y = 180;
    pdf.text(bulletChar + " Admetted candidates diploma :", 30, y);
    pdf.addImage(diplomeImg, "PNG", x, y + 10, 120, 0);
    diplomeData.forEach((element, index) => {
      pdf.setTextColor(element.color);
      pdf.text(element.label + " : " + element.value, 140, y + 20 + index * 6);
    });

    //page 2
    pdf.addPage();

    pdf.setTextColor("#000000");
    pdf.text(bulletChar + " Admetted candidates cities: ", 30, 20);
    pdf.addImage(citiesImg, "PNG", 40, 30, 0, 0);

    pdf.text(bulletChar + " Admetted candidates universities: ", 30, 110);
    pdf.addImage(universitiesImg, "PNG", 40, 120, 0, 0);

    pdf.save(
      "Dashboard statistics - " +
        this.selectedSession.sessionName +
        "-" +
        this.selectedSession.sessionId +
        ".pdf"
    );
  }

  public generateStatistics(selectedSession: Session) {
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

    this.dashboardService
      .getStatistics(selectedSession.sessionId)
      .subscribe((serverInput: DashboardStatistics) => {
        this.dashboardData = serverInput;

        //Cities
        this.canvas = document.getElementById("CitiesForPdf");
        this.ctx = this.canvas.getContext("2d");
        var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);

        gradientStroke.addColorStop(1, "rgba(146, 129, 232,0.2)");
        gradientStroke.addColorStop(0.4, "rgba(146, 129, 232,0.0)");
        gradientStroke.addColorStop(0, "rgba(146, 129, 232,0)");

        this.citiesChartForPdf = new Chart(this.ctx, {
          type: "bar",
          data: {
            labels: Object.keys(serverInput.candidatesByCities),
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
                data: Object.values(serverInput.candidatesByCities),
              },
            ],
          },
          options: gradientBarChartConfiguration,
        });
        this.citiesChartForPdf.options.animation.duration = 0;

        //Universities
        this.canvas = document.getElementById("universityForPdf");
        this.ctx = this.canvas.getContext("2d");
        var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);

        gradientStroke.addColorStop(1, "rgba(100,71,255,0.2)");
        gradientStroke.addColorStop(0.4, "rgba(100,71,255,0.0)");
        gradientStroke.addColorStop(0, "rgba(100,71,255,0)"); //blue colors

        this.universitiesChartForPdf = new Chart(this.ctx, {
          type: "bar",
          data: {
            labels: Object.keys(serverInput.candidatesByUniversities),
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
                data: Object.values(serverInput.candidatesByUniversities),
              },
            ],
          },
          options: gradientBarChartConfiguration,
        });
        this.universitiesChartForPdf.options.animation.duration = 0;

        //gender
        this.canvas = document.getElementById("genderForPdf");
        this.ctx = this.canvas.getContext("2d");
        this.genderChartForPdf = new Chart(this.ctx, {
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

        this.genderChartForPdf.options.animation.duration = 0;

        //diplome
        this.canvas = document.getElementById("diplomeForPdf");
        this.ctx = this.canvas.getContext("2d");
        this.diplomeChartForPdf = new Chart(this.ctx, {
          type: "doughnut",
          data: {
            labels: ["Bac+2", "Bac+3", "Bac+5", "Bac+8"],
            datasets: [
              {
                label: "Number of candidates",
                data: [
                  serverInput.candidatesByDiplome["BAC+2"],
                  serverInput.candidatesByDiplome["BAC+3"],
                  serverInput.candidatesByDiplome["BAC+5"],
                  serverInput.candidatesByDiplome["BAC+8"],
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

        this.diplomeChartForPdf.options.animation.duration = 0;
      });
  }

  public closeModal() {
    this.activeModal.close("Modal Closed");
  }
}
