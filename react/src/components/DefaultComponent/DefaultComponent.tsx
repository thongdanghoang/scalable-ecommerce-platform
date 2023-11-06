import { useLocation } from "react-router-dom";
import FooterComponent from "../FooterComponent/FooterComponent";
import HeaderComponent from "../HeaderComponent/HeaderComponent";
import { ReactNode , useState , createContext } from "react";
import { ToastContainer } from "react-toastify";

export interface CartContextType {
  isHiddenCart: boolean;
  setIsHiddenCart: React.Dispatch<React.SetStateAction<boolean>>;
}

export const CartContext = createContext<CartContextType | undefined>(undefined);

const DefaultComponent: React.FC<{ children: ReactNode }> = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [isHiddenCart , setIsHiddenCart] = useState(false);
  const locate = useLocation(); console.log(locate.pathname)
  return (
    <CartContext.Provider value={{isHiddenCart , setIsHiddenCart}}>
      <ToastContainer/>
      {locate.pathname !== '/system/admin' &&
       locate.pathname !== '/order/payment' &&
       locate.pathname !== '/payment/success' ? (
        <>
          <HeaderComponent />
          {children}
          <FooterComponent />
        </>
      ) : children}
    </CartContext.Provider>
  );
};

export default DefaultComponent;
