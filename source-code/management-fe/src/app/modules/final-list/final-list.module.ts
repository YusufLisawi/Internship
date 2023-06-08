import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FinalListRoutingModule } from './final-list-routing.module';
import { ListComponent } from './list/list.component';
import { SharedModule } from '../shared/shared.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AdmittedListComponent } from './admitted-list/admitted-list.component';
import { WaitingListComponent } from './waiting-list/waiting-list.component';
import { ScoreDetailsComponent } from './score-details/score-details.component';


@NgModule({
  declarations: [
    ListComponent,
    AdmittedListComponent,
    WaitingListComponent,
    ScoreDetailsComponent
  ],
  imports: [
    CommonModule,
    FinalListRoutingModule,
    SharedModule,
    NgbModule
  ]
})
export class FinalListModule { }
