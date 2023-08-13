import {Component} from '@angular/core';
import {DeliveryService} from "../service/delivery.service";
import {Delivery} from "../interface/delivery";

@Component({
  selector: 'app-delivery-page',
  templateUrl: './delivery-page.component.html',
  styleUrls: ['./delivery-page.component.scss']
})
export class DeliveryPageComponent {

  deliveries: Delivery[] = [];
  statuses = [
    {name: 'All', value: ''},
    {name: 'Delivered', value: 'DELIVERED'},
    {name: 'Pending', value: 'PENDING'},
    {name: 'Canceled', value: 'CANCELED'}
  ];
  selectedStatus: any;

  constructor(private deliveryService: DeliveryService) {
  }

  ngOnInit(): void {
    this.getDeliveries()
  }



  getDeliveries(status?: string) {
    this.deliveryService.getAllDeliveries(status).subscribe(data => {
      this.deliveries = data;
    });
  }
  onStatusDropdownChange(event: any) {
    this.getDeliveries(event.value.value);
  }

  getDisplayName(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'PENDING';
      case 'DELIVERED':
        return 'DELIVERED';
      case 'CANCELED':
        return 'CANCELED';
      default:
        return 'Unknown Status';
    }
  }

  getSeverity(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'warning';
      case 'COMPLETED':
        return 'success';
      case 'CANCELED':
        return 'danger';
      default:
        return 'info';
    }
  }

  updateStatus(deliveryId: number, status: string) {
    this.deliveryService.updateDeliveryStatusById(deliveryId, status).subscribe(
      (response) => {
        console.log(`Status updated successfully to ${status}`);
        this.getDeliveries();
      },
      (error) => {
        console.error(`Failed to update status: ${error.message}`);
      }
    );
  }
}
