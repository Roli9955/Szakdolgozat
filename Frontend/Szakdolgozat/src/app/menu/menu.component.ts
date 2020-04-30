import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  constructor(
    private authService: AuthService
  ) {}

  ngOnInit() {}

  logout(){
    this.authService.logOut();
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
