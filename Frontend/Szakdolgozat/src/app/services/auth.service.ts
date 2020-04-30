import { Injectable } from '@angular/core';
import { User } from '../classes/user';
import { HttpService } from './http.service';
import { Router } from '@angular/router';
import { Permission } from '../classes/permission';
import { PermissionDetail } from '../classes/permission-detail';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isLoggedIn: boolean = false;
  private user: User = null

  constructor(
    private httpService: HttpService,
    private router: Router
  ) { }

  getIsLoggedIn(): boolean{
    return  this.isLoggedIn;
  }

  getUser(): User{
    return this.user;
  }

  async login(username: string, password: string): Promise<User>{
    
    try {
      const token = btoa(username + ":" + password);
      window.localStorage.setItem('token', token);
      const user: User = await this.httpService.put<User>('login', null);
      this.isLoggedIn = true;
      this.user = user;
      this.setUserRole()
      return Promise.resolve(user);
    } catch (e) {
      window.localStorage.setItem('token', '');
      return Promise.reject();
    }

  }

  logOut(){
    this.isLoggedIn = false;
    this.user = null;
    window.localStorage.setItem('token', '');
    this.router.navigate(['/login']);
  }

  loginWithToken(){
    const token = window.localStorage.getItem('token');
    const [username, password] = atob(token).split(':');
    this.login(username, password);
  }

  async easyLogIn(username: string, activityGroupId: number): Promise<User>{
    try {
      let user = new User();
      user.loginName = username;
      user = await this.httpService.post("easy-log-in/activity-group/" + activityGroupId, user);
      return Promise.resolve(user);
    } catch (e) {
      return Promise.reject();
    }
  }

  setUserRole(){
    if(this.user.permission == null){
      this.user.permission = new Permission();
      this.user.permission.details = [];
      let pd: PermissionDetail = new PermissionDetail();
      pd.roleTag ="ROLE_USER";
      this.user.permission.details.push(pd);
    }
  }

}