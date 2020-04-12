import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { PermissionDetail } from '../classes/permission-detail';

@Injectable({
  providedIn: 'root'
})
export class PermissionDetailService {

  private url: string = "permission-detail";

  constructor(
    private httpService: HttpService
  ) { }

  getAllPermissionDetails(): Promise<PermissionDetail[]>{
    return this.httpService.get(this.url);
  }
}
