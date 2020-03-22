import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { WorkGroup } from '../classes/work-group';

@Injectable({
  providedIn: 'root'
})
export class UserWorkGroupService {

  private url: string = "user-work-group";

  constructor(
    private httpService: HttpService
  ) { }

  public getUserWorkGroups(year: number, month: number, day: number): Promise<WorkGroup[]>{
    return this.httpService.get(this.url + "/year/" + year + "/month/" + month + "/day/" + day);
  }
}
