import { Component, OnInit, ElementRef } from '@angular/core';

@Component({
  selector: 'app-upload-work-time',
  templateUrl: './upload-work-time.component.html',
  styleUrls: ['./upload-work-time.component.css']
})
export class UploadWorkTimeComponent implements OnInit {

  private months: string[] = ["Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"];

  constructor(
  ) { }

  ngOnInit() {
  }

  selectDate(year: number, month: number, day: number){
    const date = document.querySelector("#date");
    date.innerHTML = year + ". " + this.months[month - 1] + " " + day + ".";
  }

}
