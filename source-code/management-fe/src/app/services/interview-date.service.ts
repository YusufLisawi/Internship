import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class InterviewDateService {

  date : Date[] = [];

  constructor() { }

  public getDate() : Date[] {
    return this.date;
  }

  public setDate(date : Date[]) : void {
    this.date = date;
  }
}
