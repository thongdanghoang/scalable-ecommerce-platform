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

export async function uploadImageProfileService(file : any){
    const formData = new FormData();
    formData.append("file", file);
    try {
      const response = await fetch(`${API_URL}/api/user/profile/image/upload`, {
        method: "POST",
        credentials: 'include',
        body: formData,
      });
      return await response.json();
    } catch (error) {
      console.log(error)
    }
    return null;
}

export async function updateAvatarProfileService(avatarName : string) {
    try {
        let requestParam = new URLSearchParams({ avatarUrl: avatarName });
        const response = await fetch(
            `${API_URL}/api/user/profile/avatar?${requestParam.toString()}`,
            {
                method: "PUT",
                credentials: "include",
                headers: {
                    'Content-type' : 'application/json'
                }
            }
        );
        const data = await response.json();
        return data;
    } catch (e) {
        console.log(e);
    }
    return null;
}

