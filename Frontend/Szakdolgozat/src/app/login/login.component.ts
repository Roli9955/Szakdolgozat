import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { SnackComponent } from '../snack/snack.component';
import { Router } from '@angular/router';
import { ActivityGroup } from '../classes/activity-group';
import { ActivityGroupService } from '../services/activity-group.service';

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
    private activityGroupService: ActivityGroupService
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

    try {
      await this.authService.login(loginName, password);
      this.router.navigate(['/']);
      this.snackBar.sendMsg('Sikeres bejelentkezés!');
    } catch (e) {
      this.snackBar.sendMsg('Sikertelen bejelentkezés!');
    }

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
