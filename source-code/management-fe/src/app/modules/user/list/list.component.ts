import { Component, OnInit } from '@angular/core';
import { Recruiter } from 'src/app/models/Recruiter.model';
import { RecruiterService } from '@services/recruiter.service';
import { Role } from '@models/Role.model';
import Swal from 'sweetalert2';
import { Location } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormComponent } from '../form/form.component';
import { AuthService } from '@services/auth.service';
import { TokenStorageService } from '@services/token-storage.service';
import { ArchivedUsersModalComponent } from '../archived-users-modal/archived-users-modal.component';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  collectionSize: number;
  page = 1;
  pageSize = 5;
  public users: Recruiter[] = [];
  public usersFiltered: Recruiter[] = [];
  private currentUserUsername: string;

  constructor(private rectuiterService: RecruiterService, private tokenService: TokenStorageService, private location: Location, private modalService: NgbModal) { }

  ngOnInit() {
    this.refreshCandidates();
    this.currentUserUsername = this.tokenService.getUser().username;
  }

  public deleteUser(recruiterId: number): void {
    Swal.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      icon: 'warning',
      iconColor: "#8B0000",
      showCancelButton: true,
      confirmButtonColor: '#6485C1',
      cancelButtonColor: '#808080',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.rectuiterService.delete(recruiterId).subscribe(
          res => {
            Swal.fire({
              title: 'Succes!',
              text: res.message,
              icon: 'success',
              iconColor: '#6485C1',
              showConfirmButton: false,
              timer: 1500
            });
            this.refreshCandidates();
          }
        );
      }
    });
  }

  public getUserRoles(roles: Role[]) {
    return roles.map(r => r.name.split('_')[1]).join(' ; ');
  }

  private refreshCandidates(): void {
    this.rectuiterService.getAll().subscribe((data: Recruiter[]) => {
      this.collectionSize = data.length;
      this.users = data;
      this.usersFiltered = data.map((users, i) => ({ id: i + 1, ...users }))
        .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
    })
  }

  public changeNavigation(): void {
    this.usersFiltered = this.users.map((users, i) => ({ id: i + 1, ...users }))
      .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }

  openModal(recruiterId: number) {
    const modalRef = this.modalService.open(FormComponent);
    modalRef.componentInstance.id = recruiterId;
    modalRef.result.then((result) => {
      this.refreshCandidates();
     }).catch((error) => { });
  }
  openModalArchivedUsers() {
    const modalRef = this.modalService.open(ArchivedUsersModalComponent, {
      windowClass: "dark-modal",
      modalDialogClass: " modal-lg modalLarge",
    });
    modalRef.result.then((result) => {
      this.refreshCandidates();
     }).catch((error) => { });
  }

  goBack() {
    this.location.back();
  }

}
