import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { TaskbarComponent, TaskbarComponentDialog } from './taskbar/taskbar.component';
import { ContentComponent } from './content/content.component';
import { MainContainerComponent } from './main-container/main-container.component';
import { MainPageComponent } from './main-page/main-page.component';
import { WorkTimeComponent } from './work-time/work-time.component';
import { CalendarComponent } from './calendar/calendar.component';
import { UploadWorkTimeComponent } from './upload-work-time/upload-work-time.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { HolidayComponent } from './holiday/holiday.component';
import { SnackComponent, MySnack } from './snack/snack.component';
import { MaintenanceUserComponent, MaintenanceUserDialog } from './maintenance-user/maintenance-user.component';
import { MaintenanceProjectComponent, MaintenanceProjectDialog } from './maintenance-project/maintenance-project.component';
import { MaintenanceActivityGroupComponent, MaintenanceActivityGroupDialog } from './maintenance-activity-group/maintenance-activity-group.component';
import { MaintenancePermissionComponent, MaintenancePermissionDialog } from './maintenance-permission/maintenance-permission.component';
import { ListingProjectComponent } from './listing-project/listing-project.component';
import { ListingHolidayComponent } from './listing-holiday/listing-holiday.component';
import { LoginComponent, LoginComponentDialog } from './login/login.component';

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
    HolidayComponent,
    SnackComponent,
    MySnack,
    MaintenanceUserComponent,
    MaintenanceUserDialog,
    MaintenanceProjectComponent,
    MaintenanceProjectDialog,
    MaintenanceActivityGroupComponent,
    MaintenanceActivityGroupDialog,
    MaintenancePermissionComponent,
    MaintenancePermissionDialog,
    ListingProjectComponent,
    ListingHolidayComponent,
    TaskbarComponentDialog,
    LoginComponent,
    LoginComponentDialog
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
  providers: [SnackComponent],
  bootstrap: [AppComponent],
  entryComponents: [
    MySnack,
    MaintenanceUserDialog,
    MaintenanceProjectDialog,
    MaintenanceActivityGroupDialog,
    MaintenancePermissionDialog,
    TaskbarComponentDialog,
    LoginComponentDialog
  ]
})
export class AppModule { }
