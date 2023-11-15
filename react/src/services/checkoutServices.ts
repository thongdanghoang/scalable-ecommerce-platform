const API_URL = import.meta.env.VITE_API_URL

export async function checkoutInfoService() {
    try {
        const response = await fetch(
            `${API_URL}/api/orders/checkout-info`,
            {
                method: "GET",
                credentials: "include",
            }
        );
        const data = await response.json();
        return data;
    } catch (e) {
        console.log(e);
    }
    return null;
}

export async function checkoutService(restInfoOrder : any) {
    try {
        const response = await fetch(
            `${API_URL}/api/orders/checkout`,
            {
                method: "POST",
                credentials: "include",
                headers: {
                    'COntent-type' : 'application/json'
                },
                body: JSON.stringify(restInfoOrder)
            }
        );
        const data = await response.json();
        return data;
    } catch (e) {
        console.log(e);
    }
    return null;
}



