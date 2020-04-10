import { Component, OnInit, Inject } from '@angular/core';
import { WorkGroupService } from '../services/work-group.service';
import { WorkGroup } from '../classes/work-group';
import { FormGroup, FormControl } from '@angular/forms';
import { UserService } from '../services/user.service';
import { User } from '../classes/user';
import { UserWorkGroup } from '../classes/user-work-group';
import { SnackComponent } from '../snack/snack.component';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivityGroup } from '../classes/activity-group';

export interface DialogData {
	project: WorkGroup
}

@Component({
	selector: 'app-maintenance-project',
	templateUrl: './maintenance-project.component.html',
	styleUrls: ['./maintenance-project.component.css']
})
export class MaintenanceProjectComponent implements OnInit {

	public workGroups: WorkGroup[];
	public users: User[];

	public newWorkGroup: WorkGroup;
	public newUserWorkGroup: UserWorkGroup[];

	public edit: boolean;

	public selectedProject: WorkGroup;

	public workGroupForm = new FormGroup({
		name: new FormControl(),
		scale: new FormControl(),
		user: new FormControl(),
		from: new FormControl(),
		to: new FormControl()
	});

	constructor(
		private workGroupService: WorkGroupService,
		private userService: UserService,
		private snackBar: SnackComponent,
		private dialog: MatDialog
	) {
		this.edit = false;
		this.newUserWorkGroup = [];
		this.newWorkGroup = new WorkGroup();
		this.selectedProject = new WorkGroup();
	}

	ngOnInit() {
		this.update();
	}

	async update() {
		this.workGroups = await this.workGroupService.getAllWorkGroup();
		this.users = await this.userService.getUsers().then(users => {
			if (users.length > 0) {
				this.workGroupForm.controls['user'].setValue(users[0].id);
			}
			return users;
		});
		this.edit = false;
		this.newUserWorkGroup = [];
		this.newWorkGroup = new WorkGroup();
		this.selectedProject = new WorkGroup();

		var today = new Date();
		var dd = String(today.getDate()).padStart(2, '0');
		var mm = String(today.getMonth() + 1).padStart(2, '0');
		var yyyy = today.getFullYear();

		this.workGroupForm.controls['from'].setValue(yyyy + "-" + mm + "-" + dd);
		this.clearForm();
	}

	clearForm() {
		this.workGroupForm.controls['name'].setValue("");
		this.workGroupForm.controls['scale'].setValue("");
		this.workGroupForm.controls['to'].setValue("")
	}

	addNewProject() {
		const name = this.workGroupForm.controls['name'].value;
		const scale = this.workGroupForm.controls['scale'].value;

		if (!name || !scale) {
			this.snackBar.sendMsg("Név és arány megadása kötelező");
			return;
		}

		this.newWorkGroup.name = name;
		this.newWorkGroup.scale = scale;

		this.workGroupService.addNewWorkGroup(this.newWorkGroup).then(res => {
			if(this.newUserWorkGroup.length != 0){
				this.workGroupService.addUserWorkGroups(res.id, this.newUserWorkGroup).then(() => {
					this.update();
				});
			} else {
				this.update();
			}
		});

	}

	addUser() {

		const from = this.workGroupForm.controls['from'].value;
		const to = this.workGroupForm.controls['to'].value;
		const userId = this.workGroupForm.controls['user'].value;

		if (!from) {
			this.snackBar.sendMsg("Kötelező első napot megadni");
			return;
		}

		var l: Boolean = false;
		this.newUserWorkGroup.forEach(userWorkGroup => {
			if (userWorkGroup.user.id == userId) {
				this.snackBar.sendMsg("A kiválasztott felhasználó, már hozzáadásra került");
				l = true;
				return;
			}
		})
		if (l) return;

		var userWorkGroup: UserWorkGroup = new UserWorkGroup();
		userWorkGroup.inWorkGroupFrom = new Date(from);
		if (to) {
			userWorkGroup.inWorkGroupTo = new Date(to);
		}

		this.users.forEach(user => {
			if (user.id == userId) {
				userWorkGroup.user = user;
				return;
			}
		})

		this.newUserWorkGroup.push(userWorkGroup);
	}

	deleteUserFromWorkGroup(user: User) {
		var index = -1;
		for (let i = 0; i < this.newUserWorkGroup.length; i++) {
			if (this.newUserWorkGroup[i].user.id == user.id) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			this.newUserWorkGroup.splice(index, 1);
		}
	}

	showProject(project: WorkGroup) {
		this.selectedProject = project;
		const ref = this.dialog.open(MaintenanceProjectDialog, {
			width: "60%",
			data: {
				project: project
			}
		});

		ref.afterClosed().toPromise().then(msg => {
			if (msg == true) {
				this.loadFormForEdit();
				this.edit = true;
			}

			project.activityGroup.forEach(ag => {
				ag.name = ag.name.replace(new RegExp(" - ", "g"), "");
			});
		});
	}

	deleteProject(){
		this.workGroupService.deleteWorkGroup(this.selectedProject.id).then(() => {
			this.update();
		});
	}

	async loadFormForEdit(){
		this.workGroupForm.controls['name'].setValue(this.selectedProject.name);
		this.workGroupForm.controls['scale'].setValue(this.selectedProject.scale);
		this.newUserWorkGroup = await this.workGroupService.getUsersInProject(this.selectedProject.id);
	}

	editWorkGroup(){
		const name = this.workGroupForm.controls['name'].value;
		const scale = this.workGroupForm.controls['scale'].value;

		if (!name || !scale) {
			this.snackBar.sendMsg("Név és arány megadása kötelező");
			return;
		}

		this.selectedProject.name = name;
		this.selectedProject.scale = scale;
		
		this.workGroupService.editWorkGroup(this.selectedProject).then(res => {
			this.workGroupService.editUserWorkGroup(res.id, this.newUserWorkGroup).then(() => {
				this.update();
			});
		})
	}

}

@Component({
	templateUrl: './maintenance-project.dialog.html',
	styleUrls: ['./maintenance-project.component.css']
})
export class MaintenanceProjectDialog {

	public project: WorkGroup;
	public userWorkGroups: UserWorkGroup[];
	public activityGroups: ActivityGroup[] = [];

	constructor(
		private dialogRef: MatDialogRef<MaintenanceProjectDialog>,
		@Inject(MAT_DIALOG_DATA) public data: DialogData,
		private workGroupService: WorkGroupService
	) {
		this.load();
	}

	async load() {
		this.project = this.data.project;
		this.userWorkGroups = await this.workGroupService.getUsersInProject(this.project.id);
		this.treeSort();
	}

	treeSort() {
		for (let i = 0; i < this.project.activityGroup.length; i++) {
			if (this.project.activityGroup[i].parent == null) {
				let root = this.project.activityGroup[i];
				this.activityGroups.push(root);
				this.inorder(root, " - ");
			}
		}
	}

	inorder(root: ActivityGroup, deep: string) {
		for (let i = 0; i < this.project.activityGroup.length; i++) {
			if (this.project.activityGroup[i].parent == null) continue;
			if (this.project.activityGroup[i].parent.id == root.id) {
				let root = this.project.activityGroup[i];
				this.activityGroups.push(root);
				root.name = deep + root.name;
				this.inorder(root, deep + " - ");
			}
		}
	}

	closeDialog(){
		this.dialogRef.close();
	}

	edit(){
		this.dialogRef.close(true);
	}
}
