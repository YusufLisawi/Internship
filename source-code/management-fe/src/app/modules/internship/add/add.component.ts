import { Component, OnInit, Input } from "@angular/core";
import {
  UntypedFormGroup,
  UntypedFormBuilder,
  Validators,
} from "@angular/forms";
import { Internship } from "@models/Internship.model";
import { Session } from "@models/Session.model";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { InternshipService } from "@services/internship.service";
import { SessionService } from "@services/session.service";
import { DocsService } from "@services/docs.service";
import { UploaderService } from "@services/uploader.service";
import { PdfViewerModule } from "ng2-pdf-viewer";
import { CandidateService } from "@services/candidate.service";
import Swal from "sweetalert2";
import { Candidate } from "@models/Candidate.model";
import { Router, ActivatedRoute } from "@angular/router";
import { EInternshipType } from "@models/InternshipType.enum";
export interface CandidateOption {
  id:string,
  label: string;
}
@Component({
  selector: "app-add",
  templateUrl: "./add.component.html",
  styleUrls: ["./add.component.scss"],
})


export class AddComponent implements OnInit {


  addReactiveForm: UntypedFormGroup;
  nameRegularExpression = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
  phoneRegularExpression =
    "^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$";
  mailRegularExpression = "[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}";

  selectedSession: Session;

  public internships: Internship[] = [];
  public candidates: Candidate[] = [];
  public candidatesOptions: CandidateOption[] = [];

  collectionSize: number;
  selectedFiles: FileList[] = [];
  cinFile: File;
  agreementFile: File;
  assuranceFile: File;
  cinProgress = 0;
  agreementProgress = 0;
  assuranceProgress = 0;
  src = "";

  constructor(
    private internshipService: InternshipService,
    private sessionService: SessionService,
    private docsService: DocsService,
    private uploader: UploaderService,
    private formBuilder: UntypedFormBuilder,
    private activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private candidateService: CandidateService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}
  selectedCandidate: any;

  ngOnInit(): void {
    this.internshipService.getCurrentSessionInternships().subscribe((data) => {
      if (data !== null) {
        this.internships = data;
      }
    });
    this.candidateService.getAcceptedCandidate().subscribe((data) => {
      if (data !== null) {
        this.candidates = data.filter((candidate) => {
          const existCandidate = this.internships.find(
            (internship) =>
              internship.candidate.candidateId === candidate.candidateId
          );
          return !existCandidate;
        });
      }

      if (this.candidates.length == 0) {
        Swal.fire({
          title: "warning !",
          text: "Add and/or accept candidates to proceed with adding an internship.",
          icon: "warning",
          iconColor: "#f1c40f",
          showConfirmButton: false,
          timer: 4000,
        });
        this.activeModal.close(true);
      }
      this.candidatesOptions=this.candidates.map(candidate => ({
        id: `${candidate.candidateId}`,
        label: `${candidate.lastName} ${candidate.firstName}`
      }));      
    });

    this.addReactiveForm = this.formBuilder.group({
      candidate: ["", Validators.compose([Validators.required])],
      subject: [
        "",
        Validators.compose([
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(40),
        ]),
      ],
      supervisorPhone: [
        "",
        Validators.compose([Validators.pattern(this.phoneRegularExpression)]),
      ],
      supervisorEmail: [
        "",
        Validators.compose([
          Validators.required,
          Validators.pattern(this.mailRegularExpression),
        ]),
      ],
      type: ["", Validators.compose([Validators.required])],
      supervisor: [
        "",
        Validators.compose([
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(40),
          Validators.pattern(this.nameRegularExpression),
        ]),
      ],
      startDate: ["", Validators.compose([Validators.required])],
      endDate: ["", Validators.compose([Validators.required])],
      cin: [null, Validators.compose([Validators.required])],
      agreement: [null, Validators.compose([Validators.required])],
      assurance: [null, Validators.compose([Validators.required])],
    });

    this.sessionService.getSessionsByStatus("current").subscribe((data) => {
      if (data !== null) {
        this.selectedSession = data;
      }
    });
  }

