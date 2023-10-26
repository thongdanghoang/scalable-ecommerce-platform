import { API_URL } from "../utils/constants";

export async function getAllClothes(): Promise<Response | null> {
  try {
    const response = await fetch(`${API_URL}/api/products?page=0&limit=30`, {
      method: "GET",
    });
    return response;
  } catch (error) {}
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
