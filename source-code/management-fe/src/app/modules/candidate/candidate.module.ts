import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CandidateRoutingModule } from './candidate-routing.module';
import { ListComponent } from './list/list.component';
import { SharedModule } from '../shared/shared.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { AddComponent } from './add/add.component';
import { UpdateComponent } from './update/update.component';
import { PreselectionComponent } from './preselection/preselection.component';
import { NewOptionComponent } from './new-option/new-option.component';


@NgModule({
  declarations: [
    ListComponent,
    AddComponent,
    UpdateComponent,
    PreselectionComponent,
    NewOptionComponent
  ],
  imports: [
    CommonModule,
    CandidateRoutingModule,
    SharedModule,
    NgbModule,
    PdfViewerModule
  ]
})
export class CandidateModule { }
