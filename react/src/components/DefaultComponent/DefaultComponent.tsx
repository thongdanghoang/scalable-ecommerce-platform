import { useLocation } from "react-router-dom";
import FooterComponent from "../FooterComponent/FooterComponent";
import HeaderComponent from "../HeaderComponent/HeaderComponent";
import { ReactNode , useState , createContext } from "react";

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
      {locate.pathname !== '/system/admin' ? (
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
