export interface Order {
  id: number;
  customerId: number;
  deliveryAddress: string;
  orderItems: OrderItem[];
  status: OrderStatus;
}

export enum OrderStatus {
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
  CANCELED = "CANCELED"
}

export const OrderStatusDisplayNames = {
  [OrderStatus.IN_PROGRESS]: 'IN PROGRESS',
  [OrderStatus.COMPLETED]: 'COMPLETED',
  [OrderStatus.CANCELED]: 'CANCELED',
}

export interface OrderItem {
  id: number;
  productDetails: ProductDetails;
  quantity: number;
}

export interface ProductDetails {
  id: number;
  name: string;
  price: number;
}

export interface NewOrderItem {
  productId: number;
  quantity: number;
}

export interface NewOrder {
  customerId: number;
  deliveryAddress: string;
  orderItems: NewOrderItem[];
}
