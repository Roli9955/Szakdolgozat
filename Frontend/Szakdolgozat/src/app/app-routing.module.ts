import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { WorkTimeComponent } from './work-time/work-time.component';


const routes: Routes = [
  {path: '', component: MainPageComponent},
  {path: 'work-time', component: WorkTimeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
