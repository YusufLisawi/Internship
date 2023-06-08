import { Role } from "./Role.model";

export class JwtResponse{
    
    recruiterId?: number;
    username?: string;
    email?: string;
    role?: string[];
    erole?: Role[];
    accessToken?: string;
    type?: string = "Bearer";

}