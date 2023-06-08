import { Component, OnInit, Input, ViewChild, TemplateRef } from '@angular/core';
import { Candidate } from '@models/Candidate.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { CandidateService } from '@services/candidate.service';
import { KeyValue } from '@angular/common';
import { AddInterviewComponent } from '../add-interview/add-interview.component';
import { CalendarEvent } from 'angular-calendar';
import { isSameMonth } from 'date-fns';
import { isSameDay } from 'date-fns/fp';
@Component({
  selector: 'app-details-interview',
  templateUrl: './details-interview.component.html',
  styleUrls: ['./details-interview.component.scss']
})
export class DetailsInterviewComponent implements OnInit {

 @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;
 // @ViewChild(AddInterviewComponent) addInterviewComponent: AddInterviewComponent;
 @Input() candidateInterviewed: any;
  public values: any = [];
  public result: any = [];
  public candidate: any = [];
  public resultArray: any = [];
  public candidateinterviewed: Candidate[] = [];
  viewDate: Date = new Date();
  activeDayIsOpen: boolean = true;
  public modalData = {
  
  }; 
  modalService: any;
  constructor(private candidateService: CandidateService , public modal: NgbActiveModal) { }

  ngOnInit(): void {
  
    console.log("interviewId", this.candidateInterviewed);
    this.candidateService.getByInterviewId(this.candidateInterviewed).subscribe((data) => {
      console.log(data);
      this.candidateinterviewed = data;
      console.log("candidates", this.candidateinterviewed)
      const result = data.reduce((acc, obj) => {
        if (!acc[obj.timeOfInterview]) acc[obj.timeOfInterview] = {};
        if (!acc[obj.timeOfInterview][obj.candidateId])
          acc[obj.timeOfInterview][obj.candidateId] = [];
        acc[obj.timeOfInterview][obj.candidateId].push(obj); return acc;
      }, {});
      console.log('result:', result);
      this.result = result;
      console.log('res', Object.values(this.result))
      for (var key in this.result) { this.candidate.push(this.result[key]) };
      this.resultArray = Object.entries(this.result).map(([timeSlot, values]) => {
        return {
          timeSlot: timeSlot,
          values: Object.entries(values).map(([key, value]) => {
            return {
              key: parseInt(key),
              value: value
            };
          })
        };
      });
      this.resultArray.sort((a, b) => {
        if (a.timeSlot < b.timeSlot) {
          return -1;
        }
        if (a.timeSlot > b.timeSlot) {
          return 1;
        }
        return 0;
      });

      console.log(this.candidate);

    })
  }
  openAddInterviewModal(){
    this.modalService.open(DetailsInterviewComponent, {
      windowClass: "dark-modal",
      centered : true,
      modalDialogClass: " modal-lg",
    });
  }
 

  close() {
   this.modal.close();
  }
}
