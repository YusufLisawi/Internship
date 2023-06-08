import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { Candidate } from '@models/Candidate.model';
import { Level } from '@models/Level.enum';
import { ERole } from '@models/Role.enum';
import { Role } from '@models/Role.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CandidateService } from '@services/candidate.service';
import { RecruiterService } from '@services/recruiter.service';
import { UploaderService } from '@services/uploader.service';
import { environment } from 'src/environments/environment';
import Swal from 'sweetalert2';
import { NewOptionComponent } from '../new-option/new-option.component';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit {


  editReactiveForm: UntypedFormGroup = this.formBuilder.group({
    'firstName': [''],
    'lastName': [''],
    'email': [''],
    'gender': [''],
    'diplome': [''],
    'anapec': [''],
    'phone': [''],
    'city': [''],
    'cv': [''],
    'university': ['']
  });
  nameRegularExpression = '^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$';
  mailRegularExpression = '[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}';
  phoneRegularExpression = '^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$';

  public roles: Role[];
  public activeRoles: ERole[] = [];
  public universities: String[] = [];
  public diplomas: String[] = ["BAC+2", "BAC+3", "BAC+5", "BAC+8"];
  public cities: String[] = [];
  public selectedRow: Candidate = new Candidate('', '', '', '', '');

  private selectedFiles: FileList;
  public currentFile: File;
  public src = "";
  public progress = 0;

  @Input() public UpdatedId: number;
  constructor(private candidateService: CandidateService, private recruiterService: RecruiterService, private uploader: UploaderService, private formBuilder: UntypedFormBuilder, private activeModal: NgbActiveModal, private modalService: NgbModal, private downloadServ: UploaderService) { }

  ngOnInit(): void {
    this.candidateService.getUniversities().subscribe(
      (data: String[]) => {
        this.universities = data;
      }
    );
    this.candidateService.getCities().subscribe(
      (data: String[]) => {
        this.cities = data;
      }
    );

    this.roles = this.recruiterService.roles;

    if (this.UpdatedId) {
      this.candidateService.getCandidateById(this.UpdatedId).subscribe(
        res => {
          this.selectedRow = res;
          this.editReactiveForm = this.formBuilder.group({
            'firstName': [res.firstName, Validators.compose([Validators.required, Validators.pattern(this.nameRegularExpression), Validators.minLength(3), Validators.maxLength(30)])],
            'lastName': [res.lastName, Validators.compose([Validators.required, Validators.pattern(this.nameRegularExpression), Validators.minLength(3), Validators.maxLength(30)])],
            'email': [res.email, Validators.compose([Validators.required, Validators.pattern(this.mailRegularExpression), Validators.maxLength(60)])],
            'gender': [res.gender, Validators.compose([Validators.required])],
            'diplome': [res.diplome, Validators.compose([Validators.required])],
            'anapec': [res.anapec, Validators.compose([Validators.required])],
            'phone': [res.phoneNumber, Validators.compose([Validators.required, Validators.pattern(this.phoneRegularExpression)])],
            'city': [res.city, Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
            'cv': [null, Validators.compose([Validators.required])],
            'university': [res.university, Validators.compose([Validators.required])],
          });

          if (res.cvname) {
            this.src = environment.apiUrl + 'file/find/' + res.cvname;
          }
        }
      );
    }
  }

  public updateCandidate(): void {
    this.currentFile = this.selectedFiles?.item(0);
    this.progress = 0;
    let candidateToUpdate: Candidate = {
      "candidateId": this.selectedRow.candidateId,
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
      this.closeModal();
    }
    else{
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

  public closeModal() {
    this.activeModal.close(false);
  }

  optionValueChanged(optionName: string, value: string) {
    if (value == "Other") {
      this.editReactiveForm.get(optionName).setValue(null);
      const modalRef = this.modalService.open(NewOptionComponent);
      modalRef.componentInstance.optionName = optionName;
      modalRef.result.then((result) => {
        if (result) {
          switch (optionName) {
            case "university":
              this.universities.push(result);
              this.editReactiveForm.get(optionName).setValue(result);
              break;
            case "city":
              this.cities.push(result);
              this.editReactiveForm.get(optionName).setValue(result);
              break;
          }
        }
      });
    }
  }
}
