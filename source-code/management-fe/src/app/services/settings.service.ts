import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { ScoreConfig } from '@models/ScoreConfig.model';

@Injectable(
  {
    providedIn: 'root'
  })

export class SettingsService {

  private baseUrl = `${environment.apiUrl}scoreConfig/`;

  constructor(private httpClient: HttpClient) { }

  public getCurrent(): Observable<ScoreConfig> {
    return this.httpClient.get<ScoreConfig>(`${this.baseUrl}current-scoreConfig`);
  }

  public add(data): Observable<ScoreConfig> {
    return this.httpClient.post<ScoreConfig>(`${this.baseUrl}add`, data);
  }
}
