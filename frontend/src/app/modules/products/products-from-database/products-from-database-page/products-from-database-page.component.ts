import { Component, OnInit } from '@angular/core';
import {Product} from "../interface/product";
import {ProductService} from "../service/product.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../../notifications/service/notification.service";

interface KafkaMessage {
  key: string;
  value: string;
  timestamp: number;
}
@Component({
  selector: 'app-products-from-database-page',
  templateUrl: './products-from-database-page.component.html',
  styleUrls: ['./products-from-database-page.component.scss']
})
export class ProductsFromDatabasePageComponent implements OnInit {
  products: Product[] = [];
  kafkaMessages: KafkaMessage[] = [];
  searchTerm: string = '';
  productForm: FormGroup;
  displayDialog: boolean;

  constructor(private fb: FormBuilder, private productService: ProductService, private notificationService: NotificationService) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required],
      availableProductCount: ['', Validators.required],
      available: [false]
    });


    this.displayDialog = false;
  }
  ngOnInit() {
    this.subscribeToNotifications()
    this.getAllProducts();
  }
  ngOnDestroy(): void {
    this.notificationService.unsubscribeFromNotifications();
  }

  subscribeToNotifications(): void {

    this.notificationService.subscribeToNotifications('products').subscribe();
    this.notificationService.notifications$.subscribe((message) =>
      {
        this.kafkaMessages.push((message as unknown) as KafkaMessage);
      },
      (error) => {
        console.error('Error receiving Kafka message:', error);
      }
    );
  }

  getAllProducts(): void {
    this.productService.getAllProducts(true).subscribe(
      products => this.products = products,
      error => console.error(error)
    );
  }

  showDialog() {
    this.displayDialog = true;
  }

  addProduct() {
    if (this.productForm.valid) {
      this.productService.createProduct(this.productForm.value).subscribe(() => {
        this. getAllProducts()
        this.displayDialog = false;
      });
    }
  }

  applySearchFilter(value: string) {
    this.searchTerm = value;
    // this.getProducts();
  }
}
