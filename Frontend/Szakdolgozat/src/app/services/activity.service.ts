import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Activity } from '../classes/activity';
import { saveAs } from 'file-saver';

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

  editActivity(activity: Activity): Promise<Activity>{
    return this.httpService.put(this.url + "/edit", activity);
  }

  getAllActivity(): Promise<Activity[]>{
    return this.httpService.get(this.url);
  }

  makeExcelFile(projectId: number, activityGroupId: number, userId: number){
    return this.httpService.downloadFile(this.url + "/excel/project/" + projectId + "/activity-group/" + activityGroupId + "/user/" + userId).subscribe(response => {
      this.downLoadFile(response, "application/octet-stream");
    })
  }

  downLoadFile(data: any, type: string) {
    let blob = new Blob([data], { type: type });
    saveAs(blob, "export.xlsx");
  }

  getAllTasks(): Promise<Activity[]>{
    return this.httpService.get(this.url + "/task/me");
  }

  completeTask(activity: Activity): Promise<Activity>{
    return this.httpService.put(this.url + "/task/complete", activity);
  }

  deleteTask(id: number): Promise<Activity>{
    return this.httpService.delete(this.url + "/delete/task/" + id)
  }

  addTask(activity: Activity): Promise<Activity>{
    return this.httpService.post(this.url + "/add/task", activity);
  }
}
