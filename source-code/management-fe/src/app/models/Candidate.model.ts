import { Interview } from "./Interview.model";
import { Level } from "./Level.enum";
import { Score } from "./Score.model";
import { Session } from "./Session.model";

export class Candidate {
    candidateId?: number;
  
    constructor( 
      public firstName: string,
      public lastName: string,
      public email: string,
      public phoneNumber: string,
      public city: string,
      public levelOfStudy?: Level,
      public anapec?: string,
      public cvname?: string,
      public cvtype?: string,
      public data?: Blob,
      public gender?: string,
      public prelemenaryAdmissionStatus?: string,
      public finalAdmissionStatus?: string,
      public firstTime?: string,
      public diplome?: string,
      public preselectionStatus?: string,
      public timeOfInterview?: string,
      public presence?: string,
      public url?: string,
      public university?: string,
      public interview?: Interview,
      public session?: Session,
      public score?: Score) { }
  }
  