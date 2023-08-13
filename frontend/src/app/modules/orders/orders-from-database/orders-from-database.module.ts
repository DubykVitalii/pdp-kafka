import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrdersFromDatabasePageComponent } from './orders-from-database-page/orders-from-database-page.component';
import {CommonModule} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";
import {ReactiveFormsModule} from "@angular/forms";
import {TagModule} from "primeng/tag";

const routes: Routes = [
  { path: '', component: OrdersFromDatabasePageComponent }
];

@NgModule({
  declarations: [
    OrdersFromDatabasePageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ButtonModule,
    DialogModule,
    TableModule,
    InputTextModule,
    ReactiveFormsModule,
    TagModule
  ]
})
export class OrdersFromDatabaseModule { }

