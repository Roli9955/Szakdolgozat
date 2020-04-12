import { Component, OnInit, Inject } from '@angular/core';
import { PermissionService } from '../services/permission.service';
import { Permission } from '../classes/permission';
import { FormGroup, FormControl } from '@angular/forms';
import { SnackComponent } from '../snack/snack.component';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { PermissionDetail } from '../classes/permission-detail';
import { PermissionDetailService } from '../services/permission-detail.service';
import { User } from '../classes/user';
import { UserService } from '../services/user.service';

export interface DialogData{
  permission: Permission
}

@Component({
  selector: 'app-maintenance-permission',
  templateUrl: './maintenance-permission.component.html',
  styleUrls: ['./maintenance-permission.component.css']
})
export class MaintenancePermissionComponent implements OnInit {

  public permissions: Permission[];
  public newPermission: Permission;

  public edit: boolean;

  public permissionForm = new FormGroup({
    name: new FormControl()
  })

  constructor(
    private permissionService: PermissionService,
    private snackBar: SnackComponent,
    private dialog: MatDialog
  ) { 
    this.update();
  }

  ngOnInit() {}

  async update(){
    this.newPermission = new Permission();
    this.edit = false;
    this.permissions = await this.permissionService.getAllPermissions();

    this.clearForm();
  }

  clearForm(){
    this.permissionForm.controls['name'].setValue("");
  }

  addNewPermission(){
    const name = this.permissionForm.controls['name'].value;

    if(!name){
      this.snackBar.sendMsg("Név megadása kötelező");
      return;
    }

    this.newPermission.name = name;

    this.permissionService.addNewPermission(this.newPermission).then(() => {
      this.snackBar.sendMsg("Jogosultsági csoport sikeresen hozzáadásra került");
      this.update();
    }).catch(() => {
      this.snackBar.sendMsg("Ilyen néven már szerepel jogosultsági csoport");
    });

  }

  editLoad(event, permission: Permission){
    event.stopPropagation();
    this.edit = true;
    this.newPermission.id = permission.id;
    this.permissionForm.controls['name'].setValue(permission.name);
  }

  uplodaEdit(){
    const name = this.permissionForm.controls['name'].value;

    if(!name){
      this.snackBar.sendMsg("Név megadása kötelező");
      return;
    }

    this.newPermission.name = name;

    this.permissionService.editPermission(this.newPermission).then(() => {
      this.snackBar.sendMsg("Sikeresen modosításra került")
      this.update();
    }).catch(err => {
      this.snackBar.sendMsg(err.error);
    });
  }

  async delete(event, id: number){
    event.stopPropagation();
    await this.permissionService.deletePermission(id).then(() => {
      this.snackBar.sendMsg("Sikeresen törlésre került");
      this.update();
    });
  }

  showPermission(permission: Permission){
    const ref = this.dialog.open(MaintenancePermissionDialog, {
      width: '60%',
      data: {
        permission: permission
      }
    })

    ref.afterClosed().subscribe(() => {
      this.update();
    });
  }

}

@Component({
  templateUrl: './maintenance-permission.dialog.html',
  styleUrls: ['./maintenance-permission.component.css']
})
export class MaintenancePermissionDialog {

  public permission: Permission;
  public permissionDetails: PermissionDetail[];
  public users: User[];
  public permissionsUsers: User[];

  public edit: boolean;

  constructor(
    private dialogRef: MatDialogRef<MaintenancePermissionComponent>,
    @Inject(MAT_DIALOG_DATA) private data: DialogData,
    private permissionDetailService: PermissionDetailService,
    private permissionService: PermissionService,
    private userService: UserService,
    private snackBar: SnackComponent
  ){
    this.permission = this.data.permission;
    this.load();
  }

  async load(){
    this.edit = false;
    this.permissionDetails = await this.permissionDetailService.getAllPermissionDetails().then(res => {
      res.forEach(detail => {
        this.permission.details.forEach(myDetail => {
          if(detail.id == myDetail.id){
            detail.selected = true;
            return;
          }
        })
      })
      return res;
    });

    this.users = await this.userService.getUsers().then(res => {
      this.permissionService.getUsersByPermissionId(this.permission.id).then(users => {
        res.forEach(r => {
          users.forEach(u =>{
            if(r.id == u.id){
              r.selected = true;
            }
          })
        });
      });
      return res;
    });
  }

  enableEdit(){
    this.edit = true;
  }

  editPermissionDetail(permissionDetail: PermissionDetail){
    if(this.edit){
      permissionDetail.selected = !permissionDetail.selected;
    }
  }

  editUser(user: User){
    if(this.edit){
      user.selected = !user.selected;
    }
  }

  savePermission(){

    let permission: Permission = new Permission();
    permission.id = this.permission.id;
    permission.details = [];
    this.permissionDetails.forEach(detail => {
      if(detail.selected){
        permission.details.push(detail);
      }
    });
    
    this.permissionService.updatePermission(permission).then(res => {
      this.permission = res;
      let users: User[] = [];
      this.users.forEach(user => {
        if(user.selected){
          users.push(user);
        }
      });
  
      this.permissionService.updatePermissionsUsers(this.permission.id, users);
      this.snackBar.sendMsg("Az adatok sikeresen mentésre kerültek");
      this.load();
    });

  }

}
