import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Holiday } from '../classes/holiday';

@Injectable({
  providedIn: 'root'
})
export class HolidayService {

  private url: string = "holiday";

  constructor(
    private httpService: HttpService
  ) { }

  getYears(): Promise<number[]>{
    return this.httpService.get(this.url + "/year");
  }

  addHoliday(userId: number, holiday: Holiday){
    return this.httpService.post(this.url + "/add/user/" + userId, holiday)
  }

  deleteHoliday(holidayId: number): Promise<Holiday>{
    return this.httpService.delete(this.url + "/delete/" + holidayId);
  }

}
