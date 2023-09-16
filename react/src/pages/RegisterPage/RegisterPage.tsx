import React,{ useState } from 'react';
import style from './register.module.css'
//import { User } from '../../redux/slides/userSlide';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    password2: '',
  });

  console.log(formData)

  const handleSignUp = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    
  }

  return (
    <div className={style.background} style={{height:"100vh"}}>
      <div className={style.container}>
        <h1>Register</h1>
        <form onSubmit={handleSignUp}>
          <div className={style[`form-control`]}>
            <input 
              required 
              type="text" 
              name="username" 
              value={formData.username}
              id="username" 
              placeholder="Username" 
              onChange={e => setFormData({...formData,[e.target.name] : e.target.value})}
            />
            <span></span>
            <small></small>
          </div>
          <div className={style[`form-control`]}>
            <input 
              required 
              type="email" 
              name="email"
              value={formData.email}
              id="email" 
              placeholder="Email" 
              onChange={e => setFormData({...formData,[e.target.name] : e.target.value})}
            />
            <span></span>
            <small></small>
          </div>
          <div className={style[`form-control`]}>
            <input 
              required 
              type="password"
              name="password" 
              value={formData.password}
              id="password"
              placeholder="Password" 
              pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
              onInvalid={(e:React.ChangeEvent<HTMLInputElement>) => e.target.setCustomValidity('Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters')}              
              onChange={e => setFormData({...formData,[e.target.name] : e.target.value})}
            />
            <span></span>
            <small></small>
          </div>
          <div className={style[`form-control`]}>
            <input
              required
              type="password"
              name="password2"
              value={formData.password2}
              id="password2"
              pattern={`^${formData.password}$`}
              onInvalid={(e:React.ChangeEvent<HTMLInputElement>) => e.target.setCustomValidity('Password confirm is not same password')}              
              placeholder="Confirm password"
              onChange={e => setFormData({...formData,[e.target.name] : e.target.value})}
            />
            <span></span>
            <small></small>
          </div>
          <input type='submit' value="Register" />
          <div className="signup_link">Already a member? <a href="#">Login</a></div>				
        </form>
      </div>
    </div>
  )
}
