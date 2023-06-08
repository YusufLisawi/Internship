import { RecruiterService } from '@services/recruiter.service';
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Session } from "@models/Session.model";
import {
  HttpClient,
  HttpResponse,
  HttpResponseBase,
} from "@angular/common/http";
import { environment } from "src/environments/environment";
import { TokenStorageService } from "./token-storage.service";
import { MessageResponse } from "@models/MessageResponse.model";
import { SessionRoutingModule } from "@modules/session/session-routing.module";
import { Router } from "@angular/router";

@Injectable({
  providedIn: "root",
})
export class SessionService {
  baseUrl = `${environment.apiUrl}session`;

  constructor(
    private httpClient: HttpClient,
    private tokenService: TokenStorageService,
    private router: Router,
    private recruiterService: RecruiterService
  ) {}

  public getAll(): Observable<Session[]> {
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.get<Session[]>(`${this.baseUrl}/internship/all`);
    return this.httpClient.get<Session[]>(`${this.baseUrl}/all`);
  }

  public getSessionsByStatus(sessionStatus: string): Observable<Session> {
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.get<Session>(
        `${this.baseUrl}/internship/findByStatus`,
        {
          params: { sessionStatus },
        }
      );
    return this.httpClient.get<Session>(`${this.baseUrl}/findByStatus`, {
      params: { sessionStatus },
    });
  }

  public getById(sessionId: number): Observable<Session> {
    return this.httpClient.get<Session>(`${this.baseUrl}/findById`, {
      params: { sessionId },
    });
  }

  public getByName(sessionName: string): Observable<Session> {
    return this.httpClient.get<Session>(`${this.baseUrl}/findByName`, {
      params: { sessionName },
    });
  }

  public getAllTechnologies() {
    return this.httpClient.get<string[]>(`${this.baseUrl}/allTechnologies`);
  }

  public add(session: Session): Observable<{ message: string }> {
    session.recruiter = {
      recruiterId: this.tokenService.getUser().recruiterId,
    };
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.post<{ message: string }>(
        `${this.baseUrl}/internship/add`,
        session
      );
    return this.httpClient.post<{ message: string }>(
      `${this.baseUrl}/add`,
      session
    );
  }

  public update(session: Session): Observable<MessageResponse> {
    return this.httpClient.put<MessageResponse>(
      `${this.baseUrl}/update`,
      session
    );
  }

  public updateSessionPrevious(): Observable<boolean> {
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.put<boolean>(
        `${this.baseUrl}/internship/update-session-previous`,
        {}
      );
    return this.httpClient.put<boolean>(
      `${this.baseUrl}/update-session-previous`,
      {}
    );
  }

  public updateSessionClose(): Observable<boolean> {
    if (this.router.routerState.snapshot.root.children[0].url[0].path == "internship")
      return this.httpClient.put<boolean>(
        `${this.baseUrl}/internship/update-session-close`,
        {}
      );
    return this.httpClient.put<boolean>(
      `${this.baseUrl}/update-session-close`,
      {}
    );
  }

  public updateInterviewNumber(interviewNumber: number): Observable<boolean> {
    return this.httpClient.get<boolean>(
      `${this.baseUrl}/update-interview/${interviewNumber}`
    );
  }

  public updateAdmittedNumber(session: Session): Observable<Session> {
    return this.httpClient.put<Session>(
      `${this.baseUrl}/update-interview-info`,
      session
    );
  }

  public updateDailyInterviews(session: Session): Observable<Session> {
    return this.httpClient.put<Session>(
      `${this.baseUrl}/update-daily-interview`,
      session
    );
  }
}
