export async function loginService(
  formLogin: URLSearchParams
): Promise<Response | null> {
  try {
    const response = await fetch(
      "https://thongdanghoang.id.vn/swp391-back/login",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          // 'Access-Control-Allow-Origin':'*',
          // 'Access-Control-Allow-Methods':'POST,PATCH,OPTIONS'
        },
        body: formLogin.toString(),
      }
    );
    return response;
  } catch (e) {
    console.log(e);
  }
  return null;
}
