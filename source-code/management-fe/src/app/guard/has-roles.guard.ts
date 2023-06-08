import { AuthService } from './../services/auth.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { ERole } from '@models/Role.enum';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HasRolesGuard implements CanActivate {
  constructor(private authService : AuthService) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      return this.authService.hasRoles(route.data.roles as ERole[]);
  }

}
