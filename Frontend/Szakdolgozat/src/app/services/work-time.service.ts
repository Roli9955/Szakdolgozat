import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Activity } from '../classes/activity';

@Injectable({
  providedIn: 'root'
})
export class WorkTimeService {

  private url: string = "activity";

  constructor(
    private httpService: HttpService
  ) { }

  public getActivityByDate(year: number, month: number, day: number) : Promise<Activity[]>{
    return this.httpService.get(this.url + "/year/" + year + "/month/" + month + "/day/" + day);
  }

}
