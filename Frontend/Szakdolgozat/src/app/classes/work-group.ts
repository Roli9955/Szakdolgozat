import { ActivityGroup } from './activity-group';

export class WorkGroup {

    public id: number;
    public name: string;
    public scale: number;
    public activityGroup: ActivityGroup[];

    public userCount: number;
    public activityGroupCount: number;

    public visible: boolean;

}
