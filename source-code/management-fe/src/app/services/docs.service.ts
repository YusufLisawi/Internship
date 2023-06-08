import { Candidate } from "./../models/Candidate.model";
import { Observable } from "rxjs";
import { Docs } from "./../models/Docs.model";
import { Injectable } from "@angular/core";
import { environment } from "./../../environments/environment";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class DocsService {
  baseUrl = `${environment.apiUrl}docs`;

  constructor(private httpClient: HttpClient) {}

  public getAll(): Observable<Docs[]> {
    return this.httpClient.get<Docs[]>(`${this.baseUrl}/all`);
  }

  public add(document: Docs): Observable<Docs> {
    return this.httpClient.post<Docs>(`${this.baseUrl}/add`, document);
  }

  public update(document: Docs): Observable<Docs> {
    return this.httpClient.put<Docs>(`${this.baseUrl}/update`, document);
  }

  public delete(docId: number): Observable<string> {
    return this.httpClient.delete<string>(`${this.baseUrl}/delete/${docId}`);
  }

  public getByInternshipId(internshipId: number): Observable<Docs[]> {
    return this.httpClient.get<Docs[]>(
      `${this.baseUrl}/findByInternship/${internshipId}`
    );
  }

  public deleteAllByInternship(internshipId: number): Observable<string> {
    return this.httpClient.delete<string>(
      `${this.baseUrl}/deleteAll/${internshipId}`
    );
  }
}
