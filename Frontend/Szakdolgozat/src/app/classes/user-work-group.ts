import { WorkGroup } from './work-group';
import { User } from './user';

export class UserWorkGroup {

    public id: number;
    public inWorkGroupFrom: Date;
    public inWorkGroupTo: Date;
    public workGroup: WorkGroup;
    public user: User;

}
