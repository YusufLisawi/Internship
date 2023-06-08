import { Component, Input, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Candidate } from "@models/Candidate.model";
import { Session } from "src/app/models/Session.model";
import { CandidateService } from "@services/candidate.service";
import { SessionService } from "src/app/services/session.service";
import Swal from "sweetalert2";
import { Level } from "@models/Level.enum";
// import { UploaderService } from "@services/uploader.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { AddComponent } from "../add/add.component";
import { PreselectionComponent } from "../preselection/preselection.component";
import { UpdateComponent } from "../update/update.component";
import { environment } from "src/environments/environment";
import { Router } from "@angular/router";
import { InternshipService } from "@services/internship.service";
import { Internship } from "@models/Internship.model";

@Component({
  selector: "app-list",
  templateUrl: "./list.component.html",
  styleUrls: ["./list.component.scss"],
})
export class ListComponent implements OnInit {
  nameRegularExpression = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
  mailRegularExpression = "[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}";
  phoneRegularExpression =
    "^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$";
  @Input() selectedSession: Session;
  myFiles: string[] = [];

  public backUrl: string = environment.apiUrl;

  closeResult: string;
  firstName: string;
  lastName: string;
  email: string;
  levelOfStudy: string;
  university: string;
  public asc: boolean = true;
  public orderBy: string;
  city: string;
  searchText: string;
  lvls: string[] = ["BAC+2", "BAC+3", "BAC+5", "BAC+8"];

  collectionSize: number;
  page = 1;
  pageSize = 15;

  public isFilterOpen: boolean = false;
  public universities: string[] = [];
  public cities: string[] = [];
  public candidates: Candidate[] = [];
  public candidatesPres: Candidate[] = [];
  public candidatesFiltered: Candidate[] = [];
  public selectedRow: Candidate = new Candidate(
    "",
    "",
    "",
    "",
    "",
    Level.Default
  );
  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = "";
  src = "";
  data: Blob;

  constructor(
    private candidateService: CandidateService,
    private sessionService: SessionService,
    private internshipService: InternshipService,
    private modalService: NgbModal,
    private router: Router
  ) {}

  ngOnInit() {
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
          this.router.navigate([
            this.router.url.substring(0, this.router.url.lastIndexOf("/")) +
              "/session",
          ]);
        });
      }
    });
    this.candidateService
      .getUniversities()
      .subscribe((res) => (this.universities = res));
    this.refreshCandidates();
  }

  changeOrder(column: string) {
    if (this.orderBy == column) this.asc = !this.asc;
    else this.orderBy = column;
  }

  onFileSelected(event) {
    for (const f of event.target.files) {
      console.log(f.name);
    }
  }

  public editCandidate(candidate: Candidate): void {
    this.selectedRow = candidate;
    if (candidate.cvname) {
      this.src = "/api/file/find/" + candidate.cvname;
    }
  }

  public setPreselectionStatus(
    candidateId: number,
    preselectionStatus: boolean
  ): void {
    let internships: Internship[] = [];
    this.internshipService.getCurrentSessionInternships().subscribe((data) => {
      if (data != null) {
        internships = data;
        if (
          !preselectionStatus &&
          internships.find(
            (internship) => internship.candidate.candidateId === candidateId
          )
        ) {
          Swal.fire({
            title: "Error!",
            text: "You can't delete this candidate because he has an internship in the current session.",
            icon: "error",
            showConfirmButton: false,
            timer: 2500,
          });
          return;
        } else {
          this.candidateService
            .setPreselectionStatus(candidateId, preselectionStatus)
            .subscribe(
              (res) => {
                this.refreshCandidates();
                Swal.fire({
                  title: "Succes!",
                  text: res.message,
                  icon: "success",
                  iconColor: "#6485C1",
                  showConfirmButton: false,
                  timer: 1500,
                });
              },
              (err) =>
                Swal.fire({
                  title: "Error!",
                  text: err.error.message,
                  icon: "error",
                  showConfirmButton: false,
                  timer: 2500,
                })
            );
        }
      }
    });
  }

  public deleteCandidate(candidateId: number): void {
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
        this.candidateService.delete(candidateId).subscribe((res) => {
          this.refreshCandidates();
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

  public getCvName(cvName: string): string {
    return cvName.split("_").slice(1, cvName.split("_").length).join("_");
  }

  public onselectFile(e): void {
    this.selectedFiles = e.target.files;

    if (e.target.files) {
      var reader = new FileReader();
      reader.readAsDataURL(e.target.files[0]);
      reader.onload = (event: any) => {
        this.src = event.target.result;
      };
    }
  }

  public refreshCandidates(): void {
    this.candidateService.getAll().subscribe((data: Candidate[]) => {
      this.collectionSize = data.length;
      this.candidates = data;
      this.candidatesPres = data;
      this.candidatesFiltered = data
        .map((Candidate, i) => ({ id: i + 1, ...Candidate }))
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
      modalDialogClass: " modal-lg modalLargecv",
    });
    modalRef.result
      .then((result) => {
        if (result) {
          this.refreshCandidates();
        }
      })
      .catch((error) => {});
  }

  openUpdateModal(recruiterId: number) {
    const modalRef = this.modalService.open(UpdateComponent, {
      windowClass: "dark-modal",
      centered: true,
      modalDialogClass: " modal-lg modalLargecv",
    });
    modalRef.componentInstance.UpdatedId = recruiterId;
    modalRef.result
      .then((result) => {
        if (result) {
          this.refreshCandidates();
        }
      })
      .catch((error) => {});
  }

  openPreselectionModal() {
    const modalRef = this.modalService.open(PreselectionComponent, {
      windowClass: "dark-modal",
      centered: true,
      modalDialogClass: " modal-lg modalLargecv",
    });
    modalRef.componentInstance.isChanged.subscribe(($event) => {
      if ($event) {
        this.ngOnInit();
      }
    });
    modalRef.result.then((result) => {}).catch((error) => {});
  }
}
