import { Injectable } from '@angular/core';
import { WorkGroup } from '../classes/work-group';
import { HttpService } from './http.service';
import { UserWorkGroup } from '../classes/user-work-group';
import { User } from '../classes/user';

@Injectable({
  providedIn: 'root'
})
export class WorkGroupService {

  private url: string = "work-group"

  constructor(
    private httpService: HttpService
  ) { }

  getAllWorkGroup(): Promise<WorkGroup[]>{
    return this.httpService.get(this.url);
  }

  addNewWorkGroup(workGroup: WorkGroup): Promise<WorkGroup>{
    return this.httpService.post(this.url + "/add", workGroup);
  }

  addUserWorkGroups(workGroupId: number, userWorkGroups: UserWorkGroup[]): Promise<UserWorkGroup[]>{
    return this.httpService.post(this.url + "/add/" + workGroupId + "/user-work-group", userWorkGroups);
  }

  getUsersInProject(projetId: number): Promise<UserWorkGroup[]>{
    return this.httpService.get(this.url + "/" + projetId + "/users");
  }

  deleteWorkGroup(id: number): Promise<WorkGroup>{
    return this.httpService.delete(this.url + "/" + id)
  }

  editWorkGroup(workGroup: WorkGroup): Promise<WorkGroup>{
    return this.httpService.put(this.url + "/edit", workGroup);
  }

  editUserWorkGroup(workGroupId: number, userWorkGroups: UserWorkGroup[]): Promise<UserWorkGroup[]>{
    return this.httpService.put(this.url + "/edit/" + workGroupId + "/user-work-group", userWorkGroups);
  }
}
