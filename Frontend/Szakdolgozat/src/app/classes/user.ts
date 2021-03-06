import { Permission } from './permission';
import { Holiday } from './holiday';
import { Activity } from './activity';
import { UserWorkGroup } from './user-work-group';

export class User {

    public id: number;
    public firstName: string;
    public lastName: string;
    public email: string;
    public loginName: string;
    public password: string;
    public lastLogin: Date;
    public maxHoliday: number;
    public canLogIn: boolean;
    public permission: Permission;
    public holidays: Holiday[];
    public activitys: Activity[];
    public userWorkGroup: UserWorkGroup[];

    public onHolidays: number;
    public selected: boolean;

}
