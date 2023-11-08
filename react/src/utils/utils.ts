import type { MenuProps } from 'antd';
import { Theme, ToastOptions, ToastPosition, toast } from 'react-toastify';


export function constantMenuProfile(url : string){
    const comsUrl = url.split('/');
    const key = comsUrl[comsUrl.length - 1];

    switch(key){
        case 'information-user':
            return 'Thông tin tài khoản'
        case 'address-ship-user':
            return 'Sổ địa chỉ'
        case 'order-user':
            return 'Đơn hàng của tôi'
        case 'bank-user':
            return 'Tài khoản ngân hàng'
    }
}

export async function getListProvincesCity(){
    const res = await fetch(`https://vapi.vnappmob.com/api/province/`,{
        method : "GET",
        headers : {
            'Content-Type' : 'Application/json'
        }
    })

    const { results } = await res.json();
    return results
}

export async function getListDistricts(provinceId : string){
    const res = await fetch(`https://vapi.vnappmob.com/api/province/district/${provinceId}`,{
        method : "GET",
        headers : {
            'Content-Type' : 'Application/json'
        }
    })

    const { results } = await res.json();
    return results
}

export async function getListWards(districtId : string){
    const res = await fetch(`https://vapi.vnappmob.com/api/province/ward/${districtId}`,{
        method : "GET",
        headers : {
            'Content-Type' : 'Application/json'
        }
    })

    const { results } = await res.json();
    return results
}

export type MenuItem = Required<MenuProps>['items'][number];
export function getItem(
    label: React.ReactNode,
    key: React.Key,
    icon?: React.ReactNode,
    children?: MenuItem[],
    type?: 'group',
  ): MenuItem {
    return {
      key,
      icon,
      children,
      label,
      type,
    } as MenuItem;
  }

export const convertPrice = (price : any) => {
    try {
        const result  = price?.toLocaleString().replaceAll(',', '.')
        return `${result} ₫`
    } catch (error) {
        return null
    }
}

export const convertDateAndTime = (isoDateString : any, num = 0) => {
    const date = new Date(isoDateString);

    const year = date.getFullYear(); // Lấy năm
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Lấy tháng (lưu ý rằng tháng bắt đầu từ 0)
    const day = (date.getDate()+num).toString().padStart(2, '0'); // Lấy ngày 
    const hours = date.getHours(); // Lấy giờ
    const minutes = date.getMinutes().toString().padStart(2, '0'); // Lấy phút
    //const seconds = date.getSeconds(); // Lấy giây
    //const milliseconds = date.getMilliseconds(); // Lấy mili giây

    return {
        date : `${day}-${month}-${year}`,
        time: `${hours}:${minutes}`
    }
}

// interface toastObject {
//     position : string
//     autoClose : number
//     hideProgressBar : boolean
//     closeOnClick : boolean
//     pauseOnHover : boolean
//     draggable : boolean
//     progress : undefined
//     theme : string
// }

export const toastMSGObject = ({
    position = "top-right" as ToastPosition,
    autoClose = 2000,
    hideProgressBar = false,
    closeOnClick = true,
    pauseOnHover = true,
    draggable = true,
    progress = undefined,
    theme = "colored" as Theme,
} = {}): ToastOptions<{}> => ({
  position,
  autoClose,
  hideProgressBar,
  closeOnClick,
  pauseOnHover,
  draggable,
  progress,
  theme
})

export function convertToSlug(text : string) {
    return text
      .toLowerCase()
      .replace(/[áàảãạâấầẩẫậăắằẳẵặ]/g, "a")
      .replace(/[éèẻẽẹêếềểễệ]/g, "e")
      .replace(/[íìỉĩị]/g, "i")
      .replace(/[óòỏõọôốồổỗộơớờởỡợ]/g, "o")
      .replace(/[úùủũụưứừửữự]/g, "u")
      .replace(/[ýỳỷỹỵ]/g, "y")
      .replace(/[^a-z0-9-]/g, "-")  // Loại bỏ các ký tự không hợp lệ
      .replace(/-+/g, "-");        // Loại bỏ các dấu gạch ngang liên tiếp
}

export function convertToShortNumber(number : number) {
    if (number >= 1000) {
      return (number / 1000).toFixed(1) + "k";
    } else {
      return number.toString();
    }
}

export function formatVietnamesePhone(phoneNumber : string) {
    // Loại bỏ tất cả các ký tự không phải số từ số điện thoại
    const cleanedPhoneNumber = phoneNumber.replace(/\D/g, '');
    
    // Kiểm tra nếu số điện thoại bắt đầu bằng "0", thêm "84" vào đầu số điện thoại
    if (cleanedPhoneNumber.startsWith('0')) {
      return `+84${cleanedPhoneNumber.slice(1)}`;
    }
    
    // Nếu không bắt đầu bằng "0", thêm "+" vào đầu số điện thoại
    return `+${cleanedPhoneNumber}`;
}

export function calculatePriceFinal ( firstPrice : number , discount : number) {
    return discount === 0 ? firstPrice : firstPrice - (firstPrice * discount)
}

export const handleChangeAmountBuy = (action : string , amountChange : number , amountRemain : number) => {
  console.log(amountChange)
  if(amountChange){
    switch (action) {
        case 'INCREASE':
            if(amountRemain < amountChange){
              toast.error(`Sản phẩm này chỉ còn lại ${amountRemain} cái`)
            }else{
              return (amountChange);
            }
            break;
        case 'DECREASE':           
            return (amountChange);            
        case 'INPUT':
            if(amountChange >=1 && amountChange <=999){
              return (amountChange);
            }else if(amountRemain < amountChange){
              toast.error(`Sản phẩm này chỉ còn lại ${amountRemain} cái`)
            }else{
              return (+(amountChange+'').slice(0,-1));
            }
            break;
        default:
            break;
    }
  }else{
    return 1;
  }
}
