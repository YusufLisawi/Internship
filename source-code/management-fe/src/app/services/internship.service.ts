import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Internship } from '@models/Internship.model';
import { MessageResponse } from '@models/MessageResponse.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InternshipService {

  baseUrl = `${environment.apiUrl}internship`;

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<Internship[]> {
    return this.httpClient.get<Internship[]>(`${this.baseUrl}/all`);
  }

  public getCurrentSessionInternships(): Observable<Internship[]> {
    return this.httpClient.get<Internship[]>(`${this.baseUrl}/allCurrentInternshipSession`);
  }  

  public add(internship : Internship): Observable<MessageResponse> {
    return this.httpClient.post<MessageResponse>(`${this.baseUrl}/add`, internship);
  }

  public update(internship : Internship): Observable<any> {
    return this.httpClient.put<any>(`${this.baseUrl}/update`, internship);
  }
  
  public delete( internshipId: number ): Observable<MessageResponse> {
    return this.httpClient.delete<MessageResponse>(`${this.baseUrl}/delete/${internshipId}`);
  }

  public getInternshipById(internshipId: number): Observable<Internship> {
    return this.httpClient.get<Internship>(
      `${this.baseUrl}/find/` + internshipId
    );
  }
  


}
