export interface clothes {
    id: number;
    image: any;
    name: string;
    price: number;
}

export interface clothesOrder extends clothes{
    category: string;
    discount: number;
    size: string;
    amountBuy: number;
    classifyClothes: {
        color : string;
        images : string;
        quantities : {
            size : string;
            quantity : number;
        } 
    }
}