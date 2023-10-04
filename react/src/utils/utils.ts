
export function constantMenuProfile(url : string){
    const comsUrl = url.split('/');
    const key = comsUrl[comsUrl.length - 1];

    switch(key){
        case 'information-user':
            return 'Information User'
        case 'address-ship-user':
            return 'Address Shipping'
        case 'order-user':
            return 'Order History'
        case 'bank-user':
            return 'Linked Bank Account'
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