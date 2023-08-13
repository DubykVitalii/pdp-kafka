import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import {NewProduct, Product} from "../interface/product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = 'http://localhost:6001/api/v1/products';

  constructor(private http: HttpClient) { }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  getAllProducts(available: boolean): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl, { params: { available: String(available) } });
  }

  createProduct(product: NewProduct): Observable<void> {
    return this.http.post<void>(this.apiUrl, product);
  }

  updateProduct(id: number, product: Product): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, product);
  }
}
