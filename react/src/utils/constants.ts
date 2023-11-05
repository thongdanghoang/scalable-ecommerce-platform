export const API_URL = 'http://localhost:8080'

export const paymentImage = (key : string) => {
    switch (key) {
        case 'MOMO':
            return 'https://www.coolmate.me/images/momo-icon.png'
        case 'CASH_ON_DELIVERY':
            return 'https://salt.tikicdn.com/ts/upload/92/b2/78/1b3b9cda5208b323eb9ec56b84c7eb87.png'
        default:
            return 'https://salt.tikicdn.com/ts/upload/7e/48/50/7fb406156d0827b736cf0fe66c90ed78.png'
    }
}