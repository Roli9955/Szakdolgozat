import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../classes/user';
import { HolidayService } from '../services/holiday.service';

@Component({
  selector: 'app-holiday',
  templateUrl: './holiday.component.html',
  styleUrls: ['./holiday.component.css']
})
export class HolidayComponent implements OnInit {

  public users: User[] = [];
  public years: number[] = [];

  constructor(
    private userService: UserService,
    private holidayService: HolidayService
  ) {}
  
  async ngOnInit() {
    this.generateTable();
    this.years = await this.holidayService.getYears();
  }

  async generateTable(){
    this.users = await this.userService.getUsersForHoliday().then(users => {
      const lines = document.querySelector("#usersHoliday");
      lines.innerHTML = "";
      
      var content = "";
      users.forEach(user => {
        content += "<tr><td><b>";
        content += user.lastName + " " + user.firstName;
        content += "</b></td>";
  
        var count = 0;

        user.holidays.forEach(holiday => {
          count += holiday.days;
        });

        content += "<td></td><td><b>" + count + "</b></td></tr>";

        user.holidays.forEach(holiday => {
          content += "<tr><td></td>";
          content += "<td>" + holiday.holidayFrom + " - " + holiday.holidayTo + "</td>";
          content += "<td>" + holiday.days + "</td></tr>";
        });
      });

      lines.innerHTML += content;
      return users;
    });
  }

}
