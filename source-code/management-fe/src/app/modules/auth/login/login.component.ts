import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ERole } from '@models/Role.enum';
import { Role } from '@models/Role.model';
import { AuthService } from '@services/auth.service';
import { TokenStorageService } from '@services/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  loading: boolean = false;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService, private router: Router) { }

  ngOnInit(): void {
    this.authService.authenticated().subscribe(res => {
      if (res) {
        this.router.navigate(['/']);
      }
    });
  }

  onSubmit(): void {
    this.loading = true;
    const { username, password } = this.form;

    this.authService.login(username, password).subscribe({
      next: (data) => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {        
        this.errorMessage = err.error.message || err.error;
        this.isLoginFailed = true;
        this.loading = false;
      }
    });
  }

}
