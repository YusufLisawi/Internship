import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { InternshipRoutingModule } from "./internship-routing.module";
import { ListComponent } from "./list/list.component";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { SharedModule } from "@modules/shared/shared.module";
import { AddComponent } from "./add/add.component";
import { PdfViewerModule } from "ng2-pdf-viewer";
import { UpdateComponent } from "./update/update.component";
import { NgSelectModule } from '@ng-select/ng-select';


@NgModule({
  declarations: [ListComponent, AddComponent, UpdateComponent],
  imports: [
    CommonModule,
    InternshipRoutingModule,
    SharedModule,
    NgbModule,
    PdfViewerModule,
    NgSelectModule
  ],
})
export class InternshipModule {}
