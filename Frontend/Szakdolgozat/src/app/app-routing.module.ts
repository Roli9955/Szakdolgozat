import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { WorkTimeComponent } from './work-time/work-time.component';
import { HolidayComponent } from './holiday/holiday.component';
import { MaintenanceUserComponent } from './maintenance-user/maintenance-user.component';
import { MaintenanceProjectComponent } from './maintenance-project/maintenance-project.component';
import { MaintenanceActivityGroupComponent } from './maintenance-activity-group/maintenance-activity-group.component';
import { MaintenancePermissionComponent } from './maintenance-permission/maintenance-permission.component';
import { ListingProjectComponent } from './listing-project/listing-project.component';
import { ListingHolidayComponent } from './listing-holiday/listing-holiday.component';
import { LoginComponent } from './login/login.component';
import { WorkTimePlannerComponent } from './work-time-planner/work-time-planner.component';


const routes: Routes = [
  {path: '', component: MainPageComponent},
  {path: 'work-time', component: WorkTimeComponent},
  {path: 'holiday', component: HolidayComponent},
  {path: 'maintenance-user', component: MaintenanceUserComponent},
  {path: 'maintenance-project', component: MaintenanceProjectComponent},
  {path: 'maintenance-activity-group', component: MaintenanceActivityGroupComponent},
  {path: 'maintenance-permission', component: MaintenancePermissionComponent},
  {path: 'listing-project', component: ListingProjectComponent},
  {path: 'listing-holiday', component: ListingHolidayComponent},
  {path: 'login', component: LoginComponent},
  {path: 'work-time-planner', component: WorkTimePlannerComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
