import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {TableModule} from "primeng/table";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import { DeliveryPageComponent } from './delivery-page/delivery-page.component';
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {TagModule} from "primeng/tag";
import {DropdownModule} from "primeng/dropdown";

const routes: Routes = [
  { path: '', component: DeliveryPageComponent }
];

@NgModule({
  declarations: [
    DeliveryPageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ButtonModule,
    DialogModule,
    TableModule,
    InputTextModule,
    ReactiveFormsModule,
    TagModule,
    DropdownModule,
    FormsModule
  ]
})
export class DeliveryModule { }
