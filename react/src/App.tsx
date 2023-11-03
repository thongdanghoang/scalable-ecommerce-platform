import { BrowserRouter as Router, Routes, Route, useLocation} from 'react-router-dom'
import { routes } from './routes'
import DefaultComponent from './components/DefaultComponent/DefaultComponent'
import {Fragment} from 'react'
import InforUserComponent from './components/InforUserComponent/InforUserComponent'
import OrderHistoryComponent from './components/OrderHistoryComponent/OrderHistoryComponent'
import AddressShipComponent from './components/AddressShipComponent/AddressShipComponent'
import 'react-toastify/dist/ReactToastify.css';
import MyOrderPage from './pages/MyOrderPage/MyOrderPage'
import {useEffect} from 'react'
import { profileService } from './services/userService'
import { toast } from 'react-toastify'
import { calculatePriceFinal, toastMSGObject } from './utils/utils'
import { useDispatch, useSelector } from 'react-redux'
import { updateUser } from './redux/slides/userSlide'
import { RootState } from './redux/store'
import { getCartService, updateCartService } from './services/cartServices'
import { cloneOrder } from './redux/slides/orderSlide'
import { clothesCart, clothesOrder } from './model/ClothesModal'

function App() {
  const order = useSelector((state: RootState) => state.order);
  const user = useSelector((state: RootState) => state.user);
  const dispatch = useDispatch();
  
  // handle login with google 
  useEffect(() => {
    profileService()
      .then(res => {
        dispatch(updateUser(res.data));
        toast.success('Login with google successfully!', toastMSGObject())
      })
  },[])

  // handle get cart by user

  const handleGetCartUser = async () => {
    const res = await getCartService();
    if(res?.items.length !== 0){
      const cartMapping = res?.items.map((item : clothesCart) => ({
        id : item.product.id,
        amountBuy : item.amount,
        category : item.product.category,
        discount : item.product.discount,
        name : item.product.name,
        numberOfSold : item.product.numberOfSold,
        price : item.product.price,
        rated : item.product.rated,
        sku : item.product.sku,
        classifyClothes : {
          color : item.classification.colorName,
          images : [`http://localhost:8080/api/products/images/${item.product.image}`],
          quantities : {
            quantityId : item.classification.quantityId,
            size : item.classification.sizeName            
          }
        }
      }))
      dispatch(cloneOrder({
        orderItems : cartMapping,
        totalQuantity : cartMapping.reduce((total : number , item : clothesOrder) => total + item.amountBuy,0),
        totalPrice : cartMapping.reduce((total : number, item : clothesOrder ) => {
          const itemPriceFinal = calculatePriceFinal(item.price, item.discount);
          return total + itemPriceFinal * item.amountBuy;
        }, 0)
      }))
    }
  }

  useEffect(() => {
    user.username && handleGetCartUser();
  },[user.username])

  // handle update cart 

  const handleMapCart = () => {
    return order.orderItems.map(item => ({
      quantityId : item.classifyClothes.quantities.quantityId,
      amount : item.amountBuy
    }))
  }

  useEffect(() => {
    if(user.username){
      const handle = setTimeout(async () => {
        await updateCartService({
          items : handleMapCart()
        })
      }, 1500)
      return () => {
        clearTimeout(handle)
      }
    }
  },[order.orderItems])

  return (
    <Router>
      <Routes>
        {routes.map((route) => {
          const Page = route.page
          const Layout = route.isShowHeader ? DefaultComponent : Fragment
          return (
            <Route key={route.path} path={route.path} element={
                <Layout>
                  <Page />
                </Layout>
              } 
            >
              {route.path === '/profile-user' ? (
                <>
                  <Route path='information-user' element={<InforUserComponent/>} />
                  <Route path='address-ship-user' element={<AddressShipComponent/>} /> 
                  <Route path='order-user' element={<OrderHistoryComponent/>} >
                    <Route path=':code' element={<MyOrderPage/>}/>
                  </Route>                
                </>
              ) : <Fragment/>}
              
            </Route>
          )
        })}
      </Routes>
    </Router>
    
  )
}

export default App
