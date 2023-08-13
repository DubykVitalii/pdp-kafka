import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainPageComponent } from './main-page/main-page.component';
import { MainRoutingModule } from './main-routing.module';
import { MenuTabComponent } from './components/menu-tab/menu-tab.component';
import { TabMenuModule } from 'primeng/tabmenu';

@NgModule({
  declarations: [
    MainPageComponent,
    MenuTabComponent
  ],
  imports: [
    CommonModule,
    MainRoutingModule,
    TabMenuModule
  ]
})
export class MainModule { }
