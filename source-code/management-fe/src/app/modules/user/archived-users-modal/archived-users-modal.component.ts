import { Component, OnInit } from '@angular/core';
import { Recruiter } from '@models/Recruiter.model';
import { NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { RecruiterService } from '@services/recruiter.service';
import { TokenStorageService } from '@services/token-storage.service';
import {Location} from '@angular/common';
import Swal from 'sweetalert2';
import { Role } from '@models/Role.model';
@Component({
  selector: 'app-archived-users-modal',
  templateUrl: './archived-users-modal.component.html',
  styleUrls: ['./archived-users-modal.component.scss']
})
export class ArchivedUsersModalComponent implements OnInit {
  public users: Recruiter[] = [];
  public usersFiltered: Recruiter[] = [];
  collectionSize: number;
  page = 1;
  pageSize = 5;
  constructor(private activeModal: NgbActiveModal, private rectuiterService: RecruiterService, private tokenService: TokenStorageService, private location: Location, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.refreshCandidates();
  }
  private refreshCandidates(): void {
    this.rectuiterService.getArchived().subscribe((data: Recruiter[]) => {
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
  public getUserRoles(roles: Role[]) {
    return roles.map(r => r.name.split('_')[1]).join(' ; ');
  }
  public closeModal() {
    this.activeModal.close(null);
  }
  public restoreUser(recruiterId: number): void {
    Swal.fire({
      title: 'Are you sure ?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#6485C1',
      cancelButtonColor: '#808080',
      confirmButtonText: 'Just Do it !'
    }).then((result) => {
      if (result.isConfirmed) {
        this.rectuiterService.restore(recruiterId).subscribe(
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

}
