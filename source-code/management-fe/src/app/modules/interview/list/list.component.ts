import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Candidate } from '@models/Candidate.model';
import { Score } from 'src/app/models/Score.model';
import { ScoreConfig } from 'src/app/models/ScoreConfig.model';
import Swal from 'sweetalert2'
import { ModalComponent } from '../../shared/modal/modal.component';
import { Level } from '@models/Level.enum';
import { CandidateService } from '@services/candidate.service';
import { ExcelService } from '@services/excel.service';
import { environment } from 'src/environments/environment';
import { InterviewService } from '@services/interview.service';
import { ScoreService } from '@services/score.service';
import { SettingsService } from '@services/settings.service';
import { DetailsComponent } from '../details/details.component';
import { PdfService } from '@services/pdf.service';
import { SessionService } from '@services/session.service';
import { Session } from '@models/Session.model';

@Component({
  selector: "app-list",
  templateUrl: "./list.component.html",
  styleUrls: ["./list.component.scss"]
})
export class ListComponent implements OnInit {

  candidateFormInterview: UntypedFormGroup;
  @ViewChild('pdfTable') pdfTable: ElementRef;
  columns: string[] = ['First name', 'Last name', 'City', 'E-mail', 'Phone number', 'University', 'Presence', 'Anapec'];
  header : string[] = ['First name', 'Last name'];
  fileName = 'ExcelSheet.xlsx';
  searchText: string;
  numberRegex = '([0-9]){2}';
  page = 1;
  pageSize = 15;
  closeResult: string;
  presence: string;
  firstTime: string;
  anapec: string;
  softskills: Number;
  candidatelistFast: Candidate[] = [];
  candidatelist: Candidate[] = [];
  list: any[] = [];
  exportList : any[] = [];
  emailList: any[] = [];
  collectionSize: number;
  candidate: Candidate = new Candidate('', '', '', '', '', Level.Default);
  public selectedRow: Candidate = new Candidate('', '', '', '', '', Level.Default);
  Tdenom: number;
  SSdenom: number;
  Pdenom: number;
  SSweigh: number;
  Tweigh: number;
  Pweigh: number;
  orderBy: string = "";
  asc: boolean = true;
  isFilterOpen: boolean = false;
  public universities: string[] = [];
  public cities: string[] = [];
  levelOfStudy: string;
  university: string;
  lvls: string[] = ["BAC+2", "BAC+3", "BAC+5", "BAC+8"];
  currentSession : Session;

  constructor(private candidateService: CandidateService, private excelService: ExcelService, private interviewService: InterviewService,private sessionService : SessionService, private modalService : NgbModal, private pdfService : PdfService) { }


  ngOnInit() {
    this.candidateService.getAcceptedCandidate().subscribe((data: Candidate[]) => {
      this.collectionSize = data.length;
      this.refreshCandidates();
      this.candidatelist = data;
      this.candidatelistFast = data;
      this.candidatelist.slice();
      this.list = this.candidatelist.map((candidate) => {
        return {
          firstName: candidate.firstName, lastName: candidate.lastName, city: candidate.city,
          mail: candidate.email, phone: candidate.phoneNumber, university: candidate.university, presence: candidate.presence, anapec: candidate.anapec
        }
      });
      this.emailList = this.candidatelist.map((candidate) => {
        return candidate.email
      })
      this.exportList = this.candidatelist.filter((candidate) => {
        if(candidate.score?.isAccepted == true){
        return {
            firstName: candidate.firstName, lastName: candidate.lastName
          }
        }
      })
    });
    this.candidateService.getUniversities().subscribe((data) => {
      this.universities = data;
    });
    this.sessionService.getSessionsByStatus('current').subscribe({
      next : res => {
        this.currentSession = res;
      }
    });
  }

  openCandidatedetails(candidate : Candidate) {
    const modalRef = this.modalService.open(DetailsComponent, {
      windowClass: "dark-modal",
      centered: true,
      modalDialogClass: " modal-lg modalLargecv",
    });
    modalRef.componentInstance.candidate = candidate;
    modalRef.componentInstance.isSaved.subscribe(($event) => {
      if($event){
        this.ngOnInit();
      }
    })
    modalRef.result.then((result) => {
      if(result){
        console.log(result);
      }
    }).catch((error) => {});
  }


  changeOrder(column: string) {
    if (this.orderBy == column)
      this.asc = !this.asc;
    else
      this.orderBy = column;
  }

  refreshCandidates() {
    this.candidateService.getAcceptedCandidate().subscribe((data: Candidate[]) => {
      this.candidatelist = data.map((Candidate, i) => ({ id: i + 1, ...Candidate }))
        .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
    })
  }
 
  sendEmail() {
    Swal.fire({
      title: 'Confirmation to send Emails',
      text: 'Do you want to send an email to all Accepted Candidate?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if(result.isConfirmed){

        this.interviewService.sendInvitation(this.emailList).subscribe((data: any) => {
          Swal.fire({
            title: 'Succes!',
            text: data.message,
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          });
        });
      }
    });
  }

  exportExcel(): void {
    this.excelService.exportExcel("Interview List", '', this.columns, this.list, 'Interview List', 'Sheet1');
    Swal.fire({
      title: 'Succes!',
      text: 'Excel file dowloaded successfully',
      icon: 'success',
      iconColor: '#6485C1',
      showConfirmButton: false,
      timer: 1500
    })
  }

  exportpdf(){
    this.exportList.map((candidate) => {
      return {
        firstName: candidate.firstName, lastName: candidate.lastName
      }
    });
    this.pdfService.exportPDF(this.header,this.exportList,this.currentSession,null,true);
    Swal.fire({
      title: 'Succes!',
      text: 'PDF file dowloaded successfully',
      icon: 'success',
      iconColor: '#6485C1',
      showConfirmButton: false,
      timer: 1500
    })
  }
}

