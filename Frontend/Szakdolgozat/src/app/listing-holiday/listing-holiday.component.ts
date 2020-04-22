import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { UserService } from '../services/user.service';
import { User } from '../classes/user';
import { HolidayService } from '../services/holiday.service';
import { Holiday } from '../classes/holiday';

@Component({
  selector: 'app-listing-holiday',
  templateUrl: './listing-holiday.component.html',
  styleUrls: ['./listing-holiday.component.css']
})
export class ListingHolidayComponent implements OnInit {

  public users: User[];
  public years: number[];
  public holidays: Holiday[];

  public filter = new FormGroup({
    user: new FormControl(),
    year: new FormControl()
  });

  constructor(
    private userService: UserService,
    private holidayService: HolidayService
  ) { }

  ngOnInit() {
    this.clearForm();
    this.update();
  }

  async update(){
    this.users = await this.userService.getUsers();
    this.years = await this.holidayService.getYears();
    this.holidays = await this.holidayService.getHolidays().then(res => {
      res.forEach(h => {
        h.visible = true;
      })

      this.users.forEach(user => {
        res.forEach(h => {
          if(user.id == h.userId){
            h.user = user;
          }
        })
      });
      return res;
    });

    //this.fillTable();
  }

  clearForm(){
    this.filter.controls['user'].setValue(-1);
    this.filter.controls['year'].setValue(-1);
  }

  filterData(){
    const user = this.filter.controls['user'].value;
    const year = this.filter.controls['year'].value;

    this.holidays.forEach(h => {
      h.visible = true;
    })

    if(user != -1){
      this.holidays.forEach(h => {
        if(h.user.id != user){
          h.visible = false;
        }
      })
    }

    if(year != -1){
      this.holidays.forEach(h => {
        let fromYear = new Date(h.holidayFrom).getFullYear();
        if(fromYear != year){
          h.visible = false;
        }
      })
    }
  }

  resetFilet(){
    this.clearForm();
    this.filterData();
  }

  makeExcel(){
    const user = this.filter.controls['user'].value;
    const year = this.filter.controls['year'].value;

    this.holidayService.makeExcelFile(user, year);
  }

}
