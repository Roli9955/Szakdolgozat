import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { User } from '../classes/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url: string = "user";

  constructor(
    private httpService: HttpService
  ) { }

  getUsersForHoliday(): Promise<User[]>{
    return this.httpService.get(this.url + "/holiday");
  }
}
