import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TableFilterPipe } from '@pipes/table-filter.pipe';
import { SearchFilterPipe } from "@pipes/search-filter.pipe";
import { SessionFilter } from '@pipes/session-Filter.pipe';
import { SessionOrder } from '@pipes/session-Order.pipe';
import { ModalComponent } from './modal/modal.component';
import { HasRoleDirective } from '@directives/app-user-role.directive';
import { FinalListFilter } from '@pipes/finalList-Filter.pipe';
import { PreselectionPipe } from '@pipes/preselection-filter.pipe';
import { InternshipSortPipe } from '../../pipes/internship-sort.pipe';
import { InternshipSearchFilterPipe } from '../../pipes/internship-search-filter.pipe';
import { TooltipDirective } from './tooltip/tooltip.directive';
import { TooltipComponent } from './tooltip/tooltip.component';


@NgModule({
  declarations: [
    TableFilterPipe,
    SearchFilterPipe,
    SessionFilter,
    SessionOrder,
    FinalListFilter,
    PreselectionPipe,
    ModalComponent,
    HasRoleDirective,
    InternshipSortPipe,
    InternshipSearchFilterPipe,
    TooltipComponent,
    TooltipDirective,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    CommonModule,
    TableFilterPipe,
    SearchFilterPipe,
    SessionFilter,
    SessionOrder,
    FinalListFilter,
    PreselectionPipe,
    FormsModule,
    ReactiveFormsModule,
    HasRoleDirective,
    InternshipSortPipe,
    InternshipSearchFilterPipe,
    TooltipDirective,

  ]
})
export class SharedModule { }
