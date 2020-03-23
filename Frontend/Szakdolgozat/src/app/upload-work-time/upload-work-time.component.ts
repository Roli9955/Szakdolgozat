import { Component, OnInit } from '@angular/core';
import { Activity } from '../classes/activity';
import { WorkTimeService } from '../services/work-time.service';

@Component({
  selector: 'app-upload-work-time',
  templateUrl: './upload-work-time.component.html',
  styleUrls: ['./upload-work-time.component.css']
})
export class UploadWorkTimeComponent implements OnInit {

  private months: string[] = ["Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"];

  public activities: Activity[] = [];

  private year: number;
  private month: number;
  private day: number;
  
  constructor(
    private workTimeService: WorkTimeService
  ) { }

  ngOnInit() {
  }

  async selectDate(year: number, month: number, day: number){

    this.year = year;
    this.month = month;
    this.day = day;

    const date = document.querySelector("#date");
    date.innerHTML = year + ". " + this.months[month - 1] + " " + day + ".";

    this.activities = await this.workTimeService.getActivityByDate(this.year, this.month, this.day);
    // console.log(this.activities)
  }

}
