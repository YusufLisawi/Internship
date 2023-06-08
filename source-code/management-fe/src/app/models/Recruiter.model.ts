import { ERole } from "./Role.enum";
import { Role } from "./Role.model";

export class Recruiter {
    recruiterId?: number;

    constructor(
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public password?: string,
        public picture?: string,
        public eRole?: ERole[],
        public username?: string,
        public phoneNumber?: string,
        public role?: Role[]) { }
}
