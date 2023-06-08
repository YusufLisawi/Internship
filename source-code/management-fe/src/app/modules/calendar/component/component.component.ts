import { ChangeDetectionStrategy, Component, Input, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { addHours,  isSameDay,  isSameMonth,  parse,  startOfDay } from 'date-fns';
import Swal from 'sweetalert2';
import { Subject } from 'rxjs';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {   CalendarEvent,CalendarEventAction,  CalendarView } from 'angular-calendar';

import { InterviewService } from '@services/interview.service';
import { SessionService } from '@services/session.service';

import { Technology } from '@models/Technologie.model';
import { Interview } from '@models/Interview.model';
import { Session } from '@models/Session.model';
import { AddInterviewComponent } from '../add-interview/add-interview.component';
import { CandidateService } from '@services/candidate.service';
import { data, event } from 'jquery';
import { Candidate } from '@models/Candidate.model';
import { title } from 'process';
import { CalendarEventActionsComponent } from 'angular-calendar/modules/common/calendar-event-actions.component';
import { DetailsInterviewComponent } from '../details-interview/details-interview.component';
import { InterviewDateService } from '@services/interview-date.service';
import { Router } from '@angular/router';

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3'
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF'
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA'
  }
};
const events: CalendarEvent[] = [];

@Component({
  selector: 'app-component',
  templateUrl: './component.component.html',
  styleUrls: ['./component.component.scss'],
  
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ComponentComponent implements OnInit {
  refresh: Subject<any> = new Subject();
  events: CalendarEvent[] = [];
  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;
  viewDate: Date = new Date();
  date : Date[] = [];
  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;
  activeDayIsOpen: boolean = false;
  interviews : Interview[] = [];
  public interviewid = null;
  public modalData = { };

  ReactiveForm : UntypedFormGroup = this.formBuilder.group({
    "dailyInterview" : ["", Validators.compose([Validators.required])]
  })

  constructor(private router: Router,private interviewDate : InterviewDateService,private sessionService: SessionService, private candidateService:CandidateService ,private interviewService: InterviewService, private modalService: NgbModal , private formBuilder: UntypedFormBuilder) {
    this.initializeEvents()
    setTimeout(() => {
    this.interviewService.getByCurrentSession('current').subscribe({
      next : res => {
        this.interviews = res;
        
        res.forEach((value,index) => {
          console.log(value.interviewId+' - '+ value.date);
          this.events.push(this.fillEvents(value.interviewId, value.date));
          this.refresh.next();
          })
           console.log("modalData",this.modalData);

      }
    }
    )
    
  },0);
}

  ngOnInit(): void {
     this.sessionService.getSessionsByStatus("current").subscribe((res)=>{
      if(res.startTimeOfInterview==null || 
        res.endTimeOfInterview==null||
        res.testDuration==null||
        res.dayInterviewNumber==null){
        Swal.fire({
          title: "Information !",
          text: "You have to fix session settings first! !!",
          icon: "info",
          iconColor: "#6485C1",
          showConfirmButton: true,
        }).then((res) => {
          this.router.navigate(['/beca/settings'], { queryParams: { tab: 'third-tab' } });
        });
      } 
    }) 
 
    const action: string='';
    const date = new Date();
 

  }

  private fillEvents(numberInterview : number, date : Date) : any {
    return  {
      title: 'Interview ' + (numberInterview),
      color: colors.blue.primary,
      start: new Date(date),
      actions: [
        {
          label: '<i class="fa fa-fw fa-pencil"></i>',
          onClick: ({ event } : { event: CalendarEvent }): void => {
            console.log('Edit event', event);
            console.log("ooooooooooooo");
          }
        },
        {
          label: '<i class="fa fa-fw fa-times"></i>',
          onClick: ({ event }: { event: CalendarEvent }): void => {
            this.events = this.events.filter(iEvent => iEvent !== event);
            this.handleEvent('Deleted', event);
          }
        }
      ]
    }
  }
 

  private initializeEvents() {
   
  }

  close(){
    
  }

 
  openAddInterviewModal(){
    this.interviewService.getByCurrentSession('current').subscribe({
      next: res => {
        res.forEach(date => {
          this.date.push(date.date);
          this.interviewDate.setDate(this.date);
        });
        const modalRef = this.modalService.open(AddInterviewComponent, {
          windowClass: "dark-modal",
          centered : true,
          modalDialogClass: " modal-lg",
        });
        modalRef.componentInstance.interviewDates = res.length !== 0 ? this.interviewDate.getDate() : null;
        modalRef.result.then(() => {
          window.location.reload();
        })
      }
    });
  }
 
  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

 
  handleEvent(action: string, event: CalendarEvent): void {
    const interviewid=event.title.split(" ")[1];
    this.interviewid= interviewid;
    this.modalData = { event, action };
   
    const modalRef =this.modalService.open(DetailsInterviewComponent, { size: 'lg' });
    modalRef.componentInstance.candidateInterviewed = interviewid;
  }

   openBackDropCustomClass(content) {
    this.modalService.open(content,
      {
        windowClass: 'modal2',
        backdropClass: 'black'
      });
  }
 

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  setView(view: CalendarView) {
    this.view = view;
  }

}
function forEach(arg0: (value: any, index: any) => void) {
  throw new Error('Function not implemented.');
}

