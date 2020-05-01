import { Component, OnInit, Inject } from '@angular/core';
import { ActivityPlan } from '../classes/activity-plan';
import { ActivityPlanService } from '../services/activity-plan.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl } from '@angular/forms';
import { SnackComponent } from '../snack/snack.component';
import { UserService } from '../services/user.service';
import { User } from '../classes/user';
import { AuthService } from '../services/auth.service';

export interface DialogData {
  plan: ActivityPlan
}

@Component({
  selector: 'app-work-time-planner',
  templateUrl: './work-time-planner.component.html',
  styleUrls: ['./work-time-planner.component.css']
})
export class WorkTimePlannerComponent implements OnInit {

  private days: string[] = ["Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"];
  private months: string[] = ["Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"];

  public plans: ActivityPlan[];

  public date: Date;

  public year: number;
  public week: number;

  constructor(
    private activityPlanService: ActivityPlanService,
    private dialog: MatDialog,
    private authService: AuthService
    
  ) {
    this.date = new Date();
  }

  ngOnInit() {
    this.update();
  }

  async update() {
    this.plans = await this.activityPlanService.getUserPlans().then(res => {
      res.forEach(p => {
        p.added = false;
      });
      return res;
    });

    this.fillTable();
  }

  fillTable() {
    this.year = this.date.getFullYear();
    const data = document.querySelector("#data");
    const weekOfTheDate = this.date.getDay();
    this.date.setDate(this.date.getDate() - (weekOfTheDate - 1));

    data.innerHTML = "";

    let row = "";
    row += '<thead><tr>'
    row += '<th scope="col" class="text-center"><button id="addPlan" class="btn btn-light">Terv hozzáadása</button></th>';
    for (let i = 0; i < 7; i++) {
      row += '<th scope="col" class="text-center">';
      row += this.months[this.date.getMonth()] + ' ' + this.date.getDate() + '<br>' + this.days[i];
      row += '</th>';
      this.setDate(+1);
    }
    this.setDate(-7);
    row += '</tr></thead><tbody>'


    for (let i = 0; i < 24; i++) {
      for (let j = 0; j < 4; j++) {
        row += '<tr><th scope="row" class="text-center" style="width: 9%">';
        row += ((i < 10) ? '0' + i : i) + ':' + ((j * 15) == 0 ? '00' : (j * 15))
        row += '</th>';
        for (let k = 0; k < 7; k++) {

          let year = this.date.getFullYear();
          let month = this.date.getMonth() + 1;
          let day = this.date.getDate();
          row += '<td style="width: 13%"><ul>';
          this.plans.forEach(plan => {
            if (!plan.added) {
              let planDate = new Date(plan.date);
              let planYear = planDate.getFullYear();
              let planMonth = planDate.getMonth() + 1;
              let planDay = planDate.getDate();
              let planHour = planDate.getHours();
              let planMin = planDate.getMinutes()
              if (year == planYear && month == planMonth && day == planDay && planHour == i && planMin == (j * 15)) {
                row += '<li class="plan" id="' + plan.id + '">' + plan.title + '</li>'
                plan.added = true;
              }
            }
          });
          row += '</ul></td>';
          this.setDate(+1);
        }
        row += '</tr>'
        this.setDate(-7);
      }
    }
    row += '</tbody>';
    data.innerHTML = row;

    const button = document.querySelector("#addPlan");
    button.addEventListener("click", this.addPlan.bind(this));
    const plansLI = document.querySelectorAll(".plan");
    plansLI.forEach(pLI => {
      this.plans.forEach(plan => {
        if (plan.added) {
          if (parseInt(pLI.id) == plan.id) {
            pLI.addEventListener('click', () => {
              this.viewPlan(plan);
            })
          }
        }
      })
    })
  }

  setDate(direct: number) {
    this.date.setDate(this.date.getDate() + direct);
  }

  setWeek(direct: number) {
    switch (direct) {
      case -1:
        this.date.setDate(this.date.getDate() - 7);
        break;
      case 1:
        this.date.setDate(this.date.getDate() + 7);
        break;
    }
    this.update();
  }

  addPlan() {
    const ref = this.dialog.open(WorkTimePlannerDialog, {
      width: "30%",
      data: {
        plan: null
      }
    });

    ref.afterClosed().subscribe(() => {
      this.update();
    });
  }

