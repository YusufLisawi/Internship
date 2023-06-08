import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageResponse } from '@models/MessageResponse.model';
import { Score } from '@models/Score.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ScoreService {

  baseUrl = `${environment.apiUrl}score`;

  constructor(private httpClient : HttpClient) { }

  public addScore(score : Score) : Observable<Score>{
    return this,this.httpClient.post<Score>(`${this.baseUrl}/add`,score);
  }

  public updateScore(score : Score) : Observable<Score>{
    return this.httpClient.put<Score>(`${this.baseUrl}/update`,score);
  }
}
