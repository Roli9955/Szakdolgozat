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

  getUsers(): Promise<User[]>{
    return this.httpService.get(this.url);
  }

  addUser(user: User): Promise<User>{
    return this.httpService.post(this.url + "/add", user);
  }

  deleteUser(userId: number): Promise<User>{
    return this.httpService.delete(this.url + "/delete/" + userId);
  }

  editUser(user: User): Promise<User>{
    return this.httpService.put(this.url + "/edit", user);
  }
}
