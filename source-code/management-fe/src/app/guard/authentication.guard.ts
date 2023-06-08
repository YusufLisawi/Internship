import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.authService.authenticated().pipe(
      map(e => {
        if (e == true) {
          return true;
        }
        else{
          this.router.navigate(['/login']);
        }
      }),
      catchError((res) => {
        if (res.error == false) {
          Swal.fire({
            title: 'Warning !',
            text: "Your session has expired . You have to login again !",
            icon: 'warning',
            showConfirmButton: true
          }).then(() => this.router.navigate(['/login']));
        }
        else if(res.error){
          Swal.fire({
            title: 'Warning !',
            text: res.error,
            icon: 'warning',
            showConfirmButton: true
          }).then(() => this.router.navigate(['/login']));
        }
        else{
          Swal.fire({
            title: 'Warning !',
            text: "Something went wrong please login again.",
            icon: 'warning',
            showConfirmButton: true
          }).then(() => this.router.navigate(['/login']));
          return of(false);
        }
      })
    );
  }

}
