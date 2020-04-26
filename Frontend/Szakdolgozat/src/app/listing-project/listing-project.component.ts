import { Component, OnInit } from '@angular/core';
import { WorkGroup } from '../classes/work-group';
import { WorkGroupService } from '../services/work-group.service';
import { ActivityService } from '../services/activity.service';
import { Activity } from '../classes/activity';
import { ActivityGroup } from '../classes/activity-group';
import { ActivityGroupService } from '../services/activity-group.service';
import { User } from '../classes/user';
import { UserService } from '../services/user.service';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-listing-project',
  templateUrl: './listing-project.component.html',
  styleUrls: ['./listing-project.component.css']
})
export class ListingProjectComponent implements OnInit {

  public projects: WorkGroup[];
  public activities: Activity[];
  public activityGroups: ActivityGroup[];
  public users: User[];

  public filterFields: string[];

  public filter = new FormGroup({
    project: new FormControl(),
    activity: new FormControl(),
    user: new FormControl()
  });

  constructor(
    private workGroupService: WorkGroupService,
    private activityService: ActivityService,
    private activityGroupService: ActivityGroupService,
    private userService: UserService
  ) {
    this.filterFields = [];
   }

  ngOnInit() {
    this.update();
    this.clearForm();
  }

  async update(){
    this.projects = await this.workGroupService.getAllWorkGroup().then(async res => {
      res.forEach(p => {
        p.visible = true;
        p.activityGroup.forEach(ag => {
          ag.visible = true;
        })
      });
      let task = new WorkGroup();
      task.id = -1;
      task.name = "Feladatok";
      task.activityGroup =  [];
      let ag = new ActivityGroup();
      ag.id = -1;
      ag.name = "";
      ag.visible = true;
      task.activityGroup.push(ag);
      task.visible = true;
      res.push(task);

      let easyLogIn = new WorkGroup();
      easyLogIn.id = -2;
      easyLogIn.name = "Egyszerűsített bejeletnkezések";
      easyLogIn.activityGroup = await this.activityGroupService.getAllEasyLogInActivityGroups().then(act => {
        act.forEach(a => {
          a.visible = true;
        });
        return act;
      });
      easyLogIn.visible = true;
      res.push(easyLogIn);
      return res;
    });
    this.activities = await this.activityService.getAllActivity().then(res => {
      res.forEach(a =>{
        a.visible = true;
      })
      return res;
    });
    await this.activityGroupService.getAllActivityGroup().then(res => {
      res.forEach(ag => {
        ag.visible = true;
      });
      this.treeSort(res);
    });
    this.users = await this.userService.getUsers();

    this.fillTable();
  }

