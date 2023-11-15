const API_URL = import.meta.env.VITE_API_URL;

export async function getCategoriesReport() {
  try {
    let response = await fetch(`${API_URL}/api/products/report`, {
      method: "GET",
      credentials: "include",
    });
    let data = await response.json();
    return data;
  } catch (error) {
    console.log(error);
  }
}
export async function getCategoriesStatistic() {
  try {
    let response = await fetch(`${API_URL}/api/products/statistic`, {
      method: "GET",
      credentials: "include",
    });
    let data = await response.json();
    return data;
  } catch (error) {
    console.log(error);
  }
}
