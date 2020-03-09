import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { TaskbarComponent } from './taskbar/taskbar.component';
import { ContentComponent } from './content/content.component';
import { MainContainerComponent } from './main-container/main-container.component';
import { MainPageComponent } from './main-page/main-page.component';
import { WorkTimeComponent } from './work-time/work-time.component';
import { CalendarComponent } from './calendar/calendar.component';
import { UploadWorkTimeComponent } from './upload-work-time/upload-work-time.component';
import { HttpClientModule } from '@angular/common/http';

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
    UploadWorkTimeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
