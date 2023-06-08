import { Component, Input, OnInit } from "@angular/core";
import { Internship } from "@models/Internship.model";
import { Session } from "@models/Session.model";
import { environment } from "src/environments/environment";
import { InternshipService } from "@services/internship.service";
import { SessionService } from "@services/session.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ActivatedRoute, Router } from "@angular/router";
import { AddComponent } from "../add/add.component";
import { CandidateService } from "@services/candidate.service";
import Swal from "sweetalert2";
import { UpdateComponent } from "../update/update.component";
import { EInternshipType } from "@models/InternshipType.enum";
import { PdfService } from "@services/pdf.service";
import { UploaderService } from "@services/uploader.service";
import { DocsService } from "@services/docs.service";

@Component({
  selector: "app-list",
  templateUrl: "./list.component.html",
  styleUrls: ["./list.component.scss"],
})
export class ListComponent implements OnInit {
  //Data
  public internships: Internship[] = [];
  public internshipsFiltered: Internship[] = [];
  public selectedRow: Internship = new Internship(
    null,
    null,
    null,
    null,
    null,
    null,
    null
  );
  @Input() selectedSession: Session;

  isFilterOpen: boolean = false;
  searchText: string;
  closeResult: string;
  levelOfStudy: string;
  intenshipStatus: string;
  university: string;
  city: string;

  lvls: string[] = ["BAC+2", "BAC+3", "BAC+5", "BAC+8"];
  statuses: string[] = ["in progress", "rated", "completed"];
  universities: string[] = [];
  cities: string[] = [];

  public asc: boolean = true;
  public orderBy: string;

  collectionSize: number;
  page = 1;
  pageSize = 15;

  selectedFiles: FileList;
  currentFile: File;
  myFiles: string[] = [];
  public backUrl: string = environment.apiUrl;

  data: Blob;
  message = "";
  src = "";
  filename = "";
  certificateFile: File;

  constructor(
    private internshipService: InternshipService,
    private candidateService: CandidateService,
    private sessionService: SessionService,
    private modalService: NgbModal,
    private router: Router,
    private pdfService: PdfService,
    private docsService: DocsService,
    private uploader: UploaderService
  ) {}

  ngOnInit(): void {
    this.candidateService.getUniversities().subscribe((data) => {
      this.universities = data;
    });
    this.candidateService.getCities().subscribe((data) => {
      this.cities = data;
    });
    this.sessionService.getSessionsByStatus("current").subscribe((data) => {
      if (data !== null) {
        this.selectedSession = data;
      } else {
        Swal.fire({
          title: "Information !",
          text: "You have to create a session first !!",
          icon: "info",
          iconColor: "#6485C1",
          showConfirmButton: true,
        }).then((res) => {
          this.router.navigate(["../internship/session"]);
        });
      }
    });
    this.candidateService.getAll().subscribe((data) => {
      if (data.length == 0) {
        Swal.fire({
          title: "Information !",
          text: "You have to create a candidate first !!",
          icon: "info",
          iconColor: "#6485C1",
          showConfirmButton: true,
        }).then((res) => {
          this.router.navigate(["../internship/candidate"]);
        });
      }
    });

    this.internshipService.getCurrentSessionInternships().subscribe((data) => {
      this.internships = data;
    });
    this.refreshInternships();
  }

  changeOrder(column: string) {
    if (this.orderBy == column) this.asc = !this.asc;
    else this.orderBy = column;
  }

  public refreshInternships(): void {
    this.internshipService
      .getCurrentSessionInternships()
      .subscribe((data: Internship[]) => {
        this.collectionSize = data.length;
        this.internships = data;
        this.internshipsFiltered = data
          .map((internship, i) => ({ id: i + 1, ...internship }))
          .slice(
            (this.page - 1) * this.pageSize,
            (this.page - 1) * this.pageSize + this.pageSize
          );
      });
  }

  openAddModal() {
    const modalRef = this.modalService.open(AddComponent, {
      windowClass: "dark-modal",
      centered: true,
      modalDialogClass: " modal-sm modalLargecv",
    });
    modalRef.result
      .then((result) => {
        if (result) {
          this.refreshInternships();
        }
      })
      .catch((error) => {});
  }

  openUpdateModal(internshipId: number) {
    const modalRef = this.modalService.open(UpdateComponent, {
      windowClass: "dark-modal",
      centered: true,
      modalDialogClass: " modal-sm modalLargecv",
    });
    modalRef.componentInstance.UpdatedId = internshipId;
    modalRef.result
      .then((result) => {
        if (result) {
          this.refreshInternships();
        }
      })
      .catch((error) => {});
  }

  isDownloadable(internship: Internship) {
    let d = new Date();
    var now = new Date(d.getFullYear(), d.getMonth(), d.getDate());
    let endDate: Date = new Date(internship.endDate);
    return internship.internshipStatus == "rated" && endDate < now
      ? true
      : false;
  }

  DownloadCertificate(internship: Internship) {
    this.uploader.findById(internship.internshipId).subscribe({
      next: (fileUploaderRes) => {
        this.filename = fileUploaderRes.message;
        if (!this.filename) {
          this.certificateFile =
            this.pdfService.exportPDFCertificate(internship);
          this.uploader.upload(this.certificateFile).subscribe({
            next: (fileUploaderRes) => {
              this.filename = fileUploaderRes.message;
              Swal.fire({
                title: "Succes!",
                text: "PDF file dowloaded successfully",
                icon: "success",
                iconColor: "#6485C1",
                showConfirmButton: false,
                timer: 1500,
              });
            },
            error: (err) => {
              Swal.fire({
                title: "Error!",
                text: err.error,
                icon: "error",
                showConfirmButton: false,
                timer: 2500,
              });
            },
          });
        } else {
          this.uploader.find(this.filename).subscribe(
            (response) => {
              const blob = new Blob([response], { type: "application/pdf" });
              const downloadURL = URL.createObjectURL(blob);
              const link = document.createElement("a");
              link.href = downloadURL;
              link.download = this.filename;
              link.click();
              Swal.fire({
                title: "Succes!",
                text: "PDF file dowloaded successfully",
                icon: "success",
                iconColor: "#6485C1",
                showConfirmButton: false,
                timer: 1500,
              });
            },
            (err) => {
              Swal.fire({
                title: "Error!",
                text: err.error,
                icon: "error",
                showConfirmButton: false,
                timer: 2500,
              });
            }
          );
        }
      },
    });
  }

  public deleteInternship(internshipId: number): void {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      iconColor: "#8B0000",
      showCancelButton: true,
      confirmButtonColor: "#6485C1",
      cancelButtonColor: "#808080",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.isConfirmed) {
        this.internshipService.delete(internshipId).subscribe((res) => {
          this.refreshInternships();
          Swal.fire({
            title: "Succes!",
            text: res.message,
            icon: "success",
            iconColor: "#6485C1",
            showConfirmButton: false,
            timer: 1500,
          });
        });
      }
    });
  }

  public enumToString(type: EInternshipType) {
    if (type == EInternshipType.TYPE_PFA) {
      return "PFA";
    } else if (type == EInternshipType.TYPE_PFE) {
      return "PFE";
    } else if (type == EInternshipType.TYPE_STAGE) {
      return "Stage";
    }
  }
}
