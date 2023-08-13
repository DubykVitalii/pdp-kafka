export interface Delivery {
  id: number;
  status: DeliveryStatus;
  order: Order;
}

export enum DeliveryStatus {
  PENDING,
  DELIVERED,
  CANCELED
}

export interface Order {
  id: number;
  createdAt: string;
  customerId: number;
  deliveryAddress: string;
  orderItems: OrderItem[];
  status: OrderStatus;
}

export interface OrderItem {
  id: number;
  productDerails:ProductDetails;
  quantity: number;
}

export interface ProductDetails{
  id:number;
  name:string;
  price:number;
}

export enum OrderStatus {
  IN_PROGRESS,
  COMPLETED,
  CANCELED
}

