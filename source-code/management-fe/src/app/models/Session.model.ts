import { Time } from "@angular/common";
import { Recruiter } from "./Recruiter.model";

export class Session {
    sessionId?: number;

    constructor( 
        public sessionName: string,
        public sessionDate: Date,
        public technology:string,
        public sessionStatus?: string,
        public recruiter?:Recruiter,
        public dayInterviewNumber?: number,
        public admittedNumber?: number,
        public eliminatingMark?: number,
        public englishLevelRequire? : number,
        public startTimeOfInterview? : string,
        public endTimeOfInterview? : string,
        public testDuration?: number,
        public type?:string
        ) { }

  }
  