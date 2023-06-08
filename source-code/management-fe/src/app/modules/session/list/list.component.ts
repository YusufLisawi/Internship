import { Component, Input, OnInit, ViewEncapsulation } from "@angular/core";
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { SessionService } from "@services/session.service";
import { Session } from "@models/Session.model";
import Swal from "sweetalert2";
import { forkJoin } from "rxjs";
import { CandidateService } from "@services/candidate.service";
import { Candidate } from "@models/Candidate.model";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { UpdateSessionComponent } from "../update-session/update-session.component";
import { AddsessionComponent } from "../add-session/add-session.component";
import { MessageResponse } from "@models/MessageResponse.model";
import { concatMap } from "rxjs/operators";

@Component({
  selector: "app-list",
  templateUrl: "./list.component.html",
  encapsulation: ViewEncapsulation.None,
  styles: [
    `
      .black {
        background-color: #000000;
      }
      .modal .close {
        color: black;
      }
      .modal {
        margin: 0;
      }
    `,
  ],
})
export class ListComponent implements OnInit {
  page = 1;
  pageSize = 5;
  collectionSize: number;
  techs: string[] = ["BAC+2", "BAC+3", "BAC+5"];
  private sessions: Session[] = [];
  public sessionsFiltered: Session[];
  public selectedRow: Session = new Session("", new Date(), "", "");
  public currentSession: Session = new Session("", new Date(), "", "");
  addReactiveForm: UntypedFormGroup = this.formBuilder.group({
    sessionName: [
      "",
      Validators.compose([Validators.required, Validators.maxLength(20)]),
    ],
    technology: [
      "",
      Validators.compose([Validators.required, Validators.maxLength(20)]),
    ],
    sessionDate: ["", Validators.compose([Validators.required])],
  });
  editReactiveForm: UntypedFormGroup = this.formBuilder.group({
    sessionName: [
      this.selectedRow.sessionName,
      Validators.compose([Validators.maxLength(20)]),
    ],
    technology: [
      this.selectedRow.technology,
      Validators.compose([Validators.maxLength(20)]),
    ],
    sessionDate: [this.selectedRow.sessionDate],
  });
  errMessage: string;
  formSubmited: boolean;
  public isFilterOpen: boolean = false;
  public asc: boolean = true;
  public orderBy: string;
  public technologies: string[] = [];
  public searchText: string;
  public sessionDate: Date;
  public technology: string;
  public isInternship: boolean;
  is_Internship: boolean ; 

  constructor(
    private modalService: NgbModal,
    private sessionService: SessionService,
    private router: Router,
    private formBuilder: UntypedFormBuilder,
    private candidateService : CandidateService
   ) { 
    const urlPath = this.router.routerState.snapshot.root.children[0]?.url[0]?.path;
    this.is_Internship = urlPath === 'internship';
   }


  ngOnInit() {
    this.refreshSessions();
    this.sessionService.getAllTechnologies().subscribe((data: string[]) => {
      this.technologies = data;
    });
  }

  openAddSessionModal() {
    this.resetForm();
    const modalRef = this.modalService.open(AddsessionComponent, {
      windowClass: "dark-modal",
      modalDialogClass: "modal-md",
    });
    modalRef.result.then((result) => {
      this.refreshSessions()
      if(result){
        console.log(result);
      }
    }).catch((error) => {});
   }

  public addSession(): void {
    this.errMessage = "";
    this.formSubmited = true;

    if (this.addReactiveForm.valid) {
      let session: Session = {
        sessionName: this.addReactiveForm.value.sessionName,
        sessionDate: this.addReactiveForm.value.sessionDate,
        admittedNumber: null,
        dayInterviewNumber: null,
        technology: this.isInternship
          ? "Internship"
          : this.addReactiveForm.value.technology,
        sessionStatus: "current",
        recruiter: null,
      };

      session.dayInterviewNumber = this.currentSession?.dayInterviewNumber || 100;
      session.admittedNumber = this.currentSession?.admittedNumber || 20;
      updateSessionPrevious: this.sessionService
        .updateSessionPrevious()
        .pipe(
          concatMap(() => this.sessionService.updateSessionClose()),
          concatMap(() => this.sessionService.add(session))
        )
        .subscribe({
          next: (res) => {
           // this.refreshCandidates();
            Swal.fire({
              title: "Succes!",
              text: res.message,
              icon: "success",
              iconColor: "#6485C1",
              showConfirmButton: false,
              timer: 1500,
            });
            this.errMessage = "";
            this.addReactiveForm.reset();
            document.getElementById("cheat").click();
            this.formSubmited = false;
          },
          error: (err) => {
            this.errMessage = err.error.message;
            this.formSubmited = false;
          },
        });
    }
  }

  public editSession(session: Session) {
    this.selectedRow = session;
    this.editReactiveForm = this.formBuilder.group({
      sessionName: [
        this.selectedRow.sessionName,
        Validators.compose([Validators.maxLength(20), Validators.required]),
      ],
      technology: [
        this.selectedRow.technology,
        Validators.compose([Validators.maxLength(20), Validators.required]),
      ],
      sessionDate: [
        this.selectedRow.sessionDate,
        Validators.compose([Validators.required]),
      ],
    });

    const modalRef = this.modalService.open(UpdateSessionComponent, {
      windowClass: "dark-modal",
      modalDialogClass: "modal-md",
    });
    modalRef.componentInstance.session = this.selectedRow;
    modalRef.result
      .then((result) => {
        this.refreshSessions();
        if (result) {
        }
      })
      .catch((error) => {});
  }

  private refreshSessions() {
    this.sessionService
      .getSessionsByStatus("current")
      .subscribe((data) => (this.currentSession = data));
    this.sessionService.getAll().subscribe((data: Session[]) => {
      this.collectionSize = data.length;
      this.sessions = data;
      this.sessionsFiltered = data
        .map((candidates, i) => ({ id: i + 1, ...candidates }))
        .slice(
          (this.page - 1) * this.pageSize,
          (this.page - 1) * this.pageSize + this.pageSize
        );
    });
  }

  public changeNavigation() {
    this.sessionsFiltered = this.sessions
      .map((candidates, i) => ({ id: i + 1, ...candidates }))
      .slice(
        (this.page - 1) * this.pageSize,
        (this.page - 1) * this.pageSize + this.pageSize
      );
  }

  public resetForm(): void {
    this.addReactiveForm.reset();
    if (
      this.router.routerState.snapshot.root.children[0].url[0].path ==
      "internship"
    ) {
      this.isInternship = true;
      this.addReactiveForm.get("technology").clearValidators();
      this.addReactiveForm.get("technology").setValue("Internship");
      this.addReactiveForm.get("technology").disable();
    } else {
      this.isInternship = false;
    }
    this.formSubmited = false;
  }
  public changeOrder(column: string) {
    if (this.orderBy == column) this.asc = !this.asc;
    else this.orderBy = column;
  }
}
