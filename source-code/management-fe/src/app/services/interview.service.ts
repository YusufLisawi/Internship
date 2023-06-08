import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Interview } from '@models/Interview.model';
import { TokenStorageService } from './token-storage.service';
import { DatePipe } from '@angular/common';
import { MessageResponse } from '@models/MessageResponse.model';

@Injectable({ providedIn: 'root' })

export class InterviewService {

  baseUrl = `${environment.apiUrl}interview`;

  constructor(private httpClient: HttpClient, private tokenService: TokenStorageService) { }

  public getAll(): Observable<Interview[]> {
    return this.httpClient.get<Interview[]>(`${this.baseUrl}/all`);
  }

  public getByCurrentSession(sessionStatus: string): Observable<Interview[]> {
    return this.httpClient.get<Interview[]>(`${this.baseUrl}/findByCurrentSession`,  { params: { sessionStatus } } );
  }

  public addInterview( dates : Date[] ): Observable<Interview> {
    return this.httpClient.post<Interview>(`${this.baseUrl}/add`, dates);
  }

  public updateInterview( interview: Interview ): Observable<Interview> {
    return this.httpClient.put<Interview>(`${this.baseUrl}/update`, interview);
  }
  
  public deleteCandidate( interviewId: number ): Observable<MessageResponse> {
    return this.httpClient.delete<MessageResponse>(`${this.baseUrl}/delete/${interviewId}`);
  }

  public sendInvitation(emails : string[]): Observable<string[]>{
    return this.httpClient.post<string[]>(`${this.baseUrl}/invitation`, emails);
  }

  public getByDateInterview(date: Date): Observable<number[]> {
    const formattedDate = new DatePipe('en-US').transform(date, 'yyyy-MM-dd');
    const url = `${this.baseUrl}/findByDateInterview?date=${formattedDate}`;
    return this.httpClient.get<number[]>(url);
}
}
