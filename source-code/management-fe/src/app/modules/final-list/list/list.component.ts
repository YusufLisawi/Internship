import { Component, ElementRef, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Candidate } from '@models/Candidate.model';
import { Score } from 'src/app/models/Score.model';
import Swal from 'sweetalert2'
import { Level } from '@models/Level.enum';
import { CandidateService } from '@services/candidate.service';
import { SessionService } from '@services/session.service';
import { ExcelService } from '@services/excel.service';
import { AdmittedListComponent } from '../admitted-list/admitted-list.component';
import { WaitingListComponent } from '../waiting-list/waiting-list.component';
import { ScoreDetailsComponent } from '../score-details/score-details.component';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  @ViewChild('pdfTable') pdfTable: ElementRef;

  columns: string[] = ['First name', 'Last name', 'E-mail','Phone number', 'City','Diploma', 'Anapec', 'English', 'Spanish', 'Final score'];
  fileName = 'ExcelSheet.xlsx';
  page = 1;
  pageSize = 10;
  closeResult: string;
  minValueSS: number = 10;
  maxValueSS: number = 20;
  minValueTECH: number = 10;
  maxValueTECH: number = 20;
  minValueCOMM: number = 10;
  maxValueCOMM: number = 20;


  anapec: string;
  technology: string;
  preselectionStatus: string;
  english: string;
  french: string;
  spanish: string;
  deutch: string;
  softskills: Number;
  techs: string[] = [];
  levels: string[] = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2'];
  score: Score;
  candidates: Candidate[] = [];
  WaitingCandidate: Candidate[] = [];
  collectionSize: number;
  candidate: Candidate = new Candidate( '', '', '', '', '', Level.Default );
  selectedRow: Candidate = new Candidate( '', '', '', '', '', Level.Default );
  nbrTimes: number;
  isFilterOpen: boolean = false;
  public asc :boolean=true;
  public orderBy: string;
  excelList : any[] = [];


  constructor(private sessionService: SessionService, private candidateService: CandidateService, private modalService: NgbModal,private excelService: ExcelService) {}


  ngOnInit() {
    this.candidateService.getFinalList().subscribe((data: Candidate[]) => {
      this.collectionSize = data.length;
      this.refreshCandidates();
      this.candidates = data;
      this.candidates.slice();
      this.sessionService.getAllTechnologies().subscribe((data: string[]) => {this.techs=data;});
      this.excelList = this.candidates.map((candidate) => {
        return {
          firstName: candidate.firstName, lastName: candidate.lastName, mail: candidate.email, phone: candidate.phoneNumber, city: candidate.city, diploma: candidate.diplome, university : candidate.university,
          english : candidate.score.englishLevel, spanish : candidate.score.spanishLevel, finslScore : candidate.score.finalScore
        }
      })
    });
  }

  open(candidate) {
    this.selectedRow = candidate;
  }

  refreshCandidates() {
    this.candidateService.getFinalList().subscribe((data: Candidate[]) => {
      this.candidates = data.map((Candidate, i) => ({ id: i + 1, ...Candidate }))
        .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
    })
  }

  openAdmittedListModal() {
    const modalRef = this.modalService.open(AdmittedListComponent, {
      windowClass: "dark-modal",
      modalDialogClass: " modal-lg",
    });
    modalRef.componentInstance.admittedCandidate = this.candidates;
    modalRef.result.then((result) => {
      if(result){
        console.log(result);
      }
    }).catch((error) => {});
  }

  openWaitingListModal(){
    const modalRef = this.modalService.open(WaitingListComponent, {
      windowClass: "dark-modal",
      modalDialogClass: " modal-lg",
    });
    modalRef.result.then((result) => {
      if(result){
        console.log(result);
      }
    }).catch((error) => {});
  }

  openDetailsScoreModal(score : Score){
    const modalRef = this.modalService.open(ScoreDetailsComponent, {
      windowClass: "dark-modal",
      modalDialogClass: " modal-lg",
    });
    modalRef.componentInstance.score = score;
    modalRef.result.then((result) => {
      if(result){
        console.log(result);
      }
    }).catch((error) => {});
  }


  exportExcel(): void {
    this.excelService.exportExcel("Admitted Candidates","",this.columns,this.excelList,'Admitted Candidates','Sheet1');
    Swal.fire({
      title: 'Succes!',
      text: 'Excel file dowloaded successfully',
      icon: 'success',
      showConfirmButton: false,
      timer: 1500
    })
  }

  changeOrder(column: string){
    if(this.orderBy==column)
      this.asc=!this.asc;
    else
      this.orderBy=column;
  }
}

