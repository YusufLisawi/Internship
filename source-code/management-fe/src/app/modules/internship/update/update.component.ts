import { Component, Input, OnInit } from "@angular/core";
import {
  FormControl,
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from "@angular/forms";
import { Candidate } from "@models/Candidate.model";
import { Docs } from "@models/Docs.model";
import { Internship } from "@models/Internship.model";
import { EInternshipType } from "@models/InternshipType.enum";
import { Files } from "@models/files";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { CandidateService } from "@services/candidate.service";
import { DocsService } from "@services/docs.service";
import { InternshipService } from "@services/internship.service";
import { PdfService } from "@services/pdf.service";
import { UploaderService } from "@services/uploader.service";
import { log } from "console";
import Swal from "sweetalert2";

@Component({
  selector: "app-update",
  templateUrl: "./update.component.html",
  styleUrls: ["./update.component.scss"],
})
export class UpdateComponent implements OnInit {
  internships: Internship[] = [];
  files: Files[] = [];
  filename = "";
  editReactiveForm: UntypedFormGroup = this.formBuilder.group({
    candidate: [],
    subject: [""],
    type: [""],
    supervisor: [""],
    supervisorPhone: [""],
    supervisorEmail: [""],
    startDate: [""],
    endDate: [""],
    internshipStatus: [""],
    cin: [null],
    agreement: [null],
    assurance: [null],
    report: [null],
    internshipRating: [null],
    reportRating: [null],
  });

  public selectedRow: Internship = new Internship(
    "",
    null,
    null,
    null,
    null,
    null,
    ""
  );

  docsList: Docs[] = [];
  selectedFiles: FileList[] = [];
  certificateName;

  cinFile: File;
  agreementFile: File;
  assuranceFile: File;
  reportFile: File;
  certificateFile: File;
  cinProgress = 0;
  agreementProgress = 0;
  assuranceProgress = 0;
  reportProgress = 0;
  public src = "";

  phoneRegularExpression =
    "^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$";
  nameRegularExpression = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
  mailRegularExpression = "[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}";

  @Input() public UpdatedId: number;

  constructor(
    private internshipService: InternshipService,
    private candidateService: CandidateService,
    private docsService: DocsService,
    private uploader: UploaderService,
    private formBuilder: UntypedFormBuilder,
    private activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private pdfService: PdfService
  ) {}

  ngOnInit(): void {

    if (this.UpdatedId) {
      this.internshipService
        .getInternshipById(this.UpdatedId)
        .subscribe((res) => {
          this.docsService
            .getByInternshipId(res.internshipId)
            .subscribe((data) => {
              if (data.length != 0) {
                this.docsList = data;
              }
            });

          this.selectedRow = res;
          this.editReactiveForm = this.formBuilder.group({
            candidate: [res.candidate.lastName+" "+res.candidate.firstName],
            subject: [
              res.subject,
              Validators.compose([
                Validators.required,
                Validators.minLength(2),
                Validators.maxLength(40),
              ]),
            ],
            type: [
              res.type == EInternshipType.TYPE_PFE
                ? "PFE"
                : res.type == EInternshipType.TYPE_PFA
                ? "PFA"
                : "Stage",
              Validators.compose([Validators.required]),
            ],
            supervisor: [
              res.supervisor,
              Validators.compose([
                Validators.required,
                Validators.pattern(this.nameRegularExpression),
              ]),
            ],
            supervisorPhone: [
              res.supervisorPhone,
              Validators.compose([
                Validators.pattern(this.phoneRegularExpression),
              ]),
            ],
            supervisorEmail: [
              res.supervisorEmail,
              Validators.compose([
                Validators.required,
                Validators.pattern(this.mailRegularExpression),
              ]),
            ],
            startDate: [
              res.startDate,
              Validators.compose([Validators.required]),
            ],
            endDate: [res.endDate, Validators.compose([Validators.required])],
            internshipStatus: [res.internshipStatus],
            cin: [null],
            agreement: [null],
            assurance: [null],
            report: [null],
            internshipRating: [
              res.internshipRating,
              Validators.compose([Validators.max(20), Validators.min(0)]),
            ],
            reportRating: [
              res.reportRating,
              Validators.compose([Validators.max(20), Validators.min(0)]),
            ],
          });
        });
        this.uploader.findById(this.UpdatedId).subscribe({
          next: fileUploaderRes => {
            this.filename=fileUploaderRes.message
          }
        });  
    }

  }

  public updateInternship(): void {
    let internshipToUpdate: Internship = {
      internshipId: this.selectedRow.internshipId,

      subject: this.editReactiveForm.value.subject
        ? this.editReactiveForm.value.subject
        : this.selectedRow.subject,

      startDate: this.editReactiveForm.value.startDate
        ? this.editReactiveForm.value.startDate
        : this.selectedRow.startDate,

      endDate: this.editReactiveForm.value.endDate
        ? this.editReactiveForm.value.endDate
        : this.selectedRow.endDate,

      type: this.editReactiveForm.value.type
        ? this.editReactiveForm.value.type == "PFE"
          ? EInternshipType.TYPE_PFE
          : this.editReactiveForm.value.type == "PFA"
          ? EInternshipType.TYPE_PFA
          : EInternshipType.TYPE_STAGE
        : this.selectedRow.type,

      supervisor: this.editReactiveForm.value.supervisor
        ? this.editReactiveForm.value.supervisor
        : this.selectedRow.supervisor,

      supervisorPhone: this.editReactiveForm.value.supervisorPhone,

      supervisorEmail: this.editReactiveForm.value.supervisorEmail
        ? this.editReactiveForm.value.supervisorEmail
        : this.selectedRow.supervisorEmail,

      session: this.selectedRow.session,
      candidate: this.selectedRow.candidate,
      internshipStatus: this.selectedRow.internshipStatus,

      internshipRating:
        this.isRatable() && this.editReactiveForm.value.internshipRating
          ? this.editReactiveForm.value.internshipRating
          : this.selectedRow.internshipRating,
      reportRating:
        this.isRatable() && this.editReactiveForm.value.reportRating
          ? this.editReactiveForm.value.reportRating
          : this.selectedRow.reportRating,
    };
    if (
      this.editReactiveForm.value.startDate >
      this.editReactiveForm.value.endDate
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

    if (
      internshipToUpdate.internshipRating &&
      internshipToUpdate.reportRating
    ) {
      internshipToUpdate.internshipStatus = "rated";
    } else if (this.editReactiveForm.value.report) {
      internshipToUpdate.internshipStatus = "completed";
    }

    if (this.selectedFiles[0]) {
      this.cinFile = this.selectedFiles[0].item(0);

      this.cinProgress = 0;
      this.uploader.upload(this.cinFile).subscribe({
        next: (fileUploaderRes) => {
          this.cinProgress = 100;
          this.docsService
            .update({
              docsId: this.docsList.filter((doc) => doc.type === "cin")[0]
                .docsId,
              path: fileUploaderRes.message,
              type: "cin",
              internship: this.selectedRow,
            })
            .subscribe();
        },
      });
    }
    if (this.selectedFiles[1]) {
      this.agreementFile = this.selectedFiles[1].item(0);

      this.agreementProgress = 0;
      this.uploader.upload(this.agreementFile).subscribe({
        next: (fileUploaderRes) => {
          this.agreementProgress = 100;

          this.docsService
            .update({
              docsId: this.docsList.filter((doc) => doc.type === "agreement")[0]
                .docsId,
              path: fileUploaderRes.message,
              type: "agreement",
              internship: this.selectedRow,
            })
            .subscribe();
        },
      });
    }
    if (this.selectedFiles[2]) {
      this.assuranceFile = this.selectedFiles[2].item(0);
      this.assuranceProgress = 0;
      this.uploader.upload(this.assuranceFile).subscribe({
        next: (fileUploaderRes) => {
          this.assuranceProgress = 100;

          this.docsService
            .update({
              docsId: this.docsList.filter((doc) => doc.type === "assurance")[0]
                .docsId,
              path: fileUploaderRes.message,
              type: "assurance",
              internship: this.selectedRow,
            })
            .subscribe();
        },
      });
    }
    if (this.selectedFiles[3]) {
      this.reportFile = this.selectedFiles[3].item(0);
      this.reportProgress = 0;

      if (this.docsList.filter((doc) => doc.type === "report")[0]) {
        this.uploader.upload(this.reportFile).subscribe({
          next: (fileUploaderRes) => {
            this.assuranceProgress = 100;

            this.docsService
              .update({
                docsId: this.docsList.filter((doc) => doc.type === "report")[0]
                  .docsId,
                path: fileUploaderRes.message,
                type: "report",
                internship: this.selectedRow,
              })
              .subscribe();
          },
        });
      } else {
        this.uploader.upload(this.reportFile).subscribe({
          next: (fileUploaderRes) => {
            this.assuranceProgress = 100;

            this.docsService
              .add({
                path: fileUploaderRes.message,
                type: "report",
                internship: this.selectedRow,
              })
              .subscribe();
          },
        });
      }
    }

    this.internshipService.update(internshipToUpdate).subscribe(() => {
      Swal.fire({
        title: "Succes!",
        text: "Candidate updated successfully",
        icon: "success",
        iconColor: "#6485C1",
        showConfirmButton: false,
        timer: 1500,
      });
      this.activeModal.close(true);
    });
  }

  public onselectFile(e): void {
    if (e.target.name == "cin") {
      this.selectedFiles[0] = e.target.files;
    } else if (e.target.name == "agreement") {
      this.selectedFiles[1] = e.target.files;
    } else if (e.target.name == "assurance") {
      this.selectedFiles[2] = e.target.files;
    } else if (e.target.name == "report") {
      this.selectedFiles[3] = e.target.files;
    }
  }

  getFileSelected(fileName: string) {
    switch (fileName) {
      case "cin":
        if (this.selectedFiles[0] && this.selectedFiles[0]?.length) {
          return this.selectedFiles[0][0].name;
        } else {
          let file = this.docsList.filter((doc) => doc.type === "cin")[0];
          let filename = file?.path;
          if (filename && filename?.indexOf("_") != -1) {
            filename = filename.substring(filename.indexOf("_") + 1);
          }
          return filename;
        }
      case "agreement":
        if (this.selectedFiles[1] && this.selectedFiles[1]?.length) {
          return this.selectedFiles[1][0].name;
        } else {
          let filename = this.docsList.filter(
            (doc) => doc.type === "agreement"
          )[0]?.path;
          if (filename && filename?.indexOf("_") != -1) {
            filename = filename.substring(filename.indexOf("_") + 1);
          }
          return filename;
        }
      case "assurance":
        if (this.selectedFiles[2] && this.selectedFiles[2]?.length) {
          return this.selectedFiles[2][0].name;
        } else {
          let filename = this.docsList.filter(
            (doc) => doc.type === "assurance"
          )[0]?.path;
          if (filename && filename?.indexOf("_") != -1) {
            filename = filename.substring(filename.indexOf("_") + 1);
          }
          return filename;
        }
      case "report":
        if (this.selectedFiles[3] && this.selectedFiles[3]?.length) {
          return this.selectedFiles[3][0].name;
        } else {
          let filename = this.docsList.filter((doc) => doc.type === "report")[0]
            ?.path;
          if (filename && filename?.indexOf("_") != -1) {
            filename = filename.substring(filename.indexOf("_") + 1);
          }
          return filename;
        }
    }
  }

  isRatable() {
    return this.editReactiveForm.value.report ||
      this.docsList.filter((doc) => doc.type === "report")[0]
      ? true
      : false;
  }

  isDownloadable() {
    return this.isInternshipEnd() &&
      this.isRatable() &&
      (this.editReactiveForm.value.reportRating ||
        this.selectedRow.reportRating) &&
      (this.editReactiveForm.value.internshipRating ||
        this.selectedRow.internshipRating)
      ? true
      : false;
  }

  DownloadCertificate(){
    if(!this.filename){
      this.certificateFile =   this.pdfService.exportPDFCertificate(this.selectedRow)
      this.uploader.upload(this.certificateFile).subscribe({
        next: fileUploaderRes => {
          this.filename=fileUploaderRes.message
          Swal.fire({
            title: 'Succes!',
            text: 'PDF file dowloaded successfully',
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          })
        },
        error: err => {
          Swal.fire({
            title: 'Error!',
            text: err.error,
            icon: 'error',
            showConfirmButton: false,
            timer: 2500
          });
        }
      });
    }else{
      this.uploader.find(this.filename).subscribe(
        response => {
          const blob = new Blob([response], { type: 'application/pdf' });
          const downloadURL = URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = this.filename;
          link.click();
          Swal.fire({
            title: 'Succes!',
            text: 'PDF file dowloaded successfully',
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          })
        },
        err => {
          Swal.fire({
            title: 'Error!',
            text: err.error,
            icon: 'error',
            showConfirmButton: false,
            timer: 2500
          });
        }
      );
    }
   
  }
  

  isInternshipEnd() {
    let d = new Date();
    var now = new Date(d.getFullYear(), d.getMonth(), d.getDate());
    let endDate: Date = new Date(this.selectedRow.endDate);
    if (endDate < now) return true;
    else return false;
  }

  public closeModal() {
    this.activeModal.close(false);
  }

}
