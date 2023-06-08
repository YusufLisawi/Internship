import { Recruiter } from './Recruiter.model';
import { Session } from './Session.model';
import { Technology } from './Technologie.model';

export class Interview {
    interviewId?: number;
  
    constructor( 
      public date: Date,
      public session?: Session,
      ) { }
  }
  