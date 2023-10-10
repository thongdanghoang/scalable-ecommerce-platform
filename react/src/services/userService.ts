import { UserProfileUpdated, userRegister , AddressShipping } from "../model/UserModal";
import {API_URL} from '../utils/constants'

export async function loginService(formLogin: URLSearchParams) {
  try {
    const response = await fetch(
      `${API_URL}/login`,
      {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          //'Access-Control-Allow-Origin':'*',
          // 'Access-Control-Allow-Methods':'POST,PATCH,OPTIONS'
        },
        body: formLogin.toString(),
      }
    );
    const data = await response.json();
    return data;
  } catch (e) {
    console.log(e);
  }
  return null;
}

export async function registerService(userRegister: userRegister) {
  try {
    const res = await fetch(
      `${API_URL}/api/user/auth/register`,
      {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userRegister),
      }
    );

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function profileService() {
  try {
    const res = await fetch(
      `${API_URL}/api/user/profile`,
      {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function logoutService() {
  try {
    const res = await fetch(`${API_URL}/logout`, {
      method: "GET",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function updateProfileService(
  UserProfileUpdated: UserProfileUpdated
) {
  try {
    const res = await fetch(
      `${API_URL}/api/user/profile/basic`,
      {
        method: "PUT",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(UserProfileUpdated),
      }
    );

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function verifyEmailService(email: string, code: string) {
  try {
    const res = await fetch(
      `${API_URL}/api/user/auth/verify-email?mail=${email}` +
        (code ? `&code=${code}` : ""),
      {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function forgetPasswordByMail(
  email: string,
  code?: string
): Promise<Response | null> {
  let requestParam: URLSearchParams;
  if (code == undefined) {
    requestParam = new URLSearchParams({ email: email });
  } else {
    requestParam = new URLSearchParams({ email: email, code: code });
  }
  try {
    const res = await fetch(
      `${API_URL}/api/user/auth/forgot-password-by-mail?${requestParam.toString()}`,
      {
        method: "POST",
        credentials: "include",
      }
    );
    return res;
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function resetPassword(
  newPassword: string
): Promise<Response | null> {
  let requestParam = new URLSearchParams({ newPassword: newPassword });
  try {
    //NOTE: This API maybe is under developing in back-end
    // Detail error:
    // Object {
    //   timestamp: "2023-09-29T20:48:07.276717",
    //   status: 500,
    //   error: "Internal Server Error",
    //   message: "An unexpected application error occurred: Missing cookie 'verificationResetPassword' for method parameter of type String"
    // }

    const res = await fetch(
      `http://localhost:8080/api/user/auth/reset-password?${requestParam.toString()}`,
      {
        method: "POST",
        credentials: "include",
      }
    );
    return res;
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function createAddressShip(
  addressShip : AddressShipping
) {
  try {
    const res = await fetch(
      `http://localhost:8080/api/user/address`,
      {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(addressShip),
      }
    );
    return await res.json();
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function getAddressShipsByUser(){
  try {
    const res = await fetch(
      `http://localhost:8080/api/user/address`,
      {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        }
      }
    );
    return await res.json();
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function updateAddressShip(
  addressShip : AddressShipping
){
  try {
    const res = await fetch(
      `http://localhost:8080/api/user/address/update`,
      {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body : JSON.stringify(addressShip)
      }
    );
    return await res.json();
  } catch (error) {
    console.log(error);
  }
  return null;
}

export async function deleteAddressShip(
  idAddressShip : number
){
  try {
    const res = await fetch(
      `http://localhost:8080/api/user/address`,
      {
        method: "DELETE",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body : JSON.stringify({id : idAddressShip})
      }
    );
    return await res.json();
  } catch (error) {
    console.log(error);
  }
  return null;
}

