import './CartEmpty.css'

export default function CartEmptyComponent() {
  return (
    <div id='CartEmpty'>
      <img src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/blank_cart.svg?1697103821" alt="" />
      <div className="notify-content">
        Giỏ hàng của bạn đang trống
      </div>
      <div className='btn-buy-now'>Mua ngay</div>
    </div>
  )
}
