import { ActivityGroup } from './activity-group';
import { UserWorkGroup } from './user-work-group';

export class WorkGroup {

    public id: number;
    public name: string;
    public scale: number;
    public activityGroup: ActivityGroup[];
    public userWorkGroup: UserWorkGroup[];

}
