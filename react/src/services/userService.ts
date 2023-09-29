import { UserProfileUpdated, userRegister } from "../model/UserModal";

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

export async function updateProfileService(UserProfileUpdated : UserProfileUpdated) {
  try {
    const res = await fetch('https://thongdanghoang.id.vn/swp391-back/api/user/profile/basic' , {
        method: 'PUT', 
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(UserProfileUpdated)
      })

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error)
  }
  return null;
}

export async function verifyEmailService(email:string , code:string) {
  try {
    const res = await fetch(`https://thongdanghoang.id.vn/swp391-back/api/user/auth/verify-email?mail=${email}`+(code ? `&code=${code}` : '') , {
        method: 'POST', 
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      })

    const data = await res.json();
    return data;
  } catch (error) {
    console.log(error)
  }
  return null;
}

export async function sendEmailOTPForgetPassword(email: string): Promise<any> {
  let emailParam = new URLSearchParams({email: email});
  console.log(emailParam.toString())
  try {
    const res = await fetch(`https://thongdanghoang.id.vn/swp391-back/api/user/auth/forgot-password?${emailParam.toString()}` , {
        method: 'POST', 
      });
    return res.json();
  } catch (error) {
    console.log(error);
    
  }
  return null;
}