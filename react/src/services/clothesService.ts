const API_URL = import.meta.env.VITE_API_URL

export async function getAllClothes(): Promise<Response | null> {
  try {
    const response = await fetch(`${API_URL}/api/products?page=0&limit=30`, {
      method: "GET",
    });
    return response;
  } catch (error) {}
  return null;
}

export async function getClothesOrderBy(order: string): Promise<Response | null> {
  let params = new URLSearchParams({
    sort: order
  })
  try {
    const response = await fetch(`${API_URL}/api/products/search?${params.toString()}`, {
      method: "GET",
    });
    return response;
  } catch (error) {}
  return null;
}

export async function uploadImageClothes(file : any){
  const formData = new FormData();
  formData.append("file", file);
  try {
    const response = await fetch(`${API_URL}/api/products/image/upload`, {
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

export async function getClothesById(idClothes: number) {
  try {
    const response = await fetch(`${API_URL}/api/products/${idClothes}`, {
      method: "GET",
    });
    return await response.json();
  } catch (error) {}
  return null;
}

export async function createNewClothes(newClothes : any) {
  try {
    const response = await fetch(`${API_URL}/api/products`, {
      method: "POST",
      credentials: "include",
      headers: {
        'Content-type' : 'application/json'
      },
      body: JSON.stringify(newClothes)
    });
    return await response.json();
  } catch (error) {}
  return null;
}

export async function updateClothes(updatedClothes : any) {
  try {
    const response = await fetch(`${API_URL}/api/products`, {
      method: "PUT",
      credentials: "include",
      headers: {
        'Content-type' : 'application/json'
      },
      body: JSON.stringify(updatedClothes)
    });
    return await response.json();
  } catch (error) {}
  return null;
}

export async function sortAndFilterClothes(
  params: URLSearchParams,
): Promise<any> {
  try {
    let response = await fetch(
      `${API_URL}/api/products/search?${params.toString()}`,
      {
        method: "GET",
      },
    );
    let data = await response.json();
    return data;
  } catch (e) {
    console.log(e);
  }
  return null;
}

export async function getCategories() {
  try {
    let response = await fetch(`${API_URL}/api/products/categories`, {
      method: "GET"
    });
    let data = await response.json();
    return data;
  } catch (error) {
    console.log(error)
  }
}

export async function searchClother(searchText: string) {
  let params = new URLSearchParams({keyword: searchText, limit: "10"});
  try {
    let response = await fetch(
      `${API_URL}/api/products/search?${params.toString()}`,
      {
        method: "GET",
      },
    );
    let data = await response.json();
    return data;
  } catch (e) {
    console.log(e);
  }
  return null;
}
