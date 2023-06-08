import { Component, OnInit } from '@angular/core';
import { Candidate } from '@models/Candidate.model';
import { Session } from '@models/Session.model';
import { AddsessionComponent } from '@modules/session/add-session/add-session.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CandidateService } from '@services/candidate.service';
import { PdfService } from '@services/pdf.service';
import { SessionService } from '@services/session.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-waiting-list',
  templateUrl: './waiting-list.component.html',
  styleUrls: ['./waiting-list.component.scss']
})

export class WaitingListComponent implements OnInit {
  
  waitingList : Candidate[] = [];
  currentSession : Session;
  header : string[] = ['First name', 'Last name', 'Email', 'Phone number'];
  
  constructor(private modalService: NgbModal,private activeModal : NgbActiveModal, private candidateService : CandidateService, private sessionService : SessionService, private pdfService : PdfService) { }
  
  ngOnInit(): void {
    this.candidateService.getWaitingList().subscribe({
      next : (res) => {
        this.waitingList = res;      
      }
    });
    this.sessionService.getSessionsByStatus('current').subscribe({
      next : res => {
        this.currentSession = res;
      }
    });
  }

  exportPDF(){
    this.pdfService.exportPDF(this.header,this.waitingList,this.currentSession,true,false);
    Swal.fire({
      title: 'Succes!',
      text: 'PDF file dowloaded successfully',
      icon: 'success',
      iconColor: '#6485C1',
      showConfirmButton: false,
      timer: 1500
    })
  }

  moveToNextSession() {
    Swal.fire({
      title: 'Confirmation to move candidates',
      text: 'Do you want to move candidates in the waiting list to the next session?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if(result.isConfirmed){
        this.closeModal();
        const modalRef = this.modalService.open(AddsessionComponent, {
          windowClass: "dark-modal",
          modalDialogClass: "modal-md",
        });
        modalRef.result.then((result) => {
          if(result){
          }
        }).catch((error) => {});
      }
    });
  }
  closeModal(){
    this.activeModal.close();
  }
}
