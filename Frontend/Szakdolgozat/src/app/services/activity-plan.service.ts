import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { ActivityPlan } from '../classes/activity-plan';

@Injectable({
  providedIn: 'root'
})
export class ActivityPlanService {

  private url: string = "activity-plan";

  constructor(
    private httpService: HttpService
  ) { }

  getUserPlans(): Promise<ActivityPlan[]> {
    return this.httpService.get(this.url + "/me");
  }

  addNewPlan(activityPlan: ActivityPlan): Promise<ActivityPlan> {
    return this.httpService.post(this.url + "/add", activityPlan);
  }

  deletePlan(planId: number): Promise<ActivityPlan> {
    return this.httpService.delete(this.url + "/delete/" + planId);
  }

  editPlan(plan: ActivityPlan): Promise<ActivityPlan>{
    return this.httpService.put(this.url + "/edit", plan);
  }

  getOwnedPlans(): Promise<ActivityPlan[]>{
    return this.httpService.get(this.url + "/me/owned");
  }
}
