import { Component, OnInit, ElementRef } from '@angular/core';
import { WorkTimeService } from '../services/work-time.service';
import { UploadWorkTimeComponent } from '../upload-work-time/upload-work-time.component';
import { Activity } from '../classes/activity';

@Component({
  selector: 'app-work-time',
  templateUrl: './work-time.component.html',
  styleUrls: ['./work-time.component.css']
})
export class WorkTimeComponent implements OnInit {

  public days: string[] = ["Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"];
  public months: string[] = ["Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"];
  public activities: Activity[] = [];

  private actualYear: number = -1;
  private actualMonth: number = -1;
  private selectedDay: number = -1;

  private selected: boolean; 

  public selectedActivity: Activity;
  public selectedActivityHour: number;
  public selectedActivityMin: number;

  constructor(
    private ref: ElementRef,
    private workTimeService: WorkTimeService
    ) {
      const date = new Date();
      this.actualYear = date.getFullYear();
      this.actualMonth = date.getMonth() + 1;
      this.selectedDay = date.getDate();
      this.selected = true;
      this.selectedActivity = null;
      this.selectedActivityHour = -1;
      this.selectedActivityMin = -1;
  }

  ngOnInit() {
    this.generateCalendar();
    this.loadSelectedDay();
  }

  public generateCalendar() {
    const calendar = document.querySelector("#calendar");
    calendar.innerHTML = "";

    const firstDay = ((new Date(this.actualYear, this.actualMonth - 1, 1).getDay() + 6) % 7) + 1;
    const dayInMonth = new Date(this.actualYear, this.actualMonth, 0).getDate();
    console.log(firstDay, dayInMonth)
    var header = '';

    header += '<div class="row">';
    header += '<div class="col"><button id="prev" class="btn btn-light" ><</button></div>';
    header += '<div class="col text-center font-weight-bold"><h5>' + this.actualYear + '. ' + this.months[this.actualMonth - 1] + '</h5></div>';
    header += '<div class="col text-right"><button id="after" class="btn btn-light">></button></div>';
    header += '</div>';

    header += '</br><table class="table"><thead><tr>';
    this.days.forEach(day => {
      header += '<th scope="col">' + day + '</th>'
    });
    header += '</tr></thead><tbody>';
    var start = false;
    var day = 1;
    var lastDay = 1;
    for (let i = 1; lastDay <= dayInMonth; i++) {
      var row = '<tr>'
      for (let j = 1; j <= 7; j++) {
        if (start && day <= dayInMonth) {
          row += '<td class="text-center day">' + day + '</td>';
          day++;
          continue;
        }
        if (start && day >= dayInMonth) {
          row += '<td"></td>';
          continue;
        }
        if (!start && j == firstDay) {
          row += '<td class="text-center day">' + day + '</td>';
          day++;
          start = true;
        }
        if (!start && j != firstDay) {
          row += '<td></td>';
        }
      }
      row += '</tr>';
      lastDay += 7;
      console.log("ok")
      header += row;
    }
    header += '</tbody></table>';

    calendar.innerHTML += header;


    const prev = this.ref.nativeElement.querySelector("#prev");
    const after = this.ref.nativeElement.querySelector("#after");
    prev.addEventListener("click", this.changeMonth.bind(this));
    after.addEventListener("click", this.changeMonth.bind(this));

    this.setDateTable();
  
  }

  changeMonth(event) {
    this.selected = false;
    var direct;
    if (event.target.id == "prev") {
      direct = -1;
    } else {
      direct = 1;
    }
    if (this.actualMonth == 1 && direct == -1) {
      this.actualYear--;
      this.actualMonth = 12;
    } else if (this.actualMonth == 12 && direct == 1) {
      this.actualYear++;
      this.actualMonth = 1;
    } else {
      this.actualMonth += direct;
    }
    this.generateCalendar();
  }

  loadSelectedDay(){
    this.selectDate();
    this.setColors();
  }

  setDateTable(){
    const days = this.ref.nativeElement.querySelectorAll(".day")
    for (let i = 0; i < days.length; i++) {
      const day = days[i];
      day.style.backgroundColor = "#FFFFFF";
      day.addEventListener("click", () => {
        this.selectedDay = day.innerHTML;
        this.selected = true;
        this.loadSelectedDay();
      });
      day.style.cursor="pointer"
      day.style.userSelect="none"
      if((i == (this.selectedDay - 1)) && this.selected){
        day.style.backgroundColor = "#E6E6e6";
      }
    }
  }

  setColors(){
    const days = this.ref.nativeElement.querySelectorAll(".day")
    for (let i = 0; i < days.length; i++) {
      const day = days[i];
      day.style.backgroundColor = "#FFFFFF";
      if((i == (this.selectedDay - 1)) && this.selected){
        day.style.backgroundColor = "#E6E6e6";
      }
    }
  }

  async selectDate(){

    const date = document.querySelector("#date");
    date.innerHTML = this.actualYear + ". " + this.months[this.actualMonth - 1] + " " + this.selectedDay + ".";

    this.activities = await this.workTimeService.getActivityByDate(this.actualYear, this.actualMonth, this.selectedDay);
  }

  selectActivity(activity: Activity){
    this.selectedActivity = activity;
    this.selectedActivityHour = Math.floor(activity.min / 60)
    this.selectedActivityMin = activity.min % 60
    console.log(activity)
  }

}
