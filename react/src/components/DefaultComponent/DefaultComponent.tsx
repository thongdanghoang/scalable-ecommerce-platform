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
  return (
    <CartContext.Provider value={{isHiddenCart , setIsHiddenCart}}>
      <HeaderComponent />
      {children}
      <FooterComponent />
    </CartContext.Provider>
  );
};

export default DefaultComponent;
