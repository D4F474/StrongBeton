import { Exercise } from "./exercise";
import { Sets } from "./sets";

export class WorkoutDetails {
    
    constructor(public id: number,
                public exercise: Exercise,
                public muscleGroup:string
    ){      
        
    }


    
}
