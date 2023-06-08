import { map } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recruiter } from '../models/Recruiter.model';
import { environment } from 'src/environments/environment';
import { Role } from '@models/Role.model';
import { ERole } from '@models/Role.enum';
import { MessageResponse } from '@models/MessageResponse.model';

@Injectable({
  providedIn: 'root'
})
export class RecruiterService {

  roles: Role[];
  baseURL: string = `${environment.apiUrl}recruiter`;

  constructor(private http: HttpClient) {
    this.getRoles().subscribe(
      res => {
        this.roles = res;
      }
    )
  }

  public getAll(): Observable<Recruiter[]> {
    return this.http.get<Recruiter[]>(`${this.baseURL}/all`);
  }

  public getArchived(): Observable<Recruiter[]> {
    return this.http.get<Recruiter[]>(`${this.baseURL}/archived`);
  }

  public getRecruitersTypeUser(): Observable<Recruiter[]> {
    return this.http.get<Recruiter[]>(`${this.baseURL}/findRecruitersTypeUser`);
  }

  public getById(id: number): Observable<Recruiter> {
    return this.http.get<Recruiter>(`${this.baseURL}/find/${id}`);
  }

  public getByEmail(email: String): Observable<Recruiter> {
    return this.http.get<Recruiter>(`${this.baseURL}/findByEmail/${email}`);
  }

  public add(recruiter: Recruiter): Observable<string> {
    if(recruiter.role == null){
      recruiter.role = this.roles.filter(r => recruiter.eRole.includes(ERole[r.name]));
      const roleKeys = Object.keys(ERole);
      const roleIndex = (role: ERole) => roleKeys.indexOf(role.toString()) + 1;
      const roles = recruiter.eRole.map((role) => (new Role(roleIndex(role), role.toString())))
      recruiter.role = roles;
    }
    console.log(recruiter);
    return this.http.post<string>(`${this.baseURL}/add`, recruiter);
  }

  public update(recruiter: Recruiter): Observable<Recruiter> {
    return this.http.put<Recruiter>(`${this.baseURL}/update`, recruiter);
  }

  public delete(recruiterId: number): Observable<MessageResponse> {
    return this.http.delete<MessageResponse>(`${this.baseURL}/delete/${recruiterId}`);
  }

  public restore(recruiterId: number): Observable<MessageResponse> {
    return this.http.delete<MessageResponse>(`${this.baseURL}/restore/${recruiterId}`);
  }

  public restoreByEmail(email: String): any{
    return this.http.get<any>(`${this.baseURL}/restore/${email}`);
  }

  private getRoles(): Observable<Role[]>{
    return this.http.get<Role[]>(`${this.baseURL}/all-roles`);
  }

}
