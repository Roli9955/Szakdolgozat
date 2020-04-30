import { Component, OnInit } from '@angular/core';
import { Activity } from '../classes/activity';
import { ActivityService } from '../services/activity.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../services/user.service';
import { User } from '../classes/user';
import { FormGroup, FormControl } from '@angular/forms';
import { SnackComponent } from '../snack/snack.component';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-taskbar',
  templateUrl: './taskbar.component.html',
  styleUrls: ['./taskbar.component.css']
})
export class TaskbarComponent implements OnInit {

  public tasks: Activity[];
  public today: number;

  public selectedForDelete: Activity;

  constructor(
    private activityService: ActivityService,
    private dialog: MatDialog
  ) { 
    var today = new Date();
		var dd = String(today.getDate()).padStart(2, '0');
		var mm = String(today.getMonth() + 1).padStart(2, '0');
		var yyyy = today.getFullYear();
    this.today = new Date(yyyy + "-" + mm + "-" + dd).getTime();
  }

  ngOnInit() {
    this.selectedForDelete = new Activity();
    this.update();
  }

  taskColor(date: Date){
    const activityDate = new Date(date).getTime();
    if(activityDate === this.today){
      return "bg-info";
    }
    if(activityDate > this.today){
      return "bg-white";
    }
    if(activityDate < this.today){
      return "bg-warning";
    }
  }

  async update(){
    this.tasks = await this.activityService.getAllTasks();
  }

  completeTask(activity: Activity){
    this.activityService.completeTask(activity).then(() => {
      this.update();
    });
  }

  taskSelectForDelete(task: Activity){
    this.selectedForDelete = task;
  }

  delete(){
    this.activityService.deleteTask(this.selectedForDelete.id).then(() => {
      this.update();
    });
  }

  newTask(){
    const dialogRef = this.dialog.open(TaskbarComponentDialog, {
      width: '30%'
    });

    dialogRef.afterClosed().subscribe(() => {
      this.update();
    })
  }

}

@Component({
  templateUrl: './taskbar.component.dialog.html',
  styleUrls: ['./taskbar.component.css']
})
export class TaskbarComponentDialog {
  
  public users: User[] = [];

  public taskForm = new FormGroup({
    date: new FormControl(),
    msg: new FormControl(),
    user: new FormControl()
  });

  constructor(
    private dialogRef: MatDialogRef<TaskbarComponentDialog>,
    private userService: UserService,
    private activityService: ActivityService,
    private snackBar: SnackComponent,
    private authService: AuthService
  ){
    this.load();
  }

  async load(){
    if(this.havePermission('ROLE_ADD_TASK')){
      this.users = await this.userService.getUsers();
    }

    var today = new Date();
		var dd = String(today.getDate()).padStart(2, '0');
		var mm = String(today.getMonth() + 1).padStart(2, '0');
		var yyyy = today.getFullYear();

		this.taskForm.controls['date'].setValue(yyyy + "-" + mm + "-" + dd);
  }

  havePermission(permission: string){
    var l: boolean = false;
    this.authService.getUser().permission.details.forEach(p => {
      if(p.roleTag == permission){
        l = true;
        return;
      }
    });

    return l;
  }

  save(){
    const date = this.taskForm.controls["date"].value;
    const msg = this.taskForm.controls["msg"].value;
    const user = this.taskForm.controls["user"].value;

    if(!date || !msg){
      this.snackBar.sendMsg("Dátum és megjegyzés megadása kötelező");
      return;
    }

    let activity: Activity = new Activity();
    activity.date = date;
    activity.deadline = date;
    activity.description = msg;
    if(this.havePermission('ROLE_ADD_TASK')){
      activity.user = new User();
      activity.user.id = user;
    }

    this.activityService.addTask(activity).then(() => {
      this.snackBar.sendMsg("Feladat sikeresen mentésre került");
      this.dialogRef.close();
    });

  }

}
