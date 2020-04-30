import { Component, OnInit, Inject } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../classes/user';
import { FormGroup, FormControl } from '@angular/forms';
import { SnackComponent } from '../snack/snack.component';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Permission } from '../classes/permission';
import { UserWorkGroup } from '../classes/user-work-group';
import { UserWorkGroupService } from '../services/user-work-group.service';
import { UtilService } from '../services/util.service';

export interface DialogData{
  user: User
}

@Component({
  selector: 'app-maintenance-user',
  templateUrl: './maintenance-user.component.html',
  styleUrls: ['./maintenance-user.component.css']
})
export class MaintenanceUserComponent implements OnInit {

  public users: User[];

  public newUser: User;
  public selectedUser: User;
  public edit: boolean;

  public userForm = new FormGroup({
    lastName: new FormControl(),
    firstName: new FormControl(),
    loginName: new FormControl(),
    email: new FormControl(),
    maxHoliday: new FormControl(),
    canLogIn: new FormControl()
  });

  constructor(
    private userService: UserService,
    private snackBar: SnackComponent,
    private dialog: MatDialog,
    private utilService: UtilService
  ) { 
    this.newUser = new User();
    this.selectedUser = new User();
  }

  ngOnInit() {
    this.update();
  }

  async update(){
    this.users = await this.userService.getUsers();
    this.clearForm();
    this.selectedUser = new User();
    this.edit = false;
  }

  addNewUser(){

    const lastName = this.userForm.controls['lastName'].value;
    const firstName = this.userForm.controls['firstName'].value;
    const loginName = this.userForm.controls['loginName'].value;
    const email = this.userForm.controls['email'].value;
    const maxHoliday = this.userForm.controls['maxHoliday'].value;
    const canLogIn = this.userForm.controls['canLogIn'].value;

    if(!lastName || !firstName || !loginName || !email || !maxHoliday || !canLogIn){
      this.snackBar.sendMsg("Minden mező kitöltése kötelező");
      return;
    }

    if(!this.utilService.isNumber(maxHoliday)){
      this.snackBar.sendMsg("Az összes szabadság mező nem megfelelő típust tartalmaz");
      return;
    }

    this.newUser.lastName = lastName;
    this.newUser.firstName = firstName;
    this.newUser.loginName = loginName;
    this.newUser.email = email;
    this.newUser.password = "asd";
    this.newUser.maxHoliday = parseInt(maxHoliday);
    this.newUser.canLogIn = canLogIn;

    this.userService.addUser(this.newUser).then(() => {
      this.update();
    });
  }

  clearForm(){
    this.userForm.controls['lastName'].setValue("");
    this.userForm.controls['firstName'].setValue("");
    this.userForm.controls['loginName'].setValue("");
    this.userForm.controls['loginName'].enable();
    this.userForm.controls['email'].setValue("");
    this.userForm.controls['email'].enable();
    this.userForm.controls['maxHoliday'].setValue("");
    this.userForm.controls['canLogIn'].setValue("true");
    
    this.selectedUser = new User();
    this.edit = false;
  }

  showUser(user: User){
    this.selectedUser = user;
    const ref = this.dialog.open(MaintenanceUserDialog, {
      width: "60%",
      data: {
        user: user
      }
    });
    
    ref.afterClosed().toPromise().then(msg => {
      if(msg == true){
        this.loadFormForEdit();
        this.edit = true;
      }
    });
  }

  async deleteUser(){
    await this.userService.deleteUser(this.selectedUser.id).then(() => {
      this.update();
    });
  }

  loadFormForEdit(){
    this.userForm.controls['lastName'].setValue(this.selectedUser.lastName);
    this.userForm.controls['firstName'].setValue(this.selectedUser.firstName);
    this.userForm.controls['loginName'].setValue(this.selectedUser.loginName);
    this.userForm.controls['loginName'].disable();
    this.userForm.controls['email'].setValue(this.selectedUser.email);
    this.userForm.controls['email'].disable();
    this.userForm.controls['maxHoliday'].setValue(this.selectedUser.maxHoliday);
    this.userForm.controls['canLogIn'].setValue(this.selectedUser.canLogIn);
  }

  async uploadEdit(){
    const lastName = this.userForm.controls['lastName'].value;
    const firstName = this.userForm.controls['firstName'].value;
    const maxHoliday = this.userForm.controls['maxHoliday'].value;
    const canLogIn = this.userForm.controls['canLogIn'].value;

    if(!lastName || !firstName || !maxHoliday || !canLogIn){
      this.snackBar.sendMsg("Minden mező kitöltése kötelező");
      return;
    }

    this.selectedUser.lastName = lastName;
    this.selectedUser.firstName = firstName;
    this.selectedUser.maxHoliday = parseInt(maxHoliday);
    this.selectedUser.canLogIn = canLogIn;

    await this.userService.editUser(this.selectedUser).then(res => {
      this.clearForm();
      this.update();
      this.snackBar.sendMsg("Módosítás sikeresen megtörtént");
    })
  }

}

@Component({
  templateUrl: './maintenance-user.dialog.html',
  styleUrls: ['./maintenance-user.component.css'] 
})
export class MaintenanceUserDialog{

  public user: User
  public userWorkGroups: UserWorkGroup[];

  constructor(
    public dialogRef: MatDialogRef<MaintenanceUserDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private userWorkGroupService: UserWorkGroupService
  ){
    this.load();
  }

  async load(){
    this.user = this.data.user;
    if(this.user.permission == null){
      this.user.permission = new Permission();
    }
    this.userWorkGroups = await this.userWorkGroupService.getUserWorkGroupByUserId(this.user.id);
  }

  delete(){
    this.dialogRef.close(false);
  }

  edit(){
    this.dialogRef.close(true);
  }

}
