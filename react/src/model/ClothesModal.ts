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
            quantityId : number,
            size : string;
            quantity : number;
        } 
    }
}

export interface clothesCart {
    product: {
        id: number,
        sku: string,
        name: string,
        image: string,
        price: number,
        discount: number,
        numberOfSold: number,
        rated: number,
        category: string
    },
    classification: {
        quantityId: number,
        sizeName: string,
        colorName: string
    },
    amount: number
}