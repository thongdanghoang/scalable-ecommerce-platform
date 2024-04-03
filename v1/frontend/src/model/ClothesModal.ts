export interface clothes {
    id: number;
    image ?: string;
    name: string;
    price: number;
    discount?: number;
    rated?: number;
    numberOfSold?: number;
}

export interface clothesOrder{
    id: number;
    name: string;
    price: number;
    category: string;
    discount: number;
    amountBuy: number;
    classifyClothes: {
        color : string;
        images : string;
        quantities : {
            quantityId : number,
            size : string;
            quantityInStock ?: number;
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