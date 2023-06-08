import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TokenStorageService } from '@services/token-storage.service';
import { RecruiterService } from '@services/recruiter.service';
import { FormComponent } from '../form/form.component';
import { Recruiter } from '@models/Recruiter.model';
import { ToastrService } from 'ngx-toastr';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { UploaderService } from '@services/uploader.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  state: any;
  currentUser: Recruiter;
  imageUrl: any;
  image: File;
  baseUrl: string = environment.apiUrl + 'file/find/';
  msg = "";
  public showPassword: boolean;
  public showPasswordOnPress: boolean;
  public imageChanged: boolean = false;
  public formSubmited: boolean = false;
  public currentUserForm: UntypedFormGroup = this.formBuilder.group({ 'userName': [''], 'password': [''], 'firstName': [''], 'lastName': [''], 'email': [''], 'phoneNumber': [''] });

  constructor(private recruiterService: RecruiterService, private uploaderService: UploaderService, private tokenStorageService: TokenStorageService, private formBuilder: UntypedFormBuilder, private modalService: NgbModal, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.currentUser = this.tokenStorageService.getUser()
    this.recruiterService.getById(this.currentUser.recruiterId)
      .subscribe(
        res => {
          this.currentUser = res;
          if (res.picture) {
            this.imageUrl = this.baseUrl + res.picture;
          }
          this.currentUserForm = this.formBuilder.group({
            'userName': [res.username, Validators.compose([Validators.required])],
            'password': [''],
            'firstName': [res.firstName, Validators.compose([Validators.required, Validators.pattern('^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$'), Validators.minLength(3), Validators.maxLength(30)])],
            'lastName': [res.lastName, Validators.compose([Validators.required, Validators.pattern('^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$'), Validators.minLength(3), Validators.maxLength(30)])],
            'email': [res.email, Validators.compose([Validators.required, Validators.pattern('[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}'), Validators.maxLength(60)])],
            'phoneNumber': [res.phoneNumber, Validators.compose([Validators.required, Validators.pattern('^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$')])]
          });
        },
        err => this.toastr.error(err)
      );
  }

  updateUser() {
    this.formSubmited = true;

    if (this.currentUserForm.valid) {
      this.currentUser.firstName = this.currentUserForm.value.firstName;
      this.currentUser.lastName = this.currentUserForm.value.lastName;
      this.currentUser.email = this.currentUserForm.value.email;
      this.currentUser.phoneNumber = this.currentUserForm.value.phoneNumber;
      if (this.currentUserForm.value.password) {
        this.currentUser.password = this.currentUserForm.value.password;
      }

      if (this.imageChanged) {
        if (this.image) {
          this.uploaderService.upload(this.image).subscribe(
            res => {
              this.currentUser.picture = res.message;
              this.imageUrl = this.baseUrl + res.message;
              this.recruiterService.update(this.currentUser).subscribe(
                () => {
                  this.toastr.success('Picture updated successfuly');
                  this.formSubmited = false;
                  this.imageChanged = false;
                  this.currentUserForm.controls.password.reset();
                }
              );
            },
            () => {
              this.toastr.error('The picture can\'t be uploaded !!');
              this.formSubmited = false;
              this.imageChanged = false;
            }
          );
        }
        else {
          this.recruiterService.update(this.currentUser).subscribe(
            () => {
              this.toastr.success('Picture updated successfuly');
              this.formSubmited = false;
              this.imageChanged = false;
              this.currentUserForm.controls.password.reset();
            },
            () => {
              this.toastr.error('The picture can\'t be uploaded !!');
              this.formSubmited = false;
              this.imageChanged = false;
            }
          );

        }
      }
      else {
        this.recruiterService.update(this.currentUser).subscribe(
          () => {
            this.toastr.success('Profile updated successfuly');
            this.formSubmited = false;
            this.currentUserForm.controls.password.reset();
          },
          () => {
            this.toastr.error('Can\'t update the profile !!');
            this.formSubmited = false;
          }
        );
      }

    }
    else {
      this.formSubmited = false;
      this.toastr.error('Fill the required blanks !!');
    }
  }

  selectFile(event: any) {
    if (!event.target.files[0] || event.target.files[0].length == 0) {
      this.msg = "<i class='fa fa-exclamation-triangle'></i> You must select an image";
      return;
    }

    var mimeType = event.target.files[0].type;

    if (mimeType.match(/image\/*/) == null) {
      this.msg = "<i class='fa fa-exclamation-triangle'></i> Only images are supported";
      return;
    }

    var reader = new FileReader();
    this.image = event.target.files[0];
    reader.readAsDataURL(event.target.files[0]);

    reader.onload = (_event) => {
      this.msg = "";
      this.imageUrl = reader.result;
      this.imageChanged = true;
    }
  }

  deletePicture() {
    this.imageUrl = null;
    this.currentUser.picture = "";
    this.image = null;
    this.imageChanged = true;
  }

  openModal() {
    const modalRef = this.modalService.open(FormComponent);
    modalRef.result.then((result) => { }).catch((error) => { });
  }

}
