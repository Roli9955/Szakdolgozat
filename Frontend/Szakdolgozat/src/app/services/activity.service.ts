import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Activity } from '../classes/activity';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  private url: string = "activity";

  constructor(
    private httpService: HttpService
  ) { }

  addNewActivity(activity: Activity): Promise<Activity>{
    return this.httpService.post(this.url + "/new", activity);
  }

  deleteActivity(activityId: number): Promise<Activity>{
    return this.httpService.delete(this.url + "/delete/" +  activityId);
  }
}