  viewPlan(plan: ActivityPlan) {
    const ref = this.dialog.open(WorkTimePlannerDialog, {
      width: "30%",
      data: {
        plan: plan
      }
    });

    ref.afterClosed().subscribe(() => {
      this.update();
    });
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

  viewListing() {
    const ref = this.dialog.open(WorkTimePlannerListingDialog, {
      width: "60%"
    });

    ref.afterClosed().subscribe(() => {
      this.update();
    });
  }

}

@Component({
  templateUrl: './work-time-planner.dialog.html',
  styleUrls: ['./work-time-planner.component.css']
})
export class WorkTimePlannerDialog {

  public plan: ActivityPlan;
  public edit: boolean;
  public users: User[];

  public form = new FormGroup({
    title: new FormControl(),
    description: new FormControl(),
    date: new FormControl(),
    hour: new FormControl(),
    min: new FormControl(),
    user: new FormControl()
  });

  constructor(
    private dialogRef: MatDialogRef<WorkTimePlannerDialog>,
    @Inject(MAT_DIALOG_DATA) private data: DialogData,
    private snackBar: SnackComponent,
    private activityPlanService: ActivityPlanService,
    private userService: UserService,
    private authService: AuthService
  ) {
    this.plan = this.data.plan;
    this.edit = false;
    this.form.controls['hour'].setValue(0);
    this.form.controls['min'].setValue(0);

    this.load();
  }

  async load() {
    if (this.plan != null) {
      this.form.controls['title'].disable();
      this.form.controls['description'].disable();
      this.form.controls['date'].disable();
      this.form.controls['hour'].disable();
      this.form.controls['min'].disable();
      this.form.controls['user'].disable();

      let date = new Date(this.plan.date);
      let year = date.getFullYear();
      let month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
      let day = (date.getDate()) < 10 ? "0" + (date.getDate()) : (date.getDate());
      let hour = date.getHours();
      let min = date.getMinutes();

      this.form.controls['title'].setValue(this.plan.title);
      this.form.controls['description'].setValue(this.plan.description);
      this.form.controls['date'].setValue(year + "-" + month + "-" + day);
      this.form.controls['hour'].setValue(hour);
      this.form.controls['min'].setValue(min);
    } else {
      if(this.havePermission("ROLE_ACTIVITY_PLAN_ADMIN")){
        this.users = await this.userService.getUsers();
      } else {
        this.users = [];
      }
    }
  }

  save() {
    const title = this.form.controls['title'].value
    const description = this.form.controls['description'].value
    const date = this.form.controls['date'].value
    const hour = this.form.controls['hour'].value
    const min = this.form.controls['min'].value
    const user = this.form.controls['user'].value;

    if (!title || !date) {
      this.snackBar.sendMsg("Cím, dátum, óra és perc megadása kötelező");
      return;
    }

    if (this.plan == null) {
      this.plan = new ActivityPlan();
      this.plan.title = title;
      this.plan.description = description;
      this.plan.date = new Date(date);
      this.plan.date.setHours(hour);
      this.plan.date.setMinutes(min);
      
      if(user){
        this.plan.user = new User();
        this.plan.user.id = user;
      }

      this.activityPlanService.addNewPlan(this.plan).then(() => {
        this.dialogRef.close();
      })
    } else {
      this.plan.title = title;
      this.plan.description = description;
      this.plan.date = new Date(date);
      this.plan.date.setHours(hour);
      this.plan.date.setMinutes(min);

      this.activityPlanService.editPlan(this.plan).then(() => {
        this.snackBar.sendMsg("A terv módosításra került");
        this.dialogRef.close();
      });
    }
  }

  delete() {
    this.activityPlanService.deletePlan(this.plan.id);
    this.dialogRef.close();
  }

  enableEdit() {
    this.edit = true;

    this.form.controls['title'].enable();
    this.form.controls['description'].enable();
    this.form.controls['date'].enable();
    this.form.controls['hour'].enable();
    this.form.controls['min'].enable();
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

}

@Component({
  templateUrl: './work-time-planner.listing.dialog.html',
  styleUrls: ['./work-time-planner.component.css']
})
export class WorkTimePlannerListingDialog {

  public plans: ActivityPlan[] = [];

  constructor(
    private activityPlanService: ActivityPlanService,
    private snackBar: SnackComponent,
    private dialogRef: MatDialogRef<WorkTimePlannerListingDialog>
  ){
    this.load();
  }

  async load(){
    await this.activityPlanService.getOwnedPlans().then(res => {
      res.forEach(plan => {
        if(plan.owner.id != plan.user.id){
          this.plans.push(plan);
        }
      });
    });
  }

  delete(id: number){
    this.activityPlanService.deletePlan(id).then(() => {
      this.snackBar.sendMsg("A terv sikeresen törlésre került");
      this.dialogRef.close();
    })
  }

}

