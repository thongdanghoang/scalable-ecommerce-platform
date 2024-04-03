import { clothesCart, clothesOrder} from "./ClothesModal";
import { AddressShipping } from "./UserModal";

export interface Order {
    orderItems : clothesOrder[];
    totalPrice : number; // tính luôn cả giá giảm từ voucher hoặc free ship ...
    totalQuantity : number; // tổng số lượng clothes trong giỏ hàng , ví dụ 2 áo đỏ và 3 áo trắng thì = 5
}

export enum PaymentMethod {
    MOMO = 'MOMO',
    CASH_ON_DELIVERY = 'CASH_ON_DELIVERY',
    CREDIT_CARD = 'CREDIT_CARD'
}

export interface OrderCheckout {
    availableDeliveryMethods ?: string[],
    availablePaymentMethods ?: PaymentMethod[],
    deliveryMethod ?: string,
    paymentMethod ?: PaymentMethod,
    items : clothesCart[],
    total : number,
    shoppingFee : number,
    discount : number,
    grandTotal : number
}