import { Component, OnInit } from '@angular/core';
import { AuthService } from '@services/auth.service';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  email: string = null;
  isLoading: boolean = false;
  errorMessage: string = null;
  successMessage: string = null;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  public onSubmit(): void {
    this.successMessage = null;
    this.errorMessage = null;
    if(this.email){
      this.isLoading = true;
      this.authService.forgotPassword(this.email).subscribe(
        res => { this.successMessage = res.message; this.isLoading = false; },
        err => { this.errorMessage = err.error; this.isLoading = false; }
      );
    }
  }

}
