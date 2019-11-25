import { User } from './user';
import { PermissionDetail } from './permission-detail';

export class Permission {

    public id: number;
    public name: string;
    public users: User[];
    public details: PermissionDetail[];

}
