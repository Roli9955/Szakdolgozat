import { User } from './user';

export class ActivityPlan {

    public id: number;
    public title: string;
    public description: string;
    public date: Date;
    public user: User;
    public owner: User;

    public added: boolean;

}
