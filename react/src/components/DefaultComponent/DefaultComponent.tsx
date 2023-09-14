import HeaderComponent from "../HeaderComponent/HeaderComponent"
import { ReactNode } from 'react';


const DefaultComponent:React.FC<{children: ReactNode}> = ({ children }: { children: ReactNode }) => {
    return (
      <div>
          <HeaderComponent />
          {children}
      </div>
    )
}
  
export default DefaultComponent