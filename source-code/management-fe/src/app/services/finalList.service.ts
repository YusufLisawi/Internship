import { Injectable } from '@angular/core';
import { Candidate } from '../models/Candidate.model'
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable(
  {
    providedIn: 'root'
  })

export class FinalListService {

  baseUrl = `${environment.apiUrl}`;

  constructor(private httpClient: HttpClient) { }

  public nbrTimes(email: String) {
    return this.httpClient.get(`${this.baseUrl}/nbrTimes?email=${email}`);
  }

  getScoreByCId(candidateId: number) {
    return this.httpClient.get(`${this.baseUrl}/getScoreByCId/${candidateId}`);
  }

  public sendEmailAdmittedCandidate(emails : string[]): Observable<string>{
    return this.httpClient.post<string>(`${this.baseUrl}candidate/admitted-candidates`,emails);
  }

}
