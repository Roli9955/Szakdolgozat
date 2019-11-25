import { UserActivity } from './user-activity';

export class Activity {

    public id: number;
    public description: string;
    public isTask: boolean;
    public min: number;
    public deadline: Date;
    public isCompleted: boolean;
    public locked: boolean;
    public date: Date;
    public userActivity: UserActivity[];

}
