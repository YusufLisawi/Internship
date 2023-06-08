import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '@services/auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {

  private token: string = null;
  form: { password: string, rePassword: string } = { password: null, rePassword: null };
  isLoading: boolean = false;
  errorMessage: string = null;
  successMessage: string = null;

  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('id');
  }

  public onSubmit(): void {
    this.successMessage = null;
    this.errorMessage = null;
    if (this.form.password === this.form.rePassword) {
      this.isLoading = true;
      this.authService.resetPassword(this.token, this.form.password).subscribe(
        res => { 
          this.successMessage = res.message; 
          this.isLoading = false;
          this.router.navigate([`/login`]);
        },
        err => { this.errorMessage = err.error; this.isLoading = false; }
      );
    }
    else {
      this.errorMessage = "Password doesn't match.";
    }
  }

}
