import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { SnackComponent } from '../snack/snack.component';
import { Router } from '@angular/router';
import { ActivityGroup } from '../classes/activity-group';
import { ActivityGroupService } from '../services/activity-group.service';
import { UserService } from '../services/user.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from '../classes/user';


export interface DialogData{
  user: User
}
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public activityGroups: ActivityGroup[] = [];

  public loginForm = new FormGroup({
    loginName: new FormControl(),
    password: new FormControl()
  });

  public easyLoginForm = new FormGroup({
    loginName: new FormControl(),
    activityGroup: new FormControl()
  });

  constructor(
    private authService: AuthService,
    private snackBar: SnackComponent,
    private router: Router,
    private activityGroupService: ActivityGroupService,
    private userService: UserService,
    private dialog: MatDialog
  ) { }

  async ngOnInit() {
    this.activityGroups = await this.activityGroupService.getAllEasyLogInActivityGroups().then(res => {
      if(res.length > 0){
        this.easyLoginForm.controls["activityGroup"].setValue(res[0].id);
      }
      return res;
    });
  }

  async login(){

    const loginName = this.loginForm.controls['loginName'].value;
    const password = this.loginForm.controls['password'].value;

    if(!loginName){
      this.snackBar.sendMsg("Bejelentkezési név megadása kötelező");
      return;
    }

    this.userService.checkUserLoggedIn(loginName).then(async res => {
      if(res == null){
        try {
          await this.authService.login(loginName, password);
          this.router.navigate(['/']);
          this.snackBar.sendMsg('Sikeres bejelentkezés!');
        } catch (e) {
          this.snackBar.sendMsg('Sikertelen bejelentkezés!');
        }
      } else {
        const dialogRef = this.dialog.open(LoginComponentDialog, {
          width: '20%',
          data:{
            user: res
          }
        })
      }
    })
  }

  async easyLogin(){
    const loginName = this.easyLoginForm.controls['loginName'].value;
    const activityGroup = this.easyLoginForm.controls['activityGroup'].value;

    if(!loginName){
      this.snackBar.sendMsg("Felgasználói név megadása kötelező");
      return;
    }

    try {
      await this.authService.easyLogIn(loginName, activityGroup);
      this.router.navigate(['/login']);
      this.snackBar.sendMsg('Sikeres rögzítés!');
      this.easyLoginForm.controls["loginName"].setValue("");
      if(this.activityGroups.length > 0){
        this.easyLoginForm.controls["activityGroup"].setValue(this.activityGroups[0].id);
      }
    } catch (e) {
      this.snackBar.sendMsg('Sikertelen rögzítés!');
    }
  }

}

@Component({
  templateUrl: './login.component.dialog.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponentDialog {

  public user: User;

  public form = new FormGroup({
    password: new FormControl()
  });

  constructor(
    private dialogRef: MatDialogRef<LoginComponent>,
    @Inject(MAT_DIALOG_DATA) private data: DialogData,
    private userService: UserService,
    private snackBar: SnackComponent
  ){
    this.user = data.user;
  }

  save(){
    const password = this.form.controls['password'].value;

    if(!password){
      this.snackBar.sendMsg("Jelszó megadása kötelező");
      return;
    }

    this.user.password = password;
    this.userService.setNotLoggedUserPassword(this.user).then(() => {
      this.dialogRef.close();
    });
  }

}
