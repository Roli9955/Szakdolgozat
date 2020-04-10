import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { WorkTimeComponent } from './work-time/work-time.component';
import { HolidayComponent } from './holiday/holiday.component';
import { MaintenanceUserComponent } from './maintenance-user/maintenance-user.component';
import { MaintenanceProjectComponent } from './maintenance-project/maintenance-project.component';


const routes: Routes = [
  {path: '', component: MainPageComponent},
  {path: 'work-time', component: WorkTimeComponent},
  {path: 'holiday', component: HolidayComponent},
  {path: 'maintenance-user', component: MaintenanceUserComponent},
  {path: 'maintenance-project', component: MaintenanceProjectComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
