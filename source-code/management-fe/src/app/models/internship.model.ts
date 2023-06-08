import { Candidate } from "./Candidate.model";
import { Session } from "./Session.model";
import { EInternshipType } from "./InternshipType.enum";

export class Internship {

    internshipId?: number;

    constructor(
        public subject:string,
        public startDate:Date,
        public endDate:Date,
        public type: EInternshipType,
        public supervisor: string,
        public supervisorPhone: string,
        public supervisorEmail: string,
        public internshipStatus?: string,
        public internshipRating?:number,
        public reportRating?:number,
        public session?:Session,
        public candidate?:Candidate
    ){}


}
