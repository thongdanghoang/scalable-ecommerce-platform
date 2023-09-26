import { userRegister } from "../pages/RegisterPage/RegisterPage";

export async function loginService(
  formLogin: URLSearchParams
){
  try {
    const response = await fetch(
      "https://thongdanghoang.id.vn/swp391-back/login",
      {
        method: "POST",
        credentials: 'include',
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          //'Access-Control-Allow-Origin':'*',
          // 'Access-Control-Allow-Methods':'POST,PATCH,OPTIONS'
        },
        body: formLogin.toString(),
      }
    );
    const data = await response.json()
    return data
  } catch (e) {
    console.log(e);
  }
  return null;
}

export async function registerService(userRegister : userRegister) {
  try {
    const res = await fetch('https://thongdanghoang.id.vn/swp391-back/api/user/auth/register' , {
        method: 'POST', 
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userRegister)
      })

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error)
  }
  return null;
}

export async function profileService() {
  try {
    const res = await fetch('https://thongdanghoang.id.vn/swp391-back/api/user/profile' , {
        method: 'GET', 
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
      })

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error)
  }
  return null;
}

export async function logoutService() {
  try {
    const res = await fetch('https://thongdanghoang.id.vn/swp391-back/logout' , {
        method: 'GET', 
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
      })

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error)
  }
  return null;
}
