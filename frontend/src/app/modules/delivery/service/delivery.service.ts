import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Delivery} from "../interface/delivery";


@Injectable({
  providedIn: 'root'
})
export class DeliveryService {

  private apiUrl = 'http://localhost:6003/api/v1/deliveries';

  constructor(private http: HttpClient) {}

  getAllDeliveries(status?: string): Observable<Delivery[]> {
    const params = status ? new HttpParams().set('status', status) : undefined;
    return this.http.get<Delivery[]>(this.apiUrl, { params });
  }

  getDeliveryById(id: number): Observable<Delivery> {
    return this.http.get<Delivery>(`${this.apiUrl}/${id}`);
  }

  updateDeliveryStatusById(id: number, status: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/status`, null, {
      params: { status: status.toString() }
    });
  }
}

