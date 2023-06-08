import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { ComponentComponent } from './component/component.component';
// import { SharedModule } from '@modules/shared/shared.module';
import { DownloadPdfComponent } from './download-pdf/download-pdf.component';

@NgModule({
  declarations: [
    ComponentComponent,
    DownloadPdfComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    // SharedModule
  ]
})
export class DashboardModule { }
