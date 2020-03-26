import { Component, OnInit, ElementRef, Inject } from '@angular/core';
import { WorkTimeService } from '../services/work-time.service';
import { Activity } from '../classes/activity';
import { WorkGroup } from '../classes/work-group';
import { UserWorkGroupService } from '../services/user-work-group.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivityService } from '../services/activity.service';
import { ActivityGroup } from '../classes/activity-group';
import { MatSnackBar, MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';


export interface SnackBarMsg{
  msg: string;
}

@Component({
  selector: 'app-work-time',
  templateUrl: './work-time.component.html',
  styleUrls: ['./work-time.component.css']
})
export class WorkTimeComponent implements OnInit {

  private duration: number = 3;

  public days: string[] = ["Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"];
  public months: string[] = ["Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"];
  public activities: Activity[] = [];

  public workGroups: WorkGroup[];
  public selectedWorkGroup: WorkGroup;

  private actualYear: number = -1;
  private actualMonth: number = -1;
  private selectedDay: number = -1;

  private selected: boolean; 

  public selectedActivity: Activity;
  public selectedActivityHour: number;
  public selectedActivityMin: number;

  public newActivity: Activity;
  public selectedForDelete: Activity;

  public activityForm = new FormGroup({
    workGroup: new FormControl(),
    activityGroup: new FormControl(),
    hour: new FormControl(),
    min: new FormControl(),
    comment: new FormControl()
  })

  constructor(
    private ref: ElementRef,
    private workTimeService: WorkTimeService,
    private userWorkGroupService: UserWorkGroupService,
    private activitySerevice: ActivityService,
    private snackBar: MatSnackBar 
    ) {
      const date = new Date();
      this.actualYear = date.getFullYear();
      this.actualMonth = date.getMonth() + 1;
      this.selectedDay = date.getDate();
      this.selected = true;
      this.selectedWorkGroup = new WorkGroup();
      this.newActivity = new Activity();
      this.newActivity.workGroup = new WorkGroup();
      this.newActivity.activityGroup = new ActivityGroup();
      this.setSelectedActivityToDefault();
      this.selectedForDeleteToDefault();
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
    header += '<div class="col text-center font-weight-bold"><h5>' + this.actualYear + '. ' + this.months[this.actualMonth - 1] + '</h5></div>';
    header += '<div class="col text-right"><button id="after" class="btn btn-light">></button></div>';
    header += '</div>';

    header += '</br><table class="table"><thead><tr class="text-center">';
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
    this.setSelectedActivityToDefault();
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

    this.updateForm();
    this.clearForm();
  }

  selectActivity(activity: Activity){
    this.selectedActivity = activity;
    this.selectedActivityHour = Math.floor(activity.min / 60)
    this.selectedActivityMin = activity.min % 60
  }

  selectWorkGroup(){
    const workGroupId = this.activityForm.controls["workGroup"].value;
    
    for(let workGroup of this.workGroups){
      if(workGroup.id == workGroupId){
        this.selectedWorkGroup = workGroup;
        this.activityForm.controls['activityGroup'].setValue(workGroup.activityGroup[0].id);
        return;
      }
    }
  }

  clearForm(){
    this.activityForm.controls['workGroup'].setValue('');
    this.activityForm.controls['activityGroup'].setValue('');
    this.activityForm.controls['hour'].setValue('');
    this.activityForm.controls['min'].setValue('');
    this.activityForm.controls['comment'].setValue('');

    this.selectedWorkGroup = new WorkGroup();
  }

  async addNewActivity(){
    const workGroup = this.activityForm.controls['workGroup'].value
    const activityGroup = this.activityForm.controls['activityGroup'].value
    const hour = this.activityForm.controls['hour'].value
    const min = this.activityForm.controls['min'].value
    const comment = this.activityForm.controls['comment'].value

    console.log(workGroup, activityGroup, hour, min, comment);

    if(!workGroup || !activityGroup || !hour || !min || !comment){
      this.sendMsg("Minden mező kitöltése kötelező!");
      return;
    }

    this.newActivity.workGroup.id = parseInt(workGroup);
    this.newActivity.activityGroup.id = parseInt(activityGroup);
    this.newActivity.min = (parseInt(hour) * 60) + parseInt(min);
    this.newActivity.description = comment;
    this.newActivity.isTask = false;
    this.newActivity.deadline = null;
    this.newActivity.isCompleted = false;
    this.newActivity.locked = false;
    this.newActivity.date = new Date(this.actualYear, this.actualMonth - 1, this.selectedDay);

    await this.activitySerevice.addNewActivity(this.newActivity);
    this.updateForm();

    this.sendMsg("Tevékenység sikeresen rögzítve");

    this.clearForm();
  }

  sendMsg(msg: string){
    this.snackBar.openFromComponent(WorkTimeComponentDialog, {
      duration: this.duration * 1000,
      data:{
        msg: msg
      }
    });
  }

  setSelectedActivityToDefault(){
    this.selectedActivity = null;
    this.selectedActivityHour = -1;
    this.selectedActivityMin = -1;
  }
  
  selectForDelete(activity: Activity){
    this.selectedForDelete = activity;
  }

  async delete() {

    console.log(this.activitySerevice.deleteActivity(this.selectedForDelete.id));

    this.updateForm();

    this.sendMsg("Tevékenység sikeresen törlésre került");
    this.selectedForDeleteToDefault();
    this.setSelectedActivityToDefault();
  }

  selectedForDeleteToDefault(){
    this.selectedForDelete = new Activity();
    this.selectedForDelete.activityGroup = new ActivityGroup();
    this.selectedForDelete.workGroup = new WorkGroup();
  }

  async updateForm(){
    this.activities = await this.workTimeService.getActivityByDate(this.actualYear, this.actualMonth, this.selectedDay);
    this.workGroups = await this.userWorkGroupService.getUserWorkGroups(this.actualYear, this.actualMonth, this.selectedDay);

    if(this.workGroups.length > 0){
      this.selectedWorkGroup = this.workGroups[0];
      this.activityForm.controls['workGroup'].setValue(this.selectedWorkGroup.id);
    } else {
      this.selectedWorkGroup = null;
    }
    this.selectWorkGroup();
  }

  editActivity(activity: Activity){
    this.updateForm();
    this.selectWorkGroup();
    this.activityForm.controls['workGroup'].setValue(activity.workGroup.id);
    this.activityForm.controls['activityGroup'].setValue(activity.activityGroup.id);
    this.activityForm.controls['hour'].setValue(Math.floor(activity.min / 60));
    this.activityForm.controls['min'].setValue(activity.min % 60);
    this.activityForm.controls['comment'].setValue(activity.description);
  }

}

@Component({
  selector: 'app-work-time-dialog',
  templateUrl: './work-time-dialog.component.html',
  styleUrls: ['./work-time.component.css']
})
export class WorkTimeComponentDialog {

  public msg: string;

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: SnackBarMsg
  ) {
    this.msg = this.data.msg;
  }

}