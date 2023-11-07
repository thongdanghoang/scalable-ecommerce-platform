// export const API_URL = 'https://thongdanghoang.id.vn/swp391'
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

export const paymentName = (key : string) => {
    switch (key) {
        case 'MOMO':
            return 'Thanh toán bằng MOMO'
        case 'CASH_ON_DELIVERY':
            return 'Thanh toán khi nhận hàng (COD)'
        default:
            return 'Thanh toán bằng thẻ tín dụng (CREDIT CARD)'
    }
}

export const statusOrder = [
    {
        key : 'PENDING',
        value : 'Chờ xác nhận'
    },
    {
        key : 'PROCESSING',
        value : 'Đang xử lí'
    },
    {
        key : 'DELIVERING',
        value : 'Đang vận chuyển'
    },
    {
        key : 'COMPLETED',
        value : 'Đã giao'
    },
    {
        key : 'CANCELLED',
        value : 'Đã hủy'
    },
]


