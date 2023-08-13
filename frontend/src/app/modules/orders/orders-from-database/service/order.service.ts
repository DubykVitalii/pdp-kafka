import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import {NewOrder, Order} from "../interface/order";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:6002/api/v1/orders';

  constructor(private http: HttpClient) { }

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/${id}`);
  }

  findAllOrdersByStatus(status?: string): Observable<Order[]> {
    let params = status ? { params: { status: status } } : {};
    return this.http.get<Order[]>(`${this.apiUrl}`, params);
  }

  findAllOrdersByCustomerId(id: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/user/${id}`);
  }

  createOrder(order: NewOrder): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, order);
  }

  updateOrder(id: number, order: Order): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${id}`, order);
  }

  completeOrder(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/complete/${id}`, null, { responseType: 'text' as 'json' });
  }

  cancelOrder(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/cancel/${id}`, null, { responseType: 'text' as 'json' });
  }
}
