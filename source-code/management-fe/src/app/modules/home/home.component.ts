import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    const userRoles = this.authService.getUserRoles();
    if (userRoles.length === 1 && userRoles[0] !== 'ROLE_ADMIN') {
      switch (userRoles[0]) {
        case 'ROLE_BECA':
          this.router.navigateByUrl('/beca/dashboard');
          break;
        case 'ROLE_INTERNSHIP':
          this.router.navigateByUrl('/internship/dashboard');
          break;
        case 'ROLE_CERTIFICATION':
          this.router.navigateByUrl('/certification/dashboard');
          break;
        default:
          break;
      }
    }
  }

  logout() {
    this.authService.logout();
  }
}
