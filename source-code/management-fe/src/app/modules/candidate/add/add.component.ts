import { Component, Input, OnInit } from "@angular/core";
import {
  UntypedFormGroup,
  UntypedFormBuilder,
  Validators,
  FormGroup,
  FormControl,
  FormBuilder,
} from "@angular/forms";
import { UploaderService } from "@services/uploader.service";
import { CandidateService } from "@services/candidate.service";
import { Session } from "src/app/models/Session.model";
import Swal from "sweetalert2";
import { Candidate } from "@models/Candidate.model";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NewOptionComponent } from "../new-option/new-option.component";
import { SessionService } from "@services/session.service";

@Component({
  selector: "app-add",
  templateUrl: "./add.component.html",
  styleUrls: ["./add.component.scss"],
})
export class AddComponent implements OnInit {
  addReactiveForm: UntypedFormGroup;
  nameRegularExpression = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
  mailRegularExpression = "[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}";
  phoneRegularExpression = "^06\\d{2}\\d{2}\\d{2}\\d{2}$";
  cityRegularExpression = "^[a-zA-Z]+(?:'[a-zA-Z]+)*$";
  @Input() selectedSession: Session;

  public candidates: Candidate[] = [];
  public candidatesPres: Candidate[] = [];
  public candidatesFiltered: Candidate[] = [];
  public universities: String[] = [];
  public diplomas: String[] = ["BAC+2", "BAC+3", "BAC+5", "BAC+8"];
  public cities: String[] = [];

  collectionSize: number;
  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  showOtherCity = false;
  src = "";

  constructor(
    private candidateService: CandidateService,
    private sessionService: SessionService,
    private uploader: UploaderService,
    private formBuilder: UntypedFormBuilder,
    private activeModal: NgbActiveModal,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.addReactiveForm = this.formBuilder.group({
      firstName: [
        "",
        Validators.compose([
          Validators.required,
          Validators.pattern(this.nameRegularExpression),
          Validators.minLength(3),
          Validators.maxLength(30),
        ]),
      ],
      lastName: [
        "",
        Validators.compose([
          Validators.required,
          Validators.pattern(this.nameRegularExpression),
          Validators.minLength(3),
          Validators.maxLength(30),
        ]),
      ],
      email: [
        "",
        Validators.compose([
          Validators.required,
          Validators.pattern(this.mailRegularExpression),
          Validators.maxLength(60),
        ]),
      ],
      gender: ["", Validators.compose([Validators.required])],
      diplome: ["", Validators.compose([Validators.required])],
      anapec: ["", Validators.compose([Validators.required])],
      phone: [
        "",
        Validators.compose([
          Validators.required,
          Validators.pattern(this.phoneRegularExpression),
        ]),
      ],
      city: [
        "",
        Validators.compose([
          Validators.required,
          Validators.pattern(this.cityRegularExpression),
          Validators.minLength(3),
          Validators.maxLength(30),
        ]),
      ],
      cv: ["", Validators.compose([Validators.required])],
      university: [null, Validators.compose([Validators.required])],
    });

    this.candidateService.getUniversities().subscribe(
      (data: String[]) => {
        this.universities = data;
      },
      () => {}
    );

    this.sessionService.getSessionsByStatus("current").subscribe((data) => {
      if (data !== null) {
        this.selectedSession = data;
      }
    });

    this.candidateService.getCities().subscribe(
      (data: String[]) => {
        this.cities = data;
      },
      () => {}
    );
  }

  public addCandidate(): void {
    if (!this.addReactiveForm.valid) {
      this.addReactiveForm.markAllAsTouched();
      return;
    }
    this.progress = 0;
    this.currentFile = this.selectedFiles.item(0);

    this.uploader.upload(this.currentFile).subscribe({
      next: (fileUploaderRes) => {
        this.progress = 100;

        this.candidateService
          .add({
            firstName: this.addReactiveForm.value.firstName,
            lastName: this.addReactiveForm.value.lastName,
            email: this.addReactiveForm.value.email,
            phoneNumber: this.addReactiveForm.value.phone,
            city: this.addReactiveForm.value.city,
            anapec: this.addReactiveForm.value.anapec,
            gender: this.addReactiveForm.value.gender,
            diplome: this.addReactiveForm.value.diplome,
            university: this.addReactiveForm.value.university,
            session: this.selectedSession,
            cvname: fileUploaderRes.message,
          })
          .subscribe(
            (res) => {
              this.progress = 0;
              this.addReactiveForm.reset();
              Swal.fire({
                title: "Succes!",
                text: res.message,
                icon: "success",
                iconColor: "#6485C1",
                showConfirmButton: false,
                timer: 1500,
              });
              this.activeModal.close(true);
            },
            (err) => {
              Swal.fire({
                title: "Error!",
                text: err.error?.message,
                icon: "error",
                showConfirmButton: false,
                timer: 2500,
              });
            }
          );
      },
      error: (err) =>
        Swal.fire({
          title: "Error!",
          text: err.error.message,
          icon: "error",
          showConfirmButton: false,
          timer: 2500,
        }),
    });
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

  public closeModal() {
    this.activeModal.close(false);
  }
  optionValueChanged(optionName: string, value: string) {
    if (value == "Other") {
      this.addReactiveForm.get(optionName).setValue(null);
      const modalRef = this.modalService.open(NewOptionComponent);
      modalRef.componentInstance.optionName = optionName;
      modalRef.result.then((result) => {
        if (result) {
          switch (optionName) {
            case "university":
              this.universities.push(result);
              this.addReactiveForm.get(optionName).setValue(result);
              break;
            case "city":
              const isValidCity = new RegExp(this.cityRegularExpression).test(
                result
              );
              if (isValidCity) {
                this.cities.push(result);
                this.addReactiveForm.get(optionName).setValue(result);
              } else {
                const index = this.cities.indexOf(result);
                if (index !== -1) {
                  this.cities.splice(index, 1);
                }
                this.addReactiveForm.get(optionName).setValue(null);
              }
              break;
          }
        }
      });
    }
  }
}
