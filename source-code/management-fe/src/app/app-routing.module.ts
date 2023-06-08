import { Title } from '@angular/platform-browser';
import { HasRolesGuard } from './guard/has-roles.guard';
import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { LoginComponent } from "@modules/auth/login/login.component";
import { AdminLayoutComponent } from "@components/admin-layout/admin-layout.component";
import { AuthenticationGuard } from "./guard/authentication.guard";
import { ForgotPasswordComponent } from "@modules/auth/forgot-password/forgot-password.component";
import { ErrorComponent } from "@modules/error/error.component";
import { ResetPasswordComponent } from "@modules/auth/reset-password/reset-password.component";
import { ERole } from '@models/Role.enum';

const routes: Routes = [
  { path: "", data: { title: "NTT DATA Train | Home"}, canActivate: [AuthenticationGuard], loadComponent: ()=> import('@modules/home/home.component').then(c => c.HomeComponent) },
  { path: "login", data: { title: "Login" }, component: LoginComponent },
  { path: "forgot-password", data: { title: "Forgot password" }, component: ForgotPasswordComponent },
  { path: "reset-password/:id", data: { title: "Reset password" }, component: ResetPasswordComponent },
  {
    path: "beca",
    canActivate: [AuthenticationGuard, HasRolesGuard],
    data: {
      title:"Beca Training",
      roles: [ERole.ROLE_ADMIN, ERole.ROLE_BECA]
    },
    children: [
      { path: "", pathMatch: "full", redirectTo: "dashboard" },
      { path: "user", data: { title: "Account" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/user/user.module").then(m => m.UserModule) },
      { path: "dashboard", data: { title: "Dashboard" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/dashboard/dashboard.module").then(m => m.DashboardModule) },
      { path: "session", data: { title: "Session" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/session/session.module").then(m => m.SessionModule) },
      { path: "candidate", data: { title: "Candidate" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/candidate/candidate.module").then(m => m.CandidateModule) },
      { path: "settings", data: { title: "Settings" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/settings/settings.module").then(m => m.SettingsModule) },
      { path: "calendar", data: { title: "Calendar" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/calendar/calendar.module").then(m => m.AppCalendarModule) },
      { path: "interview", data: { title: "Interview" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/interview/interview.module").then(m => m.InterviewModule) },
      { path: "final-list", data: { title: "Final list" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/final-list/final-list.module").then(m => m.FinalListModule) },
      { path: "**", pathMatch:"full", redirectTo: "dashboard" }

    ]
  },
  {
    path: "internship",
    canActivate: [AuthenticationGuard, HasRolesGuard],
    data: {
      title:"Internship" ,
      roles: [ERole.ROLE_ADMIN, ERole.ROLE_INTERNSHIP]
    },
    children: [
      { path: "", pathMatch:"full", redirectTo: "dashboard" },
      { path: "user", data: { title: "Account" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/user/user.module").then(m => m.UserModule) },
      { path: "dashboard", data: { title: "Dashboard" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/dashboard/dashboard.module").then(m => m.DashboardModule) },
      { path: "session", data: { title: "Session" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/session/session.module").then(m => m.SessionModule) },
      { path: "candidate", data: { title: "Candidate" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/candidate/candidate.module").then(m => m.CandidateModule) },
      { path: "internship", data: { title: "Internship" }, component: AdminLayoutComponent, loadChildren: () => import("@modules/internship/internship.module").then(m => m.InternshipModule) },
      { path: "**", pathMatch:"full", redirectTo: "dashboard" }
    ]
  },
  { path: "*", component: ErrorComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
