import { Spin } from "antd";
import { ReactNode } from "react";

interface propsLoading {
    isloading: boolean,
    delay?: number,
    children: ReactNode
}
// eslint-disable-next-line react/prop-types
export default function LoadingComponent({isloading,children,delay = 0} : propsLoading) {
  return (
    <Spin spinning={isloading} size="large" delay={delay}>
        {children}
    </Spin>
  )
}
