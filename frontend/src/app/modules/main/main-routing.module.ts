import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainPageComponent} from "./main-page/main-page.component";

const routes: Routes = [
  {
    path: '',
    component: MainPageComponent,
    children: [
      {
        path: 'products',
        loadChildren: () => import('../products/products-from-database/products-from-database.module').then((m) => m.ProductsFromDatabaseModule),
      },
      {
        path: 'orders',
        loadChildren: () => import('../orders/orders-from-database/orders-from-database.module').then((m) => m.OrdersFromDatabaseModule),
      },
      {
        path: 'delivery',
        loadChildren: () => import('../delivery/delivery.module').then((m) => m.DeliveryModule),
      },
      {
        path: '**',
        redirectTo: 'products',
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MainRoutingModule {
}
