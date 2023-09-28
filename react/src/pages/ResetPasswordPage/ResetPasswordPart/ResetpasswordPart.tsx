import { useState } from "react";

// import React from 'react'
export function ResetPasswordPart({ email: string }: any) {
  const [password, setPassword] = useState({
    newPassword: "",
    repeatPassword: "",
    errPasswordMessage:""
  });

  const handleChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setPassword({
      ...password,
      [name]: value,
    });

    if (password.newPassword.length + password.repeatPassword.length === 0) {
      setPassword({
        ...password,
        errPasswordMessage: "Password is empty"
      })
      return;
    }
    if (password.newPassword !== password.repeatPassword) {
      setPassword({
        ...password,
        errPasswordMessage: "Password not match"
      })
      return;
    }

  };
  return (
    <form>
      <div className="form-group">
        <label htmlFor="exampleInputEmail1">Email address</label>
        <input
          type="password"
          className="form-control"
          id="newPassword"
          placeholder="New password"
          value={password.newPassword}
        />
        
      </div>
      <div className="form-group">
        <label htmlFor="exampleInputPassword1">Password</label>
        <input
          type="password"
          className="form-control"
          id="repeatPassword"
          placeholder="Repeat password"
          value={password.repeatPassword}
        />
      </div>
      <small className="form-text text-muted text-danger">
        {password.errPasswordMessage}
        </small>
      <button type="submit" className="btn btn-primary">
        Submit
      </button>
    </form>
  );
}
