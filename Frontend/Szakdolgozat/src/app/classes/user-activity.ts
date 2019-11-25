import { User } from './user';
import { Activity } from './activity';

export class UserActivity {

    public id: number;
    public user: User;
    public activity: Activity;
    public owner: User;

}
