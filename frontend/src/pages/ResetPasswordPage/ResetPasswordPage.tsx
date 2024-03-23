import EmailInputPart from "./EmailInputPart/EmailInputPart"

// import './ResetPassword.css'
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const a = () => {
    return (
        <div id="ResetPasswordPage">
        <div className="card text-center" style={{width: '300px'}}>
            <div
                className="card-header h5 text-white bg-primary border-radius-top"
            >
                Password Reset
            </div>
            <div className="card-body px-5">
                <p className="card-text py-2">
                    Enter your email address and we'll send you an email with instructions
                    to reset your password.
                </p>
                <div className="form-outline">
                    <input type="email" id="typeEmail" className="form-control my-3" />
                    <label className="form-label" htmlFor="typeEmail">Email input</label>
                </div>
                <a href="#" className="btn btn-primary w-100">Reset password</a>
                <div className="d-flex justify-content-between mt-4">
                    <a className="" href="#">Login</a>
                    <a className="" href="#">Register</a>
                </div>
            </div>
        </div>
    </div>
    )
}
export default function ResetPasswordPage() {
  return (
    <EmailInputPart/>
  )
}
