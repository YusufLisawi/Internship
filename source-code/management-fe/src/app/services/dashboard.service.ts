import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";
import { DashboardStatistics } from "@models/DashboardStatistics.model";
import { Router } from "@angular/router";

@Injectable({
  providedIn: "root",
})
export class DashboardService {
  baseUrl = `${environment.apiUrl}dashboard`;

  constructor(private httpClient: HttpClient, private router: Router) {}

  public getStatistics(sessionId: number): Observable<DashboardStatistics> {
    if (
      this.router.routerState.snapshot.root.children[0].url[0].path ==
      "internship"
    )
      return this.httpClient.get<DashboardStatistics>(
        `${this.baseUrl}/getInternshipStatistics`,
        { params: { sessionId } }
      );
    return this.httpClient.get<DashboardStatistics>(
      `${this.baseUrl}/getStatistics`,
      { params: { sessionId } }
    );
  }
  public getStatisticsOfLatestSession(): Observable<DashboardStatistics> {
    if (
      this.router.routerState.snapshot.root.children[0].url[0].path ==
      "internship"
    )
      return this.httpClient.get<DashboardStatistics>(
        `${this.baseUrl}/getStatisticsOfLatestInternshipSession`
      );
    return this.httpClient.get<DashboardStatistics>(
      `${this.baseUrl}/getStatisticsOfLatestSession`
    );
  }

  public countAdmittedPreviousSession(): Observable<number> {
    if (
      this.router.routerState.snapshot.root.children[0].url[0].path ==
      "internship"
    )
      return this.httpClient.get<number>(
        `${this.baseUrl}/countAdmittedPreviousInternshipSession`
      );
    return this.httpClient.get<number>(
      `${this.baseUrl}/countAdmittedPreviousSession`
    );
  }

  public getTotalNumber() {
    return this.httpClient.get(`${this.baseUrl}/getTotalNumber`);
  }

  public getPreselctedNumber() {
    return this.httpClient.get(`${this.baseUrl}/getPreselctedNumber`);
  }

  public getAdmittedNumber() {
    return this.httpClient.get(`${this.baseUrl}/getAdmittedNumber`);
  }

  public getAdmitted() {
    return this.httpClient.get(`${this.baseUrl}/getAdmitted`);
  }

  public getWaiting() {
    return this.httpClient.get(`${this.baseUrl}/getWaiting`);
  }
}
