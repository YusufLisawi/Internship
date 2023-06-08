import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SessionRoutingModule } from './session-routing.module';
import { ListComponent } from './list/list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from '../shared/shared.module';
import { CandidateComponent } from './candidate/candidate.component';
import { AddsessionComponent } from './add-session/add-session.component';
import { UpdateSessionComponent } from './update-session/update-session.component';


@NgModule({
  declarations: [
    ListComponent,
    CandidateComponent,
    AddsessionComponent,
    UpdateSessionComponent
  ],
  imports: [
    CommonModule,
    SessionRoutingModule,
    NgbModule,
    SharedModule
  ]
})
export class SessionModule { }
