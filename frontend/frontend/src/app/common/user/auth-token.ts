import { userDto } from "./user-dto";

export interface AuthToken {

    token:string;
    expiresIn:number;
    userDTO: userDto;
}
