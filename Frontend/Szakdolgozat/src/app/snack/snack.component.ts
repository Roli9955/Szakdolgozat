import { Component, OnInit, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBar } from '@angular/material/snack-bar';

export interface SnackBarMsg{
  msg: string;
}

@Component({
  selector: 'app-snack',
  templateUrl: './snack.component.html',
  styleUrls: ['./snack.component.css']
})
export class SnackComponent implements OnInit {

  private duration: number = 3;
  
  constructor(
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
  }

  sendMsg(msg: string){
    this.snackBar.openFromComponent(MySnack, {
      duration: this.duration * 1000,
      data: {
        msg: msg
      }
    })
  }

}

@Component({
  templateUrl: './snack.component.html',
  styleUrls: ['./snack.component.css']
})
export class MySnack {

  public msg: string;

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: SnackBarMsg
  ) {
    this.msg = this.data.msg;
  }

}