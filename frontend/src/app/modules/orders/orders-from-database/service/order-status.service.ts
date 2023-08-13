import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {OrderStatus} from "../interface/order";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OrderStatusService {

  private apiUrl = 'http://localhost:8080/api/v1/orders/status';

  constructor(private http: HttpClient) {}

  updateOrderStatus(id: number, status: OrderStatus): Observable<void> {
    const orderStatusKafkaMessageRecord = {
      id,
      status,
    };
    return this.http.put<void>(this.apiUrl, orderStatusKafkaMessageRecord);
  }
}
