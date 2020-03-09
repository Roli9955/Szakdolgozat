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

  // public calendar: string = "";

  private actualYear: number = -1;
  private actualMonth: number = -1;
  private selectedDay: number = -1;

  private selected: boolean; 

  constructor(
    private ref: ElementRef,
    private workTimeService: WorkTimeService
    ) {
      const date = new Date();
      this.actualYear = date.getFullYear();
      this.actualMonth = date.getMonth() + 1;
      this.selectedDay = date.getDate();
      this.selected = true;
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

    var header = '';

    header += '<div class="row">';
    header += '<div class="col"><button id="prev" class="btn btn-light" ><</button></div>';
    header += '<div class="col text-center font-weight-bold"><h3>' + this.actualYear + '. ' + this.months[this.actualMonth - 1] + '</h3></div>';
    header += '<div class="col text-right"><button id="after" class="btn btn-light">></button></div>';
    header += '</div>';

    header += '</br><div class="row">';
    this.days.forEach(day => {
      header += '<div class="col text-center"><h3>' + day + '</h3></div>'
    });
    header += '</div></br>';

    calendar.innerHTML += header;


    var start = false;
    var day = 1;
    var lastDay = 1;
    for (let i = 1; lastDay <= dayInMonth; i++) {
      var row = '<div class="row text-center">'
      for (let j = 1; j <= 7; j++) {
        if (start && day <= dayInMonth) {
          row += '<div class="col border day"><br><h4>' + day + '</h4><br></div>';
          day++;
          continue;
        }
        if (start && day >= dayInMonth) {
          row += '<div class="col border"></div>';
          continue;
        }
        if (start == false && j == firstDay) {
          row += '<div class="col day-col border day"><br><h4>' + day + '</h4><br></div>';
          day++;
          start = true;
        }
        if (start == false && j != firstDay) {
          row += '<div class="col border"></div>';
        }
      }
      row += '</div>'
      calendar.innerHTML += row;

      lastDay = day;
    }

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

  selectDay(day: number){
    this.selectedDay = day;
    this.selected = true;
    this.setDateTable();
    this.loadSelectedDay();
  }

  loadSelectedDay(){
    this.selectDate();
    console.log("aaa")
  }

  setDateTable(){
    const days = this.ref.nativeElement.querySelectorAll(".day")
    for (let i = 0; i < days.length; i++) {
      const day = days[i];
      day.style.backgroundColor = "#FFFFFF";
      day.addEventListener("click", this.selectDay(i));
      day.style.cursor="pointer"
      day.style.userSelect="none"
      if((i == (this.selectedDay - 1)) && this.selected){
        day.style.backgroundColor = "#E6E6e6";
      }
    }
  }

  async selectDate(){

    const date = document.querySelector("#date");
    date.innerHTML = this.actualYear + ". " + this.months[this.actualMonth - 1] + " " + this.selectedDay + ".";

    this.activities = await this.workTimeService.getActivityByDate(this.actualYear, this.actualMonth, this.selectedDay);
    console.log(this.activities)
  }

}
