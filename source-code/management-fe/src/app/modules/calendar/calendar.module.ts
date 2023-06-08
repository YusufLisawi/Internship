import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarRoutingModule } from './calendar-routing.module';
import { ComponentComponent } from './component/component.component';
import { SharedModule } from '../shared/shared.module';
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { AddInterviewComponent } from './add-interview/add-interview.component';
import { DetailsInterviewComponent } from './details-interview/details-interview.component';
import { MultiDatePickerComponent } from './add-interview/multi-date-picker/multi-date-picker.component';


@NgModule({
  declarations: [
    ComponentComponent,
    AddInterviewComponent,
    DetailsInterviewComponent,
    MultiDatePickerComponent,
  ],
  imports: [
    CommonModule,
    CalendarRoutingModule,
    SharedModule,
    NgbModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    })
  ]
})
export class AppCalendarModule { }
