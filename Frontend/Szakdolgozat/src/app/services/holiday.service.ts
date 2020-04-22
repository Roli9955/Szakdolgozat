import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Holiday } from '../classes/holiday';
import { saveAs } from 'file-saver';

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

  getHolidays(): Promise<Holiday[]>{
    return this.httpService.get(this.url);
  }

  makeExcelFile(userId: number, year: number){
    return this.httpService.downloadFile(this.url + "/excel/user/" + userId + "/year/" + year).subscribe(response => {
      console.log(response.header)
      this.downLoadFile(response, "application/octet-stream");
    })
  }

  downLoadFile(data: any, type: string) {
    let blob = new Blob([data], { type: type });
    saveAs(blob, "export.xlsx");
  }
}
