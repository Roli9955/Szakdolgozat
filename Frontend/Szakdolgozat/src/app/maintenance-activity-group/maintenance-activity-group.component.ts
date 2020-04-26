import { Component, OnInit, Inject } from '@angular/core';
import { ActivityGroupService } from '../services/activity-group.service';
import { ActivityGroup } from '../classes/activity-group';
import { WorkGroupService } from '../services/work-group.service';
import { WorkGroup } from '../classes/work-group';
import { FormGroup, FormControl } from '@angular/forms';
import { SnackComponent } from '../snack/snack.component';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface DialogData{
  project: WorkGroup
}

@Component({
  selector: 'app-maintenance-activity-group',
  templateUrl: './maintenance-activity-group.component.html',
  styleUrls: ['./maintenance-activity-group.component.css']
})
export class MaintenanceActivityGroupComponent implements OnInit {

  public activityGroups: ActivityGroup[];
  public activityGroupsCanAddChild: ActivityGroup[];
  public workGroups: WorkGroup[];

  public newActivityGroup: ActivityGroup;

  public selectedForDelete: ActivityGroup;

  public activityGroupForm = new FormGroup({
    name: new FormControl(),
    parent: new FormControl(),
    child: new FormControl(),
    easyLogIn: new FormControl()
  });

  constructor(
    private activityGroupService: ActivityGroupService,
    private workGroupService: WorkGroupService,
    private snackBar: SnackComponent,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.update();
  }

  async update(){
    this.activityGroups = [];
    this.activityGroupsCanAddChild = [];
    this.workGroups = [];
    this.newActivityGroup = new ActivityGroup();
    this.selectedForDelete = new ActivityGroup();
    await this.activityGroupService.getAllActivityGroup().then(res => {
      this.treeSort(res);
    });
    this.workGroups = await this.workGroupService.getAllWorkGroup();
    this.clearForm();
  }

  clearForm(){
    this.activityGroupForm.controls['name'].setValue("");
    this.activityGroupForm.controls['parent'].setValue(-1);
    this.activityGroupForm.controls['child'].setValue("true");
    this.activityGroupForm.controls['easyLogIn'].setValue("false");
    this.activityGroupForm.controls['child'].enable();
      this.activityGroupForm.controls['parent'].enable();
  }

  treeSort(res: ActivityGroup[]) {
		for (let i = 0; i < res.length; i++) {
			if (res[i].parent == null) {
				let root = res[i];
        this.activityGroups.push(root);
        if(root.canAddChild) {
          this.activityGroupsCanAddChild.push(root)
        }
        this.inorder(root, " - ", res);
			}
		}
	}

	inorder(root: ActivityGroup, deep: string, res: ActivityGroup[]) {
		for (let i = 0; i < res.length; i++) {
			if (res[i].parent == null) continue;
			if (res[i].parent.id == root.id) {
				let root = res[i];
        this.activityGroups.push(root);
        if(root.canAddChild) {
          this.activityGroupsCanAddChild.push(root)
        }
        root.name = deep + root.name;
        this.inorder(root, deep + " - ", res);
			}
		}
  }
  
  addNewActivityGroup(){
    const name = this.activityGroupForm.controls['name'].value;
    const parent = this.activityGroupForm.controls['parent'].value;
    const child = this.activityGroupForm.controls['child'].value;
    const easyLogIn = this.activityGroupForm.controls['easyLogIn'].value;

    if(!name){
      this.snackBar.sendMsg("A név megadása kötelező");
      return;
    }

    this.newActivityGroup.name = name;
    this.newActivityGroup.canAddChild = child;
    this.newActivityGroup.easyLogIn = easyLogIn;
    if(parent != -1){
      this.newActivityGroup.parent = new ActivityGroup();
      this.newActivityGroup.parent.id = parent;
    }

    if(easyLogIn == "false"){
      this.activityGroupService.addNewActivityGroup(this.newActivityGroup).then(res => {
        this.update();
      }).catch(err => {
        this.snackBar.sendMsg(err.error);
      });
    } else {
      this.activityGroupService.addNewActivityGroupForEasyLogIn(this.newActivityGroup).then(res => {
        this.update();
      }).catch(err => {
        this.snackBar.sendMsg(err.error);
      });
    }

  }

