import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { TaskbarComponent } from './taskbar/taskbar.component';
import { ContentComponent } from './content/content.component';
import { MainContainerComponent } from './main-container/main-container.component';
import { MainPageComponent } from './main-page/main-page.component';
import { WorkTimeComponent, WorkTimeComponentDialog } from './work-time/work-time.component';
import { CalendarComponent } from './calendar/calendar.component';
import { UploadWorkTimeComponent } from './upload-work-time/upload-work-time.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    TaskbarComponent,
    ContentComponent,
    MainContainerComponent,
    MainPageComponent,
    WorkTimeComponent,
    CalendarComponent,
    UploadWorkTimeComponent,
    WorkTimeComponentDialog
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    BrowserAnimationsModule,
    MatDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [
    WorkTimeComponentDialog
  ]
})
export class AppModule { }