  public addInternship(): void {

    if (!this.addReactiveForm.valid) {
      this.addReactiveForm.markAllAsTouched();
      return;
    }
    if (
      this.addReactiveForm.value.startDate > this.addReactiveForm.value.endDate
    ) {
      Swal.fire({
        title: "Invalid dates!",
        text: "Please note that the end date should always come after the start date",
        icon: "error",
        showConfirmButton: false,
        timer: 2500,
      });
      return;
    }
    this.cinFile = this.selectedFiles[0].item(0);
    this.agreementFile = this.selectedFiles[1].item(0);
    this.assuranceFile = this.selectedFiles[2].item(0);

    this.internshipService
      .add({
        subject: this.addReactiveForm.value.subject,
        startDate: this.addReactiveForm.value.startDate,
        endDate: this.addReactiveForm.value.endDate,
        type:
          this.addReactiveForm.value.type == "PFE"
            ? EInternshipType.TYPE_PFE
            : this.addReactiveForm.value.type == "PFA"
            ? EInternshipType.TYPE_PFA
            : EInternshipType.TYPE_STAGE,
        supervisor: this.addReactiveForm.value.supervisor,
        supervisorPhone: this.addReactiveForm.value.supervisorPhone,
        supervisorEmail: this.addReactiveForm.value.supervisorEmail,
        session: this.selectedSession,
        candidate: this.candidates.find(
          (candidate) =>
            candidate.candidateId ===
            parseInt(this.addReactiveForm.value.candidate.id)
        ),
      })
      .subscribe(
        (res: any) => {
          let internshipSaved = res;

          this.addReactiveForm.reset();
          this.cinProgress = 0;
          this.assuranceProgress = 0;
          this.agreementProgress = 0;
          Swal.fire({
            title: "Succes!",
            text: res.message,
            icon: "success",
            iconColor: "#6485C1",
            showConfirmButton: false,
            timer: 1500,
          });

          this.cinProgress = 0;
          this.uploader.upload(this.cinFile).subscribe({
            next: (fileUploaderRes) => {
              this.cinProgress = 100;
              this.docsService
                .add({
                  internship: internshipSaved,
                  path: fileUploaderRes.message,
                  type: "cin",
                })
                .subscribe();

              this.agreementProgress = 0;
              this.uploader.upload(this.agreementFile).subscribe({
                next: (fileUploaderRes) => {
                  this.agreementProgress = 100;

                  this.docsService
                    .add({
                      internship: internshipSaved,
                      path: fileUploaderRes.message,
                      type: "agreement",
                    })
                    .subscribe({
                      next: (fileUploaderRes) => {
                        this.assuranceProgress = 0;
                        this.uploader.upload(this.assuranceFile).subscribe({
                          next: (fileUploaderRes) => {
                            this.assuranceProgress = 100;

                            this.docsService
                              .add({
                                internship: internshipSaved,
                                path: fileUploaderRes.message,
                                type: "assurance",
                              })
                              .subscribe();
                          },
                        });
                      },
                    });
                },
              });
            },
          });

          this.activeModal.close(true);
        },
        (err) => {
          this.cinProgress = 0;
          this.assuranceProgress = 0;
          this.agreementProgress = 0;
          Swal.fire({
            title: "Error!",
            text: err.error?.message,
            icon: "error",
            showConfirmButton: false,
            timer: 2500,
          });
        }
      );
  }

  public onselectFile(e): void {
    if (e.target.name == "cin") {
      this.selectedFiles[0] = e.target.files;
    } else if (e.target.name == "agreement") {
      this.selectedFiles[1] = e.target.files;
    } else if (e.target.name == "assurance") {
      this.selectedFiles[2] = e.target.files;
    }
  }
  public closeModal() {
    this.activeModal.close(false);
  }
}