  selectForDelete(activityGroup: ActivityGroup){
    this.selectedForDelete = activityGroup;
  }

  delete(){
    this.activityGroupService.deleteActivityGroup(this.selectedForDelete.id).then(res => {
      this.update();
    })
  }

  showProject(project: WorkGroup){
    const ref = this.dialog.open(MaintenanceActivityGroupDialog, {
      width: "60%",
      data: {
        project: project
      }
    });

    ref.afterClosed().subscribe(() => {
      this.update();
    })
  }

  easyLogInChange(){
    const easyLogIn = this.activityGroupForm.controls['easyLogIn'].value;

    if(easyLogIn == "true"){
      this.activityGroupForm.controls['child'].disable();
      this.activityGroupForm.controls['parent'].disable();
    } else {
      this.activityGroupForm.controls['child'].enable();
      this.activityGroupForm.controls['parent'].enable();
    }
  }

}

@Component({
  templateUrl: './maintenance-activity-group.dialog.html',
  styleUrls: ['./maintenance-activity-group.component.css']
})
export class MaintenanceActivityGroupDialog {

  public project: WorkGroup;
  public activityGroups: ActivityGroup[];
  public edit: boolean;

  constructor(
    private dialogRef: MatDialogRef<MaintenanceActivityGroupDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private activityGroupService: ActivityGroupService,
    private snackBar: SnackComponent
  ){
    this.load();
  }

  async load(){
    this.edit = false;
    this.activityGroups = [];
    await this.activityGroupService.getAllActivityGroupWithOutEasyLogIn().then(res => {
      this.treeSort(res);
    });
    this.project = this.data.project;

    this.project.activityGroup.forEach(ag => {
      this.activityGroups.forEach(allAG => {
        if(ag.id == allAG.id){
          allAG.selected = true;
          return;
        }
      })
    })
  }

  treeSort(res: ActivityGroup[]) {
		for (let i = 0; i < res.length; i++) {
			if (res[i].parent == null) {
				let root = res[i];
        this.activityGroups.push(root);
        this.inorder(root, " - ", res);
			}
		}
	}

	inorder(root: ActivityGroup, deep: string, res: ActivityGroup[]) {
		for (let i = 0; i < res.length; i++) {
			if (res[i].parent == null) continue;
			if (res[i].parent.id == root.id) {
				let root = res[i];
        this.activityGroups.push(root);
        root.name = deep + root.name;
        this.inorder(root, deep + " - ", res);
			}
		}
  }

  editWorkGroup(){
    this.edit = true;
  }

  selectActivityGroup(activityGroup: ActivityGroup){
    if(this.edit){
      activityGroup.selected = !activityGroup.selected;

      if(activityGroup.selected){
        let node = activityGroup;
        while(node.parent != null){
          this.activityGroups.forEach(ag => {
            if(node.parent != null){
              if(ag.id == node.parent.id){
                node = ag;
                return;
              }
            }
          });
          node.selected = true;
        }
      } else {
        this.disableChild(activityGroup);
      }
    }
  }

  disableChild(activityGroup: ActivityGroup){
    for (let i = 0; i < this.activityGroups.length; i++) {
			if (this.activityGroups[i].parent == null) continue;
			if (this.activityGroups[i].parent.id == activityGroup.id) {
				let root = this.activityGroups[i];
        root.selected = false;
        this.disableChild(root);
			}
		}
  }

  saveWorkGroup(){
    
    this.project.activityGroup = [];
    this.activityGroups.forEach(res => {
      if(res.selected){
        this.project.activityGroup.push(res);
      }
    })

    this.activityGroupService.editWorkGroupActivityGroups(this.project)
    this.snackBar.sendMsg("A projekt feladatai sikeresen frissültek");
    
    this.load();
  }

}
