export interface clothes {
    id: number;
    image: any;
    name: string;
    price: number;
}

export interface clothesOrder extends clothes{
    category: string;
    discount: number;
    color: string;
    size: string;
    amountBuy: number;
}