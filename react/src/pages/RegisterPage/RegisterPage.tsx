import './register.css'

export default function RegisterPage() {
  return (
    <div id='RegisterPage' className='background' style={{height:"100vh"}}>
      <div className='container'>
        <h1>Register</h1>
        <form>
          <div className='form-control'>
            <input type="text" id="username" placeholder="Username" />
            <span></span>
            <small></small>
          </div>
          <div className='form-control'>
            <input type="email" id="email" placeholder="Email" />
            <span></span>
            <small></small>
          </div>
          <div className='form-control'>
            <input type="password" id="password" placeholder="Password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"  />
            <span></span>
            <small></small>
          </div>
          <div className='form-control'>
            <input
              type="password"
              id="password2"
              placeholder="Confirm password"
            />
            <span></span>
            <small></small>
          </div>
          <input type="submit" value="Register" />
          <div className="signup_link">Already a member? <a href="#">Login</a></div>				
        </form>
      </div>
    </div>
  )
}
