import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserHomePageComponent } from './user-home-page/user-home-page.component';
import {HomeRoutingModule} from "./home-routing.module";



@NgModule({
  declarations: [
    UserHomePageComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule
  ]
})
export class HomeModule { }
