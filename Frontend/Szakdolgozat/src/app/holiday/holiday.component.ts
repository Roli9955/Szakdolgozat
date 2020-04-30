import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../classes/user';
import { HolidayService } from '../services/holiday.service';
import { FormGroup, FormControl } from '@angular/forms';
import { SnackComponent } from '../snack/snack.component';
import { Holiday } from '../classes/holiday';

@Component({
  selector: 'app-holiday',
  templateUrl: './holiday.component.html',
  styleUrls: ['./holiday.component.css']
})
export class HolidayComponent implements OnInit {

  public users: User[] = [];
  public years: number[] = [];

  public holidayForm = new FormGroup({
    year: new FormControl(),
    user: new FormControl(),
    from: new FormControl(),
    to: new FormControl()
  });

  public selectedYear: number;
  public selectedUser: User;

  public selectedForDelete: Holiday;
  public userForSelected: User;

  constructor(
    private userService: UserService,
    private holidayService: HolidayService,
    private snackBar: SnackComponent
  ) {
    this.selectedUser = new User();
  }
  
  ngOnInit() {
    this.update();
  }

  async update(){
    this.selectedForDelete = new Holiday();
    this.userForSelected = new User();

    this.years = await this.holidayService.getYears().then(years => {
      if(years.length > 0){
        this.selectedYear = years[0];
        this.holidayForm.controls["year"].setValue(this.selectedYear);
      }

      var actualYear = new Date().getFullYear();

      if(years.indexOf(actualYear) == -1){
        var tmpYear: number[] = [];
        tmpYear.push(actualYear);
        tmpYear = tmpYear.concat(years);
        this.selectedYear = actualYear;
        this.holidayForm.controls["year"].setValue(this.selectedYear);
        return tmpYear;
      } else {
        return years;
      }
    });
    this.generateTable();
  }

  async generateTable(){
    this.users = await this.userService.getUsersForHoliday().then(users => {
      const lines = document.querySelector("#usersHoliday");
      lines.innerHTML = "";

      if(users.length > 0){
        this.selectedUser = users[0];
        this.holidayForm.controls["user"].setValue(this.selectedUser.id);
      }
      
      var content = "";
      users.forEach(user => {
        content += "<tr><td><b>";
        content += user.lastName + " " + user.firstName;
        content += "</b></td>";
  
        var count = 0;

        user.holidays.forEach(holiday => {
          const year = new Date(holiday.holidayFrom).getFullYear();
          if(this.selectedYear == year){
            count += holiday.days;
          }
        });
        user.onHolidays = count;

        content += "<td></td><td><b>" + count + "</b></td></tr>";

        user.holidays.forEach(holiday => {
          const year = new Date(holiday.holidayFrom).getFullYear();
          if(this.selectedYear == year){
            content += "<tr><td></td>";
            content += "<td>" + holiday.holidayFrom + " - " + holiday.holidayTo + "</td>";
            content += "<td>" + holiday.days + "</td>";
            content += '<td><button class="btn btn-danger del" data-toggle="modal" data-target="#deleteHoliday">' + holiday.id + '</button></td></tr>';
          }
        });
      });

      lines.innerHTML += content;

      const buttons = document.querySelectorAll(".del");
      buttons.forEach(button => {
        const id = parseInt(button.innerHTML);
        button.innerHTML = "🗑️";
        button.addEventListener("click", () => this.selectForDelete(id));
      });

      return users;
    });
  }

  selectUser(){
    const userId = this.holidayForm.controls["user"].value;

    this.users.forEach(user => {
      if(user.id == userId){
        this.selectedUser = user;
      }
    });
  }

  selectYear(){
    const year = this.holidayForm.controls["year"].value;
    this.selectedYear = year;
    this.generateTable()
  }

  async addHoliday(){
    const from = this.holidayForm.controls["from"].value;
    const to = this.holidayForm.controls["to"].value;

    if(!from || !to){
      this.snackBar.sendMsg("Első és tulsó napot meg kell adni");
      return;
    }

    if(new Date(from) > new Date(to)){
      this.snackBar.sendMsg("Az első nap nem lehet nagyobb az utolsó napnál");
      return;
    }

    const fromDate = new Date(from).getFullYear();
    const toDate = new Date(to).getFullYear();

    if(fromDate != this.selectedYear){
      this.snackBar.sendMsg("Első napnál nem megfelelő az év");
      return;
    }

    if(toDate != this.selectedYear){
      this.snackBar.sendMsg("Utolsó napnál nem megfelelő az év");
      return;
    } 

    const newHoliday = new Holiday();
    newHoliday.holidayFrom = new Date(from);
    newHoliday.holidayTo = new Date(to);
    newHoliday.days = Math.ceil(Math.abs(newHoliday.holidayTo.getTime() - newHoliday.holidayFrom.getTime()) / (1000 * 60 * 60 * 24)) + 1;

    if(newHoliday.days > (this.selectedUser.maxHoliday - this.selectedUser.onHolidays)){
      this.snackBar.sendMsg("Nem adható ki több szabadság az engedélyezettnél");
      return;
    } else {
      await this.holidayService.addHoliday(this.selectedUser.id, newHoliday);
    }

    this.update();
    this.clearForm();
  }

  clearForm(){
    this.holidayForm.controls["from"].setValue('');
    this.holidayForm.controls["to"].setValue('');
  }

  selectForDelete(id: number){
    this.users.forEach(user => {
      user.holidays.forEach(holiday => {
        if(holiday.id == id){
          this.selectedForDelete = holiday;
          this.userForSelected = user;
        }
      });
    });
  }

  async delete(){
    await this.holidayService.deleteHoliday(this.selectedForDelete.id).then(() => {
      this.update();
    })
  }

}
