import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor() { }

  isNumber(str: string){
    var regex = /^-{0,1}\d+$/;
    return regex.test(str)
  }
}
