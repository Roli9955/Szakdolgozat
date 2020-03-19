import { ActivityGroup } from './activity-group';
import { WorkGroup } from './work-group';

export class Activity {

    public id: number;
    public description: string;
    public isTask: boolean;
    public min: number;
    public deadline: Date;
    public isCompleted: boolean;
    public locked: boolean;
    public date: Date;
    public activityGroup: ActivityGroup;
    public workGroup: WorkGroup;
    
}
