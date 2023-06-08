import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Recruiter } from '@models/Recruiter.model';
import { RecruiterService } from '@services/recruiter.service';
import Swal from 'sweetalert2';
import { ERole } from '@models/Role.enum';
import { Role } from '@models/Role.model';

@Component({
  selector: 'app-add',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {

  public errorMessage = '';
  public roles: Role[];
  public activeRoles: ERole[] = [];
  public showPassword: boolean;
  public showPasswordOnPress: boolean;
  public recruiter: Recruiter;
  public reactiveForm: UntypedFormGroup = this.formBuilder.group({
    'email': ['', Validators.compose([Validators.required, Validators.pattern('[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}'), Validators.maxLength(60)])],
    'firstName': ['', Validators.compose([Validators.required, Validators.pattern('^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$'), Validators.minLength(3), Validators.maxLength(30)])],
    'lastName': ['', Validators.compose([Validators.required, Validators.pattern('^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$'), Validators.minLength(3), Validators.maxLength(30)])],
    'phoneNumber': ['', Validators.compose([Validators.required, Validators.pattern('^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$')])],
    'password': ['', Validators.compose([Validators.required])],
    'userName': ['', Validators.compose([Validators.required])]
  });
  public id: number;
  public formSubmited: boolean = false;

  constructor(private activeModal: NgbActiveModal, private formBuilder: UntypedFormBuilder, private recruiterService: RecruiterService) { }

  ngOnInit(): void {
    this.roles = this.recruiterService.roles;
    if (this.id) {
      this.recruiterService.getById(this.id).subscribe(
        res => {
          this.recruiter = res;
          this.reactiveForm = this.formBuilder.group({
            'email': [res.email, Validators.compose([Validators.required, Validators.pattern('[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}'), Validators.maxLength(60)])],
            'firstName': [res.firstName, Validators.compose([Validators.required, Validators.pattern('^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$'), Validators.minLength(3), Validators.maxLength(30)])],
            'lastName': [res.lastName, Validators.compose([Validators.required, Validators.pattern('^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$'), Validators.minLength(3), Validators.maxLength(30)])],
            'phoneNumber': [res.phoneNumber, Validators.compose([Validators.required, Validators.pattern('^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$')])],
            'password': [res.password],
            'userName': [res.username, Validators.compose([Validators.required])]
          });
          this.activeRoles = res.role.map(r => ERole[r.name]);
          for (let role in this.roles) {
            let input: any = document.getElementById(this.roles[role].name);
            if(input){
              if (input) {
                if (this.activeRoles.includes(ERole[this.roles[role].name])) {
                  input.checked = true;
                }
                else{
                  input.checked = false;
                }
              }
            }
            else{
              let timeOut = setTimeout(()=>{
                input = document.getElementById(this.roles[role].name);
                if (input) {
                  if (this.activeRoles.includes(ERole[this.roles[role].name])) {
                    input.checked = true;
                  }
                  else{
                    input.checked = false;
                  }
                }
                clearTimeout(timeOut);
              },500);
            }
          }
        }
      )
    }
  }

  public actionRecruiter() {
    this.formSubmited = true;
    if (this.reactiveForm.valid && this.activeRoles.length) {
      if (this.id) {
        let recruiter: Recruiter = {
          "recruiterId": this.id,
          "email": this.reactiveForm.value.email,
          "username": this.reactiveForm.value.userName,
          "password": this.reactiveForm.value.password,
          "firstName": this.reactiveForm.value.firstName,
          "lastName": this.reactiveForm.value.lastName,
          "phoneNumber": this.reactiveForm.value.phoneNumber,
          "picture": this.recruiter.picture
        };
        let finalRoles = [];
        for(let role in this.activeRoles){
          finalRoles.push(this.roles.filter(rol=> rol.name == ERole[this.activeRoles[role]])[0]);
        }
        recruiter.role = finalRoles;

        this.recruiterService.update(recruiter).subscribe(
          (data: any) => {
            Swal.fire({
              title: 'Succes!',
              text: data.message,
              icon: 'success',
              iconColor: '#6485C1',
              showConfirmButton: false,
              timer: 1500
            })
            this.reactiveForm.reset();
            this.closeModal();
            //document.getElementById('cheat').click();
          },
          err => {
            Swal.fire({
              title: 'Oops !!',
              text: err.error.message,
              icon: 'error',
              showConfirmButton: false,
              timer: 2000
            })
          }
        );
      }
      else {
        this.recruiterService.add(
          {
            "email": this.reactiveForm.value.email,
            "username": this.reactiveForm.value.userName,
            "firstName": this.reactiveForm.value.firstName,
            "lastName": this.reactiveForm.value.lastName,
            "password": this.reactiveForm.value.password,
            "phoneNumber": this.reactiveForm.value.phoneNumber,
            "eRole": this.activeRoles,
            "picture": null
          }
        ).subscribe(
          (data: any) => {
            Swal.fire({
              title: 'Succes!',
              text: data.message,
              icon: 'success',
              iconColor: '#6485C1',
              showConfirmButton: false,
              timer: 1500
            })
            document.getElementById('cheat').click();
            this.reactiveForm.reset();
          },
          err => {
            if(err.error.message=="Error: Email is already in use!"){
              Swal.fire({
                title: 'Email is already in use !',
                text : "Do you want to restore the account instead ?",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#6485C1',
                cancelButtonColor: '#808080',
                confirmButtonText: 'Just Do it !'
              }).then((result) => {
                if (result.isConfirmed) {
                  this.recruiterService.restoreByEmail(this.reactiveForm.value.email).subscribe(
                    res => {
                      this.activeModal.close();
                      Swal.fire({
                        title: 'Succes!',
                        text: res.message,
                        icon: 'success',
                        iconColor: '#6485C1',
                        showConfirmButton: false,
                        timer: 1500
                      });
                    }
                  );
                }
              });
            }
            else{
              Swal.fire({
                title: 'Oops !!',
                text: err.error.message,
                icon: 'error',
                showConfirmButton: false,
                timer: 2000
              })
            }
          }
        );
      }
    }
  }

  public changeRole(role: Role, isChecked: boolean): void {
    if (isChecked) {
      this.activeRoles.push(ERole[role.name]);
    }
    else {
      this.activeRoles = this.activeRoles.filter(r => r !== ERole[role.name]);
    }
  }

  public closeModal() {
    this.activeModal.close();
  }

}
