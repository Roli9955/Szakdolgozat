import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { ActivityGroup } from '../classes/activity-group';
import { WorkGroup } from '../classes/work-group';

@Injectable({
  providedIn: 'root'
})
export class ActivityGroupService {

  private url: string = "activity-group";

  constructor(
    private httpService: HttpService
  ) { }

  getAllActivityGroup(): Promise<ActivityGroup[]>{
    return this.httpService.get(this.url)
  }

  getAllActivityGroupWithOutEasyLogIn(): Promise<ActivityGroup[]>{
    return this.httpService.get(this.url + "/no/easy-log-in")
  }

  getAllEasyLogInActivityGroups(): Promise<ActivityGroup[]>{
    return this.httpService.get(this.url + "/easy-log-in")
  }

  addNewActivityGroup(activityGroup: ActivityGroup): Promise<ActivityGroup>{
    return this.httpService.post(this.url + "/add", activityGroup);
  }

  addNewActivityGroupForEasyLogIn(activityGroup: ActivityGroup): Promise<ActivityGroup>{
    return this.httpService.post(this.url + "/add/easy-log-in", activityGroup);
  }

  deleteActivityGroup(id: number): Promise<ActivityGroup>{
    return this.httpService.delete(this.url + "/" + id);
  }

  editWorkGroupActivityGroups(workGroup: WorkGroup): Promise<WorkGroup>{
    return this.httpService.put(this.url + "/edit/work-group", workGroup)
  }

}
