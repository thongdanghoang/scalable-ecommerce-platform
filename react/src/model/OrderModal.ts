import { clothesOrder} from "./ClothesModal";
import { AddressShipping } from "./UserModal";

export interface Order {
    orderItems : clothesOrder[];
    totalPrice : number; // tính luôn cả giá giảm từ voucher hoặc free ship ...
    totalQuantity : number; // tổng số lượng clothes trong giỏ hàng , ví dụ 2 áo đỏ và 3 áo trắng thì = 5
}