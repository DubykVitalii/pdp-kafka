import {OrderStatus} from "../../../orders/orders-from-database/interface/order";

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
  availableProductCount: number;
}

export interface NewProduct {
  name: string;
  description: string;
  price: number;
  available: boolean;
  availableProductCount: number;
}
