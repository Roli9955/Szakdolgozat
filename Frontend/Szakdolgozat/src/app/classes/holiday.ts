import { User } from './user';

export class Holiday {

    public id: number;
    public holidayFrom: Date;
    public holidayTo: Date;
    public days: number;
    public userId: number;

    public user: User;
    public visible: boolean;
}
