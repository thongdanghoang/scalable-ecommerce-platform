import {API_URL} from '../utils/constants';

export async function getAllClothes(): Promise<Response | null> {
    try {
        const response = await fetch(`${API_URL}/api/products?page=0&limit=30`,
        {
            method: "GET",
        });
        return response;
    } catch (error) {
        
    }
    return null;
}