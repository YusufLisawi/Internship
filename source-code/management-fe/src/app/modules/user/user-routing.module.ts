import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminGuard } from 'src/app/guard/admin.guard';
import { AuthenticationGuard } from 'src/app/guard/authentication.guard';
import { ListComponent } from './list/list.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  { path: '', data: { title: "Profile" }, component: ProfileComponent },
  { path: 'list', data: { title: "Users" }, canActivate: [AuthenticationGuard, AdminGuard], component: ListComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
