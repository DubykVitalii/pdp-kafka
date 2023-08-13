import { Component } from '@angular/core';
import {MenuItem} from "primeng/api";

@Component({
  selector: 'app-menu-tab',
  templateUrl: './menu-tab.component.html',
  styleUrls: ['./menu-tab.component.scss']
})
export class MenuTabComponent {
  items!: MenuItem[];
  activeItem!: MenuItem;

  ngOnInit() {
    this.items = [
      { label: 'Products', routerLink: '/products' },
      { label: 'Orders', routerLink: '/orders' },
      { label: 'Delivery', routerLink: '/delivery' }
    ];

    this.activeItem = this.items[0];
  }
}
