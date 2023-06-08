import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { RouterModule } from "@angular/router";
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from "./app-routing.module";
import { ComponentsModule } from "@components/components.module";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { SharedModule } from "@modules/shared/shared.module";
import { HttpInterceptorModule } from "./http-interceptor.module";

import { CandidateService } from '@services/candidate.service'
import { InterviewService } from '@services/interview.service'
import { FinalListService } from '@services/finalList.service'
import { SessionService } from '@services/session.service';
import { PdfService } from '@services/pdf.service';

import { AdminLayoutComponent } from "@components/admin-layout/admin-layout.component";
import { LoginComponent } from "@modules/auth/login/login.component";
import { ForgotPasswordComponent } from '@modules/auth/forgot-password/forgot-password.component';
import { AppComponent } from "./app.component";

import { AuthenticationGuard } from "./guard/authentication.guard";
import { ResetPasswordComponent } from "@modules/auth/reset-password/reset-password.component";
import { ErrorComponent } from "@modules/error/error.component";
import { HomeComponent } from "@modules/home/home.component";

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    ToastrModule.forRoot(),
    ReactiveFormsModule,
    HttpInterceptorModule,
    SharedModule
  ],
  declarations: [AppComponent, AdminLayoutComponent, LoginComponent, ForgotPasswordComponent, HomeComponent, ResetPasswordComponent, ErrorComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
