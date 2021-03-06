import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { User } from '../classes/user';
import { Holiday } from '../classes/holiday';
import { HolidayService } from '../services/holiday.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  public holidays: Holiday[];

  constructor(
    public authUser: AuthService,
    private holidayService: HolidayService
  ) {
  }
  
  ngOnInit() {
    this.load();
  }

  async load(){
    this.holidays = await this.holidayService.getOwnHolidays().then(res => {
      var year = new Date().getFullYear();
      var list: Holiday[] = [];
      res.forEach(h => {
        if(year == new Date(h.holidayFrom).getFullYear()){
          list.push(h);
        }
      })
      return list;
    });
  }
}
