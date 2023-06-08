import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { ListComponent } from './list/list.component';
import { ProfileComponent } from './profile/profile.component';
import { FormComponent } from './form/form.component';
import { SharedModule } from '../shared/shared.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ArchivedUsersModalComponent } from './archived-users-modal/archived-users-modal.component';


@NgModule({
  declarations: [
    ListComponent,
    ProfileComponent,
    FormComponent,
    ArchivedUsersModalComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    SharedModule,
    NgbModule
  ]
})
export class UserModule { }
