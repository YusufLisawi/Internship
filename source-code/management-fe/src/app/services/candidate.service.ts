
import { Injectable } from "@angular/core";
import { Candidate } from "../models/Candidate.model";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { MessageResponse } from "@models/MessageResponse.model";
import { Score } from "@models/Score.model";
import { ActivatedRoute, Router } from "@angular/router";

@Injectable({ providedIn: "root" })
export class CandidateService {
  baseUrl = `${environment.apiUrl}candidate`;

  constructor(
    private httpClient: HttpClient,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) { }

  public getAll(): Observable<Candidate[]> {
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.get<Candidate[]>(`${this.baseUrl}/internship/all`);
    return this.httpClient.get<Candidate[]>(`${this.baseUrl}/all`);
  }

  public getCandidateById(candidateId: number): Observable<Candidate> {
    return this.httpClient.get<Candidate>(
      `${this.baseUrl}/find/` + candidateId
    );
  }

  public getByEmail(email: string): Observable<Candidate> {
    return this.httpClient.get<Candidate>(`${this.baseUrl}/findByEmail`, {
      params: { email },
    });
  }

  public getBySession(sessionId: number): Observable<Candidate[]> {
    return this.httpClient.get<Candidate[]>(
      `${this.baseUrl}/findBySession/${sessionId}`
    );
  }

  public getCountPreselected(): Observable<any> {
    return this.httpClient.get<any>(`${this.baseUrl}/preselected-count`);
  }

  public getFinalList(): Observable<Candidate[]> {
    return this.httpClient.get<Candidate[]>(`${this.baseUrl}/final-list`);
  }

  public getWaitingList(): Observable<Candidate[]> {
    return this.httpClient.get<Candidate[]>(`${this.baseUrl}/waiting-list`);
  }

  public add(candidate: Candidate): Observable<MessageResponse> {
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.post<MessageResponse>(
        `${this.baseUrl}/internship/add`,
        candidate
      );
    return this.httpClient.post<MessageResponse>(
      `${this.baseUrl}/add`,
      candidate
    );
  }

  public update(candidate: Candidate): Observable<any> {
    return this.httpClient.put<any>(`${this.baseUrl}/update`, candidate);
  }


  public moveWaitingListToSession(candidate: Candidate, sessionId: number): Observable<any> {
    return this.httpClient.put<any>(`${this.baseUrl}/moveWaitingListToSession/${sessionId}`, candidate);
  }

  public setPreselectionStatus(candidateId: number, preselectionStatus: boolean): Observable<MessageResponse> {
    return this.httpClient.get<MessageResponse>(`${this.baseUrl}/set-preselection-status`, { params: { candidateId, preselectionStatus } });
  }

  public delete(candidateId: number): Observable<MessageResponse> {
    return this.httpClient.delete<MessageResponse>(
      `${this.baseUrl}/delete/${candidateId}`
    );
  }

  public getUniversities(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.baseUrl}/allUniversities`);
  }

  public getDiplomas(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.baseUrl}/allDiplomas`);
  }

  public getCities(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.baseUrl}/allCities`);
  }

  public getAcceptedCandidate(): Observable<Candidate[]> {
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.get<Candidate[]>(`${this.baseUrl}/accepted-internship`);
    return this.httpClient.get<Candidate[]>(`${this.baseUrl}/accepted`);
  }

  public addScore(score: Score, candidateId: number): Observable<Candidate> {
    return this.httpClient.post<Candidate>(
      `${this.baseUrl}/save-score/${candidateId}`,
      score
    );
  }
  public getByInterviewId(interviewid: any): Observable<Candidate[]> {
    return this.httpClient.get<Candidate[]>(`${this.baseUrl}/findByInterview/${interviewid}`);
  }
}
