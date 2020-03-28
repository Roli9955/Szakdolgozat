import { Injectable } from '@angular/core';
import { HttpService } from './http.service';

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

}
