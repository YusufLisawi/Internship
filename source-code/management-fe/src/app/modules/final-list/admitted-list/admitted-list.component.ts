import { Component, Input, OnInit } from '@angular/core';
import { Candidate } from '@models/Candidate.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FinalListService } from '@services/finalList.service';
import Swal from 'sweetalert2';
import { Session } from '@models/Session.model';
import { SessionService } from '@services/session.service';
import { PdfService } from '@services/pdf.service';

 

@Component({
  selector: 'app-admitted-list',
  templateUrl: './admitted-list.component.html',
  styleUrls: ['./admitted-list.component.scss']
})
export class AdmittedListComponent implements OnInit {

  
  @Input() admittedCandidate: Candidate[] = [];
  emailList : string[] = [];
  currentSession : Session;
  header : string[] = ['First name', 'Last name', 'Email', 'Phone number'];

  constructor(private activeModal : NgbActiveModal, private finalListService : FinalListService, private sessionService : SessionService, private pdfService : PdfService) { }

  ngOnInit(): void {
    this.emailList = this.admittedCandidate.map((candidate) => {
      return candidate.email
    });
    this.sessionService.getSessionsByStatus('current').subscribe({
      next : res => {
        this.currentSession = res;
      }
    });
  }

  sendEmails(){
    Swal.fire({
      title: 'Confirmation to send Emails',
      text: 'Do you want to send an email to all Admitted Candidate?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if (result.isConfirmed) {
        this.finalListService.sendEmailAdmittedCandidate(this.emailList).subscribe({
          next : (response : string) =>{
            Swal.fire({
              title: 'Succes!',
              text: response,
              icon: 'success',
              iconColor: '#6485C1',
              showConfirmButton: false,
              timer: 1500
            })
          },
          error : (reason) =>{
            console.log(reason);
          }
        });
      }
    });
  }
 
  exportPDF(){
    this.pdfService.exportPDF(this.header,this.admittedCandidate,this.currentSession,false,false);
    Swal.fire({
      title: 'Succes!',
      text: 'PDF file dowloaded successfully',
      icon: 'success',
      iconColor: '#6485C1',
      showConfirmButton: false,
      timer: 1500
    })
  } 
 
  closeModal(){
    this.activeModal.close();
  }

}
