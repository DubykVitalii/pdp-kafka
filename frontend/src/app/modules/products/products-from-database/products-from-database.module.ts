import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsFromDatabasePageComponent } from './products-from-database-page/products-from-database-page.component';
import {CommonModule} from "@angular/common";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {CheckboxModule} from "primeng/checkbox";
import {InputTextModule} from "primeng/inputtext";
import {DialogModule} from "primeng/dialog";
import {ReactiveFormsModule} from "@angular/forms";

const routes: Routes = [
  { path: '', component: ProductsFromDatabasePageComponent }
];

@NgModule({
  declarations: [
    ProductsFromDatabasePageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    TableModule,
    ButtonModule,
    CheckboxModule,
    InputTextModule,
    DialogModule,
    ReactiveFormsModule
  ]
})
export class ProductsFromDatabaseModule { }
