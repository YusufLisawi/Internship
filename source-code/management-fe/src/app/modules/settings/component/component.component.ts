import { Time } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Interview } from '@models/Interview.model';
import { Session } from '@models/Session.model';
import { InterviewDateService } from '@services/interview-date.service';
import { InterviewService } from '@services/interview.service';
import { SessionService } from '@services/session.service';
import { ToastrService } from 'ngx-toastr';
import { tap } from 'rxjs/operators';
import { ScoreConfig } from 'src/app/models/ScoreConfig.model';
import { SettingsService } from 'src/app/services/settings.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-component',
  templateUrl: './component.component.html',
  styleUrls: ['./component.component.scss']
})
export class ComponentComponent implements OnInit {

  public reactiveForm: UntypedFormGroup = this.formBuilder.group({
    'peopleDenominator': [20, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
    'softSkillsDenominator': [20, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
    'technicalTestDenominator': [20, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
    'technicalInterviewDenominator': [20, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
    'peopleWeight': [1, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])],
    'softSkillsWeight': [1, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])],
    'technicalTestWeight': [1, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])],
    'technicalInterviewWeight': [1, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])]
  });
  public dayInterviewNumber: number;
  public admittedNumber: number;
  public eliminatingMark : number;
  public englishLevel : number;
  public startTimeOfInterview : string;
  public testDuration : number;
  public endTimeOfInterview : string

  currentSession : Session;
  date : Date[] = [];
  interview:Interview[]=[];
  constructor(private route: ActivatedRoute,private formBuilder: UntypedFormBuilder, private settingsService: SettingsService, private sessionService: SessionService,private interviewDate : InterviewDateService, private interviewService : InterviewService) { }

  ngOnInit() {
    this.settingsService.getCurrent().subscribe({
      next: res => {
        this.reactiveForm = this.formBuilder.group({
          'peopleDenominator': [res.peopleDenominator, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
          'softSkillsDenominator': [res.softSkillsDenominator, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
          'technicalTestDenominator': [res.technicalTestDenominator, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
          'technicalInterviewDenominator': [res.technicalInterviewDenominator, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(100)])],
          'peopleWeight': [res.peopleWeight, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])],
          'softSkillsWeight': [res.softSkillsWeight, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])],
          'technicalTestWeight': [res.technicalTestWeight, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])],
          'technicalInterviewWeight': [res.technicalInterviewWeight, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(10)])]
        });
      }
    });
    this.sessionService.getSessionsByStatus('current').subscribe({
      next: res => {
        if(res){
          this.currentSession = res;
          this.dayInterviewNumber = res.dayInterviewNumber;
          this.admittedNumber = res.admittedNumber;
          this.eliminatingMark = res.eliminatingMark;
          this.englishLevel = res.englishLevelRequire;
          this.startTimeOfInterview = res.startTimeOfInterview;
          this.endTimeOfInterview=res.endTimeOfInterview;
          this.testDuration = res.testDuration;
        }       
      }
    });
    this.interviewService.getByCurrentSession('current').subscribe({
      next : res => {
        console.log(res);
        res.forEach( date => {
          this.date.push(date.date);
          this.interviewDate.setDate(this.date);
        })
      }
    })  
     this.route.queryParams.subscribe(params => {
      const tabParam = params['tab'];
      if (tabParam === 'third-tab') {
        const thirdTabElement = document.getElementById('third-tab');
        if (thirdTabElement) {
          thirdTabElement.click();
        }
      }
    }); 
  }

  public add(scoreConfig: ScoreConfig): void {
    this.settingsService.add(scoreConfig).subscribe({
      next: res => {
        Swal.fire({
          title: 'Succes!',
          text: "The settings has been updated successfully.",
          icon: 'success',
          iconColor: '#6485C1',
          showConfirmButton: false,
          timer: 1500
        });
      }
    });
  }

  public updateInterviewNumber(): void {
    this.currentSession.dayInterviewNumber = this.dayInterviewNumber;
    this.currentSession.startTimeOfInterview = this.startTimeOfInterview;
    this.currentSession.endTimeOfInterview=this.endTimeOfInterview;
    this.currentSession.testDuration = this.testDuration;
    if (this.currentSession.startTimeOfInterview <= this.currentSession.endTimeOfInterview) {
      this.sessionService.updateDailyInterviews(this.currentSession).subscribe({
      next: res => {
        if(res){
          Swal.fire({
            title: 'Succes!',
            text: "The interview number has been updated successfully.",
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          }).then((res)=>{
            this.interviewService.getByCurrentSession("current").subscribe((res)=>{
              if(res.length!=0){
                Swal.fire({
                title: "Are you sure?",
                text: "You want to modify the date on the calendar.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#6485C1",
                cancelButtonColor: "#808080",
                confirmButtonText: "Yes, updated it!!",
                }).then((result) => {
                  if (result.isConfirmed) {
                    this.interviewService.addInterview(this.interviewDate.getDate()).subscribe((next)=>{
                        Swal.fire({
                          title: "Succes!",
                          text:'The calendar has been updated',
                          icon: "success",
                          iconColor: "#6485C1",
                          showConfirmButton: false,
                          timer: 1500,
                        });                   
                    })
                  }
                });
              }  
            })
          })
        }else{
          Swal.fire({
            title: 'Oops !',
            text: "Something went wrong .",
            icon: 'error',
            showConfirmButton: true
          });
        }
      }
      });
    } else{
      Swal.fire({
        title: 'Oops !',
        text: "Start time should be earlier than end time.",
        icon: 'error',
        showConfirmButton: true
      });
    }  
  }

  public updateAdmittedNumber(): void {
    this.currentSession.admittedNumber = this.admittedNumber;
    this.currentSession.eliminatingMark = this.eliminatingMark;
    this.currentSession.englishLevelRequire = this.englishLevel;
    this.sessionService.updateAdmittedNumber(this.currentSession).subscribe({
      next: res => {
        if(res){
          Swal.fire({
            title: 'Succes!',
            text: "The admitted number has been updated successfully.",
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          });
        }
        else{
          Swal.fire({
            title: 'Oops !',
            text: "Something went wrong .",
            icon: 'error',
            showConfirmButton: true
          });
        }
      }
    });
  }
}
