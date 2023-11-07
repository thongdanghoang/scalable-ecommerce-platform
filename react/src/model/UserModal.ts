export enum Role {
    '[ROLE_USER]' = '[ROLE_USER]',
    '[ROLE_ADMIN]' = '[ROLE_ADMIN]',
    '[ROLE_SHOP_OWNER]' = '[ROLE_SHOP_OWNER]'
}

export interface UserSystem {
    username : string,
    password ?: string,
    role : string,
    enabled ?: boolean
}

export interface User {
    version : number,
    username: string,
    fullName: string,
    avatar: string,
    role: string
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
    id ?: number,
    version ?: number,
    fullName : string,
    phone : string,
    province : string,
    district : string,
    ward : string,
    addressDetail : string,
    type : string,
    default : boolean
}