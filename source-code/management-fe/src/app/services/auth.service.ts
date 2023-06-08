import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recruiter } from '@models/Recruiter.model';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { TokenStorageService } from './token-storage.service';
import { JwtResponse } from '@models/JwtResponse.model';
import { ERole } from '@models/Role.enum';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = `${environment.apiUrl}auth/`;

  constructor(private http: HttpClient, private router: Router, private tokenStrorageService: TokenStorageService) { }

  public login(username: string, password: string): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.baseUrl}login`, { username, password });
  }

  public authenticated(): Observable<any> {
    let user: Recruiter = this.tokenStrorageService.getUser();
    let token: string = this.tokenStrorageService.getToken();

    return this.http.post<any>(`${this.baseUrl}authenticated`, { id: user?.recruiterId, accessToken: token });
  }

  public hasRole(role: ERole): boolean {
    return this.tokenStrorageService.getUserJwtResponse().role.some(userRoles => ERole[userRoles] == role);
  }

  public hasRoles(roles: ERole[]): boolean {
    const userRoles = this.tokenStrorageService.getUserJwtResponse().role;
    return roles.some(role => userRoles.includes(role));
  }

  public getUserRoles(): ERole[] {
    const userRoles = this.tokenStrorageService.getUserJwtResponse().role;
    return userRoles.map(role => ERole[role]);
  }

  public forgotPassword(email: string): Observable<{ message: string }> {
    return this.http.get<{ message: string }>(`${this.baseUrl}forgot-password`, { params: { email } });
  }

  public resetPassword(token: string, password: string): Observable<{ message: string }> {
    return this.http.get<{ message: string }>(`${this.baseUrl}reset-password`, { params: { token, password } });
  }

  // public register(user: Recruiter): Observable<string> {
  //   return this.http.post<string>(`${this.baseUrl}register`, user);
  // }

  public logout(): void {
    localStorage.removeItem('auth-user');
    localStorage.removeItem('auth-token');
    this.router.navigate(['/login']);
  }

}
