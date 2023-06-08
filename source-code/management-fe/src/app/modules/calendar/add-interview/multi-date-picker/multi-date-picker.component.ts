import { ChangeDetectorRef, Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Interview } from '@models/Interview.model';
import { NgbActiveModal, NgbDate, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { InterviewDateService } from "@services/interview-date.service"
import { InterviewService } from '@services/interview.service';
import Swal from 'sweetalert2';
import { AddInterviewComponent } from '../add-interview.component';
import { CandidateService } from '@services/candidate.service';
import { SessionService } from '@services/session.service';
import { Session } from '@models/Session.model';

const equals = (one: NgbDateStruct, two: NgbDateStruct) =>
  one && two && two.year === one.year && two.month === one.month && two.day === one.day;

const before = (one: NgbDateStruct, two: NgbDateStruct) =>
  !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
    ? false : one.day < two.day : one.month < two.month : one.year < two.year;

const after = (one: NgbDateStruct, two: NgbDateStruct) =>
  !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
    ? false : one.day > two.day : one.month > two.month : one.year > two.year;


@Component({
  selector: 'app-multi-date-picker',
  templateUrl: './multi-date-picker.component.html',
  styleUrls: ['./multi-date-picker.component.scss']
})
export class MultiDatePickerComponent {

  hoveredDate: NgbDateStruct;
  fromDate: NgbDateStruct;
  toDate: NgbDateStruct;
  _datesSelected: NgbDateStruct[] = [];
  datesInterview : Date[] = [];
  datesColored: Date[] = [];
  public preselectedCandidate: number;
  public interviewPerDay: number;
  public numberOfInterviewDays: number;
  session : Session;
  @Input()
  set datesSelected(value: NgbDateStruct[]) {
    this._datesSelected = value;
  }
  get datesSelected(): NgbDateStruct[] {
    return this._datesSelected ? this._datesSelected : [];
  }

  @Output() datesSelectedChange = new EventEmitter<NgbDateStruct[]>();
  constructor(private interviewDate : InterviewDateService,private interviewService:InterviewService, private candidateService: CandidateService,private sessionService: SessionService) {}

  ngOnInit(): void {
  this.datesInterview = this.interviewDate.getDate(); 
  for (const dateString of this.datesInterview) {
    this.datesColored.push(new Date(dateString))
  }
  }

  onDateSelection(event: any, date: NgbDateStruct) {
    event.target.parentElement.blur();  //make that not appear the outline
    if (!this.fromDate && !this.toDate) {
      if (event.ctrlKey == true)  //If is CrtlKey pressed
        this.fromDate = date;
      else
        this.addDate(date);
      
      this.datesSelectedChange.emit(this.datesSelected);

    } else if (this.fromDate && !this.toDate && after(date, this.fromDate)) {
      this.toDate = date;
      this.addRangeDate(this.fromDate, this.toDate);
      this.fromDate = null;
      this.toDate = null;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
    this.interviewDate.setDate(this.datesInterview);
    this.interviewService.getByDateInterview(new Date(date.year,date.month-1,date.day)).subscribe((res)=>{
      if(res!=null){
        res.forEach(id=>{
          this.interviewService.deleteCandidate(id).subscribe(() => {
            Swal.fire({
              title: "Succes!",
              text:"Interview has been deleted successfully",
              icon: "success",
              iconColor: "#6485C1",
              showConfirmButton: false,
              timer: 1500,
            }).then((result)=>{             
              this.interviewService.addInterview(this.interviewDate.getDate()).subscribe((res)=>{
                this.candidateService.getCountPreselected().subscribe({
                  next: res => {
                    this.preselectedCandidate = res;
                    this.sessionService.getSessionsByStatus('current').subscribe({
                      next: res => {
                        this.session = res;
                        this.interviewPerDay = res.dayInterviewNumber;
                        this.numberOfInterviewDays = Math.floor( this.preselectedCandidate / this.interviewPerDay);
                        if(this.interviewDate.getDate().length!=this.numberOfInterviewDays){
                          Swal.fire({
                            title: 'Information !',
                            text: `You should choose ${this.numberOfInterviewDays} days.`,
                            icon: 'info',
                            iconColor: '#6485C1',
                            showConfirmButton: true,
                          })
                        }
                      }
                    })
                  }
                }); 
              }) 
            })
          });
        })
      }
    })
  }

  addDate(date: NgbDateStruct) {
    let dateConverter = new Date(date.year,date.month-1,date.day);
    let index = this.datesSelected.findIndex(f => f.day == date.day && f.month == date.month && f.year == date.year);
    if (index >= 0) {
      const indexToRemove = this.datesInterview.findIndex(date => new Date(date).getTime() === dateConverter.getTime());
      if(indexToRemove !== -1){
        this.datesSelected.splice(index , 1);
        this.datesInterview.splice(indexToRemove ,1);
      }
    }     
    else { //a simple push
        if (!this.datesInterview.some(d => +new Date(d) === +dateConverter)) {
            this.datesInterview.push(dateConverter)
            this.datesSelected.push(date);
       } else {
          const indexToRemove = this.datesInterview.findIndex(date => new Date(date).getTime() === dateConverter.getTime());
          if (indexToRemove !== -1) {
            this.datesInterview.splice(indexToRemove, 1);
            this.datesColored.splice(indexToRemove,1);
          } 
        }
    }  
  }

  addRangeDate(fromDate: NgbDateStruct, toDate: NgbDateStruct) {
    //We get the getTime() of the dates from and to
    let from = new Date(fromDate.year + "-" + fromDate.month + "-" + fromDate.day).getTime();
    let to = new Date(toDate.year + "-" + toDate.month + "-" + toDate.day).getTime();
    for (let time = from; time <= to; time += (24 * 60 * 60 * 1000)) //add one day
    {
      let date = new Date(time);
      //javascript getMonth give 0 to January, 1, to February...
      this.addDate({ year: date.getFullYear(), month: date.getMonth() , day: date.getDate() });
    }
    this.datesSelectedChange.emit(this.datesSelected);
  }

  //return true if is selected
  isDateSelected(date: NgbDateStruct) {
    return (this.datesSelected.findIndex(f => f.day == date.day && f.month == date.month && f.year == date.year) >= 0);
  }

  isHovered = date => this.fromDate && !this.toDate && this.hoveredDate && after(date, this.fromDate) && before(date, this.hoveredDate);
  isInside = date => after(date, this.fromDate) && before(date, this.toDate);
  isFrom = date => equals(date, this.fromDate);
  isTo = date => equals(date, this.toDate);

  isColored(date: NgbDate): boolean {
    const dateColored = new Date(date.year, date.month - 1, date.day); 
    return this.datesColored.some((d: Date) => d.getTime() === dateColored.getTime()); 
  }  
}