  fillTable(){

    this.filterData();

    const table = document.querySelector("#data");

    table.innerHTML = "";

    var row = "";
    for(let i = 0; i < this.projects.length; i++){
      const project = this.projects[i];
      if(project.visible){
        row += "<tr><td><b>" + project.name + "</b></td><td></td><td></td><td></td><td></td><td></td></tr>"
        for (let j = 0; j < project.activityGroup.length; j++) {
          const activityGroup = project.activityGroup[j];
          if(activityGroup.visible){
            row += "<tr><td></td><td>" + activityGroup.name + "</td><td></td><td></td><td></td><td></td></tr>"
            for (let k = 0; k < this.activities.length; k++) {
              const activity = this.activities[k];
              if(activity.visible){
                if(activity.isTask){
                  if(project.id == -1){
                    row += "<tr><td></td><td></td><td>" + activity.user.loginName + "</td><td>" + activity.date + "</td><td></td><td class='w-25'>" + activity.description + "</td></tr>";
                  }
                } else {
                  if(activity.min == null){
                    if(activity.activityGroup.id == activityGroup.id){
                      row += "<tr><td></td><td></td><td>" + activity.user.loginName + "</td><td>" + activity.date + "</td><td></td><td class='w-25'></td></tr>"
                    }
                  } else {
                    if(activity.activityGroup.id == activityGroup.id){
                      row += "<tr><td></td><td></td><td>" + activity.user.loginName + "</td><td>" + activity.date + "</td><td>" + Math.floor(activity.min/60) + " óra " + activity.min % 60 + " perc</td><td class='w-25'>" + activity.description + "</td></tr>"
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    table.innerHTML += row;
  }

  clearForm(){
    this.filter.controls['project'].setValue(-1);
    this.filter.controls['activity'].setValue(-1);
    this.filter.controls['user'].setValue(-1);
  }

  filterData(){
    this.filterFields.forEach(cmd => {
      switch (cmd) {
        case "project":
          const project = this.filter.controls['project'].value;
          this.projects.forEach(p => {
            if(p.id != project){
              p.visible = false;
            }
          })
          break;

        case "activityGroup":
          const activityGroup = this.filter.controls['activity'].value;
          this.projects.forEach(p =>{
            p.activityGroup.forEach(ag => {
              if(ag.id != activityGroup){
                ag.visible = false;
              }
            })
          })
          break;

        case "user":
          const user = this.filter.controls['user'].value;
          this.activities.forEach(a =>{
            if(a.user.id != user){
              a.visible = false;
            }
          })
          break;
      }
    });
  }

  selectProject(){
    const project = this.filter.controls['project'].value;
    if(project != -1){
      if(!this.filterFields.includes('project')){
        this.filterFields.push("project");
      }
    } else {
      const index: number = this.filterFields.indexOf('project');
      if (index !== -1) {
        this.filterFields.splice(index, 1);
      }
      this.projects.forEach(p => {
        p.visible = true;
      })
    }
    this.update();
  }

  selectActivityGroup(){
    const activity = this.filter.controls['activity'].value;
    if(activity != -1){
      if(!this.filterFields.includes('activityGroup')){
        this.filterFields.push("activityGroup");
      }
    } else {
      const index: number = this.filterFields.indexOf('activityGroup');
      if (index !== -1) {
        this.filterFields.splice(index, 1);
      }
      this.projects.forEach(p =>{
        p.activityGroup.forEach(ag => {
            ag.visible = true;
        })
      })
    }
    this.update();
  }

  selectUser(){
    const user = this.filter.controls['user'].value;
    if(user != -1){
      if(!this.filterFields.includes('user')){
        this.filterFields.push("user");
      }
    } else {
      const index: number = this.filterFields.indexOf('user');
      if (index !== -1) {
        this.filterFields.splice(index, 1);
      }
      this.activities.forEach(a =>{
        a.visible = true;
      })
    }
    this.update();
  }

  treeSort(activityGroups: ActivityGroup[]) {
    let activityGroup: ActivityGroup[] = activityGroups;
    this.activityGroups = []
		for (let i = 0; i < activityGroup.length; i++) {
			if (activityGroup[i].parent == null) {
        let root = activityGroup[i];
        this.activityGroups.push(root);
				this.inorder(root, " - ", activityGroup);
			}
		}
	}

	inorder(root: ActivityGroup, deep: string, activityGroup: ActivityGroup[]) {
		for (let i = 0; i < activityGroup.length; i++) {
			if (activityGroup[i].parent == null) continue;
			if (activityGroup[i].parent.id == root.id) {
				let root = activityGroup[i];
        root.name = deep + root.name;
        this.activityGroups.push(root);
				this.inorder(root, deep + " - ", activityGroup);
			}
    }
  }

  resetFilet(){
    this.clearForm();
    this.filterFields = [];
    this.update();
  }

  makeExcel(){
    const projectId = this.filter.controls['project'].value;
    const activityGroupId = this.filter.controls['activity'].value;
    const userId = this.filter.controls['user'].value;

    this.activityService.makeExcelFile(projectId, activityGroupId, userId);
  }

}
