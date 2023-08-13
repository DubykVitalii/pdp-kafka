import { Component } from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {KafkaMessage} from "kafkajs";
import {OrderService} from "../service/order.service";
import {NotificationService} from "../../../notifications/service/notification.service";
import {Order, OrderStatus, OrderStatusDisplayNames} from "../interface/order";
import {OrderStatusService} from "../service/order-status.service";

@Component({
  selector: 'app-orders-from-database-page',
  templateUrl: './orders-from-database-page.component.html',
  styleUrls: ['./orders-from-database-page.component.scss']
})
export class OrdersFromDatabasePageComponent {
  orders: Order[] = [];
  kafkaMessages: KafkaMessage[] = [];
  searchTerm: string = '';
  orderForm: FormGroup;
  displayDialog: boolean;
  OrderStatusDisplayNames = OrderStatusDisplayNames;

  getDisplayName(status: OrderStatus): string {
    return this.OrderStatusDisplayNames[status];
  }
  get orderItems(): FormArray {
    return this.orderForm.get('orderItems') as FormArray;
  }

  constructor(private fb: FormBuilder, private orderService: OrderService, private notificationService: NotificationService,private orderStatusService:OrderStatusService) {
    this.orderForm = this.fb.group({
      customerId: ['', Validators.required],
      deliveryAddress: ['', Validators.required],
      orderItems: this.fb.array([
        this.createOrderItem()
      ])
    });

    this.displayDialog = false;
  }

  createOrderItem(): FormGroup {
    return this.fb.group({
      productId: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit() {
    this.subscribeToNotifications();
    this.getAllOrders();
  }

  ngOnDestroy(): void {
    this.notificationService.unsubscribeFromNotifications();
  }

  addOrderItem(): void {
    this.orderItems.push(this.createOrderItem());
  }

  removeOrderItem(index: number): void {
    this.orderItems.removeAt(index);
  }

  getStatusClass(status: OrderStatus): string {
    return 'status-' + status.toLowerCase();
  }

  subscribeToNotifications(): void {
    this.notificationService.subscribeToNotifications('orders').subscribe();
    this.notificationService.notifications$.subscribe((message) =>
      {
        this.kafkaMessages.push((message as unknown) as KafkaMessage);
      },
      (error) => {
        console.error('Error receiving Kafka message:', error);
      }
    );
  }

  getAllOrders(): void {
    this.orderService.findAllOrdersByStatus().subscribe(
      orders => this.orders = orders,
      error => console.error(error)
    );
  }

  showDialog() {
    this.displayDialog = true;
  }

  addOrder() {
    if (this.orderForm.valid) {
      this.orderService.createOrder(this.orderForm.value).subscribe(() => {
        this.getAllOrders();
        this.displayDialog = false;
      });
    }
  }

  applySearchFilter(value: string) {
    this.searchTerm = value;
  }

  initOrderItems(): FormGroup {
    return this.fb.group({
      productId: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1), Validators.max(100)]]
    });
  }

  getSeverity(status: OrderStatus): string {
    switch (status) {
      case OrderStatus.IN_PROGRESS:
        return 'warning';
      case OrderStatus.COMPLETED:
        return 'success';
      case OrderStatus.CANCELED:
        return 'danger';
      default:
        return '';
    }
  }

  getOrderTotal(order: Order): number {
    return order.orderItems.reduce((sum, item) => sum + item.productDetails.price * item.quantity, 0);
  }

  updateStatus(value: any, status: string): void {
    const order = JSON.parse(value) as Order;
    const orderStatus: OrderStatus =
      status === 'CANCEL' ? OrderStatus.CANCELED : OrderStatus.COMPLETED;
    this.orderStatusService.updateOrderStatus(order.id, orderStatus).subscribe(
      () => {
        setTimeout(() => {
          this.getAllOrders();
          this.kafkaMessages.pop()
        }, 1000);
      },
      (error) => {
        console.error(`Failed to update order status for ID ${order.id}`, error);
      }
    );
  }
}
