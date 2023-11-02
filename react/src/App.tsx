import { BrowserRouter as Router, Routes, Route} from 'react-router-dom'
import { routes } from './routes'
import DefaultComponent from './components/DefaultComponent/DefaultComponent'
import {Fragment} from 'react'
import InforUserComponent from './components/InforUserComponent/InforUserComponent'
import OrderHistoryComponent from './components/OrderHistoryComponent/OrderHistoryComponent'
import AddressShipComponent from './components/AddressShipComponent/AddressShipComponent'
import 'react-toastify/dist/ReactToastify.css';


function App() {

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
                  <Route path={'information-user'} element={<InforUserComponent/>} />
                  <Route path={'address-ship-user'} element={<AddressShipComponent/>} /> 
                  <Route path={'order-user'} element={<OrderHistoryComponent/>} />                
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
