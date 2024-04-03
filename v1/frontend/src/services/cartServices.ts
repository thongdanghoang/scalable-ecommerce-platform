const API_URL = import.meta.env.VITE_API_URL

export async function getCartService() {
    try {
        const response = await fetch(
            `${API_URL}/cart`,
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

export async function createCartService(classify : any) {
    try {
        const response = await fetch(
            `${API_URL}/cart`,
            {
                method: "POST",
                credentials: "include",
                headers:{
                    'Content-type': "application/json"
                },
                body: JSON.stringify(classify)
            }
        );
        const data = await response.json();
        return data;
    } catch (e) {
        console.log(e);
    }
    return null;
}

export async function updateCartService(updatedCart : any) {
    try {
        const response = await fetch(
            `${API_URL}/cart`,
            {
                method: "PUT",
                credentials: "include",
                headers:{
                    'Content-type': "application/json"
                },
                body: JSON.stringify(updatedCart)
            }
        );
        const data = await response.json();
        return data;
    } catch (e) {
        console.log(e);
    }
    return null;
}

export async function removeItemOutCartService(quantityId : number) {
    try {
        const response = await fetch(
            `${API_URL}/cart/${quantityId}`,
            {
                method: "DELETE",
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