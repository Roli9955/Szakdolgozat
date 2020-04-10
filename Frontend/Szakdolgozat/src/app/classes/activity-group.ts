import { Activity } from './activity';

export class ActivityGroup {

    public id: number;
    public parent: ActivityGroup;
    public name: string;
    public canAddChild: boolean;

}
