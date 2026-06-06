import { UserDetails } from "../common/user-details";

export interface AuthToken {
    token:string;
    expiresIn:number;
    user: UserDetails;
}
