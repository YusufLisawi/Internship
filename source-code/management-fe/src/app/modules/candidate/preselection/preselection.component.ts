import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { Candidate } from '@models/Candidate.model';
import { Level } from '@models/Level.enum';
import { ERole } from '@models/Role.enum';
import { Role } from '@models/Role.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CandidateService } from '@services/candidate.service';
import { RecruiterService } from '@services/recruiter.service';
import { UploaderService } from '@services/uploader.service';
import { environment } from 'src/environments/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-preselection',
  templateUrl: './preselection.component.html',
  styleUrls: ['./preselection.component.scss']
})
export class PreselectionComponent implements OnInit {

  @Output() public isChanged : EventEmitter<any> = new EventEmitter();
  editReactiveForm: UntypedFormGroup;
  nameRegularExpression = '^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$';
  mailRegularExpression = '[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}';
  phoneRegularExpression = '^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$';

  public roles: Role[];
  public activeRoles: ERole[] = [];
  public candidatesPres: Candidate[] = [];
  public selectedRow: Candidate = new Candidate('', '', '', '', '');
  public searchText: string;
  public preselectionSearch: string = "";
  public universities: String[] = [];
  public cities: String[] = [];
  private selectedFiles: FileList;
  public currentFile: File;
  public src = "";
  public progress = 0;

  constructor(private candidateService: CandidateService, private uploader: UploaderService, private formBuilder: UntypedFormBuilder, private activeModal: NgbActiveModal) { }

  ngOnInit(): void {
    this.refreshCandidates();
    this.editReactiveForm = this.formBuilder.group({
      'firstName': [this.selectedRow.firstName, Validators.compose([Validators.required, Validators.pattern(this.nameRegularExpression), Validators.minLength(3), Validators.maxLength(30)])],
      'lastName': [this.selectedRow.lastName, Validators.compose([Validators.required, Validators.pattern(this.nameRegularExpression), Validators.minLength(3), Validators.maxLength(30)])],
      'email': [this.selectedRow.email, Validators.compose([Validators.required, Validators.pattern(this.mailRegularExpression), Validators.maxLength(60)])],
      'gender': [this.selectedRow.gender, Validators.compose([Validators.required])],
      'diplome': [this.selectedRow.diplome, Validators.compose([Validators.required])],
      'anapec': [this.selectedRow.anapec, Validators.compose([Validators.required])],
      'phone': [this.selectedRow.phoneNumber, Validators.compose([Validators.required, Validators.pattern(this.phoneRegularExpression)])],
      'city': [this.selectedRow.city, Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
      'cv': [null, Validators.compose([Validators.required])],
      'university': [this.selectedRow.university, Validators.compose([Validators.required])],
    });

    this.candidateService.getUniversities().subscribe(
      (data: String[]) => {
        this.universities = data;
        this.universities.push("Other");
      }
    );
    this.candidateService.getCities().subscribe({
      next: (data: String[]) => {
        this.cities = data;
        this.cities.push("Other");
      }
    });
  }

  public editCandidate(candidate: Candidate): void {
    this.selectedRow = candidate;
    this.src = `${environment.apiUrl}file/find/${candidate.cvname}`;
  }

  public updateCandidate(): void {
    this.currentFile = this.selectedFiles.item(0);
    this.progress = 0;
    let candidateToUpdate: Candidate = {
      "firstName": this.editReactiveForm.value.firstName ? this.editReactiveForm.value.firstName : this.selectedRow.firstName,
      "lastName": this.editReactiveForm.value.lastName ? this.editReactiveForm.value.lastName : this.selectedRow.lastName,
      "email": this.editReactiveForm.value.email ? this.editReactiveForm.value.email : this.selectedRow.email,
      "phoneNumber": this.editReactiveForm.value.phone ? this.editReactiveForm.value.phone : this.selectedRow.phoneNumber,
      "city": this.editReactiveForm.value.city ? this.editReactiveForm.value.city : this.selectedRow.city,
      "anapec": this.editReactiveForm.value.anapec ? this.editReactiveForm.value.anapec : this.selectedRow.anapec,
      "gender": this.editReactiveForm.value.gender ? this.editReactiveForm.value.gender : this.selectedRow.gender,
      "diplome": this.editReactiveForm.value.diplome ? this.editReactiveForm.value.diplome : this.selectedRow.diplome,
      "university": this.editReactiveForm.value.university ? this.editReactiveForm.value.university : this.selectedRow.university,
      "cvname": this.selectedRow.cvname
    };

    if (this.currentFile) {
      this.uploader.upload(this.currentFile).subscribe({
        next: fileUploaderRes => {
          this.progress = 100;
          candidateToUpdate.cvname = fileUploaderRes?.message || this.selectedRow.cvname;
          this.candidateService.update(candidateToUpdate).subscribe(
            () => {
              Swal.fire({
                title: 'Succes!',
                text: 'Candidate updated successfully',
                icon: 'success',
                iconColor: '#6485C1',
                showConfirmButton: false,
                timer: 1500
              });
              this.activeModal.close(true);
            });
        },
        error: err => Swal.fire({
          title: 'Error!',
          text: err.error.message,
          icon: 'error',
          showConfirmButton: false,
          timer: 2500
        })
      });
    }
    else {
      this.candidateService.update(candidateToUpdate).subscribe(
        () => {
          Swal.fire({
            title: 'Succes!',
            text: 'Candidate updated successfully',
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          });
          this.activeModal.close(true);
        });
    }
  }

  public changePreselectionStatus(status: boolean): void {
    if (this.selectedRow.candidateId != null) {
      this.selectedRow.preselectionStatus = status ? "true" : "false";
      this.candidateService.setPreselectionStatus(this.selectedRow.candidateId, status).subscribe(
        (res) => {
          this.refreshCandidates();
          this.isChanged.emit(true);
          Swal.fire({
            title: status ? "Candidate Admited!" : "Candidate Rejected!",
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

  public onselectFile(e): void {
    this.selectedFiles = e.target.files;

    if (e.target.files) {
      var reader = new FileReader();
      reader.readAsDataURL(e.target.files[0]);
      reader.onload = (event: any) => {
        this.src = event.target.result;
      }
    }
  }

  public refreshCandidates(): void {
    this.candidateService.getAll().subscribe((data: Candidate[]) => {
      this.candidatesPres = data;
    });
  }

  public closeModal(): void {
    this.activeModal.close('Modal Closed');
  }
}
