import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { Session } from '@models/Session.model';
import { NgbActiveModal, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { CandidateService } from '@services/candidate.service';
import { InterviewDateService } from '@services/interview-date.service';
import { SessionService } from '@services/session.service';
import { InterviewService } from '@services/interview.service';
import Swal from 'sweetalert2';
import { Interview } from '@models/Interview.model';
@Component({
  selector: 'app-add-interview',
  templateUrl: './add-interview.component.html',
  styleUrls: ['./add-interview.component.scss']
})
export class AddInterviewComponent implements OnInit {

  datesSelected: NgbDateStruct[] = [];

  ReactiveForm: UntypedFormGroup = this.formBuilder.group({
    "dailyInterview": ["", Validators.compose([Validators.required])]
  })
  public preselectedCandidate: number;
  public interviewPerDay: number;
  public numberOfInterviewDays: number;
  session : Session;
  date : Date[] = [];

  constructor(private formBuilder: UntypedFormBuilder, private activeModal: NgbActiveModal, private candidateService: CandidateService, private sessionService: SessionService, private interviewDate : InterviewDateService, private interviewService : InterviewService) { }

  ngOnInit(): void {
    this.fetchPreselectedCandidateData();
    this.interviewService.getByCurrentSession('current').subscribe({
      next : res => {
        console.log(res);
        res.forEach( date => {
          this.date.push(date.date);
          this.interviewDate.setDate(this.date);
        })
      }
    })  
  }
  
  close() {
    this.activeModal.close();
  }

  change(value: NgbDateStruct[]) {
    this.datesSelected = value;
  }
  
  saveInterviews() {
    this.date = this.interviewDate.getDate();
    if(this.date.length==this.numberOfInterviewDays){
      this.interviewService.addInterview(this.date).subscribe({
      next : res => {
        Swal.fire({
          title: 'Succes!',
          text: 'The interview date was saved successfully',
          icon: 'success',
          iconColor: '#6485C1',
          showConfirmButton: false,
          timer: 1500
        }).then((result)=>{
           this.close()
        }) 
      }
    })
     } else{
      Swal.fire({
        title: 'Information !',
        text: `You should choose ${this.numberOfInterviewDays} days.`,
        icon: 'info',
        iconColor: '#6485C1',
        showConfirmButton: true,
      })
    } 
  }

  resetInterviewNumber(){
    Swal.fire({
      title: 'Change the interview number in one day',
      input: 'text',
      inputAttributes: {
        autocapitalize: 'off'
      },
      showCancelButton: true,
      confirmButtonText: 'Save',
    }).then((result) => {
      if(result.isConfirmed){
        this.session.dayInterviewNumber = result.value;
        this.sessionService.updateDailyInterviews(this.session).subscribe({
          next : res => {
            this.fetchPreselectedCandidateData();
          }
        })
      }
    });
  }

  private fetchPreselectedCandidateData(): void {
    this.candidateService.getCountPreselected().subscribe({
      next: res => {
        this.preselectedCandidate = res;
        this.sessionService.getSessionsByStatus('current').subscribe({
          next: res => {
            this.session = res;
            this.interviewPerDay = res.dayInterviewNumber;
            this.numberOfInterviewDays = Math.floor(this.preselectedCandidate / this.interviewPerDay);
          }
        });
      }
    });
  }
  
}
