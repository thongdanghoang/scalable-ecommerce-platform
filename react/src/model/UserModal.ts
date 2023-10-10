export interface User {
    version : number,
    username: string,
    fullName: string,
    email: string,
    phone : string,
    gender : string,
    birthday : string,
    weight : number,
    height : number,
    emailVerified : boolean,
    phoneVerified : boolean,
}

export interface userRegister {
    username: string,
    email: string,
    password: string,
    password2: string,
}

export interface UserProfileUpdated {
    version : number,
    username : string,
    fullName : string,
    gender : string,
    birthday : string,
    weight : number,
    height : number
}

export interface AddressShipping {
    id ?: number
    fullName : string,
    phone : string,
    province : string,
    district : string,
    ward : string,
    addressDetail : string,
    type : string,
    default : boolean
}