import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Permission } from '../classes/permission';
import { User } from '../classes/user';

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  private url: string = 'permission'

  constructor(
    private httpService: HttpService
  ) { }

  getAllPermissions(): Promise<Permission[]>{
    return this.httpService.get(this.url);
  }

  addNewPermission(permission: Permission): Promise<Permission>{
    return this.httpService.post(this.url + "/add", permission);
  }

  editPermission(permission: Permission): Promise<Permission>{
    return this.httpService.put(this.url + "/edit", permission);
  }

  deletePermission(permissionId): Promise<Permission>{
    return this.httpService.delete(this.url + "/delete/" + permissionId);
  }

  getUsersByPermissionId(id: number): Promise<User[]>{
    return this.httpService.get(this.url + "/" + id + "/users");
  }

  updatePermission(permission: Permission): Promise<Permission>{
    return this.httpService.put(this.url + "/edit/permission-detail", permission);
  }

  updatePermissionsUsers(id: number, users: User[]): Promise<Permission>{
    return this.httpService.put(this.url + "/edit/" + id + "/user", users);
  }
}
