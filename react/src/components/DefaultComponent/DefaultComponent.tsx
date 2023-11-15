import {useLocation } from "react-router-dom";
import FooterComponent from "../FooterComponent/FooterComponent";
import HeaderComponent from "../HeaderComponent/HeaderComponent";
import { ReactNode , useState , createContext } from "react";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";

export interface CartContextType {
  isHiddenCart: boolean;
  setIsHiddenCart: React.Dispatch<React.SetStateAction<boolean>>;
}

export const CartContext = createContext<CartContextType | undefined>(undefined);

const DefaultComponent: React.FC<{ children: ReactNode }> = ({
  children,
}: {
  children: ReactNode 
}) => {
  const [isHiddenCart , setIsHiddenCart] = useState(false);
  const locate = useLocation(); console.log(locate.pathname);
  const user = useSelector((state: RootState) => state.user);

  // const checkShowHeaderFooterPage = () => {
  //   if(isShowHeaderFooter){
  //     return (
  //       <>
  //         <HeaderComponent />
  //           {children}
  //         <FooterComponent />
  //       </>
  //     )
  //   }else{
  //     return children
  //   }
  // }

  // const checkRoleToPage = () => {
  //   if(role.length === 0 ){
  //     checkShowHeaderFooterPage()
  //   }else{
  //     if(role.includes(user.role as Role)){
  //       checkShowHeaderFooterPage()
  //     }else{
  //       console.log('sss')
  //       return <Navigate to="/" />
  //     }
  //   }
  // }

  return (
    <CartContext.Provider value={{isHiddenCart , setIsHiddenCart}}>
        {locate.pathname === '/system' ? (
          children
        ) : (
          <>
            <HeaderComponent />
              {children}
            <FooterComponent />
          </>
        )}
    </CartContext.Provider>
  );
};

export default DefaultComponent;
