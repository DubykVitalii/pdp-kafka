import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductKafkaMessage} from "../../products/products-from-kafka/interface/product";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private apiUrlProducts = 'http://localhost:8080/api/v1/messages/products';
  constructor(private http: HttpClient) {}

  getProductMessagesByName(name: string): Observable<ProductKafkaMessage[]> {
    const searchUrl = `${this.apiUrlProducts}?name=${name}`;
    return this.http.get<ProductKafkaMessage[]>(searchUrl);
  }

  // getOrderMessagesByCustomerId(customerId: number): Observable<OrderKafkaMessage[]> {
  //   const searchUrl = `${this.apiUrlOrders}?customerId=${customerId}`;
  //   return this.http.get<OrderKafkaMessage[]>(searchUrl);
  // }
}
