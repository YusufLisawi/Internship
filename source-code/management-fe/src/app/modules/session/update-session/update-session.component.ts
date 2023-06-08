import { Component, Input, OnInit } from "@angular/core";
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { Candidate } from "@models/Candidate.model";
import { MessageResponse } from "@models/MessageResponse.model";
import { Session } from "@models/Session.model";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { SessionService } from "@services/session.service";
import Swal from "sweetalert2";

@Component({
  selector: "app-update-session",
  templateUrl: "./update-session.component.html",
  styleUrls: ["./update-session.component.scss"],
})
export class UpdateSessionComponent implements OnInit {
  @Input() session: Session;
  page = 1;
  pageSize = 5;
  collectionSize: number;
  private sessions: Session[] = [];
  public sessionsFiltered: Session[];
  public selectedRow: Session = new Session("", new Date(), "", "");
  public currentSession: Session = new Session("", new Date(), "", "");
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
  public technologies: string[] = [];
  public sessionDate: Date;
  public technology: string;
  waitingList: Candidate[] = [];
  public isInternship: boolean;

  constructor(
    private activeModal: NgbActiveModal,
    private sessionService: SessionService,
    private formBuilder: UntypedFormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.sessionService.getAllTechnologies().subscribe((data: string[]) => {
      this.technologies = data;
    });
    this.selectedRow = this.session;
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
    if (
      this.router.routerState.snapshot.root.children[0].url[0].path ==
      "internship"
    ) {
      this.isInternship = true;
      this.editReactiveForm.get("technology").clearValidators();
      this.editReactiveForm.get("technology").setValue("Internship");
      this.editReactiveForm.get("technology").disable();
    } else {
      this.isInternship = false;
    }
  }
  public updateSession() {
    this.formSubmited = true;
    if (this.editReactiveForm.valid) {
      this.sessionService
        .update({
          sessionId: this.selectedRow.sessionId,
          sessionName: this.editReactiveForm.value.sessionName
            ? this.editReactiveForm.value.sessionName
            : this.selectedRow.sessionName,
          sessionDate: this.editReactiveForm.value.sessionDate
            ? this.editReactiveForm.value.sessionDate
            : this.selectedRow.sessionDate,
          admittedNumber: this.selectedRow.admittedNumber,
          dayInterviewNumber: this.selectedRow.dayInterviewNumber,
          technology: this.isInternship
            ? "Internship"
            : this.editReactiveForm.value.technology
            ? this.editReactiveForm.value.technology
            : this.selectedRow.technology,
        })
        .subscribe(
          (res: MessageResponse) => {
            this.activeModal.close(true);
            Swal.fire({
              title: "Succes!",
              text: res.message,
              icon: "success",
              iconColor: "#6485C1",
              showConfirmButton: false,
              timer: 1500,
            });
          },
          (err) => {
            this.activeModal.close(true);
            Swal.fire({
              title: "Oops !",
              text: err?.error || "Something went wrong !!",
              icon: "error",
              showConfirmButton: true,
            });
          }
        );
    }
  }

  closeModal() {
    this.activeModal.close(true);
  }
}
