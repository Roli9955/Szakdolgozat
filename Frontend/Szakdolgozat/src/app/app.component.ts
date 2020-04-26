import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Szakdolgozat';

  constructor (
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    if (window.localStorage.getItem('token')) {
      this.authService.loginWithToken();
      this.router.navigate(['/']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}
