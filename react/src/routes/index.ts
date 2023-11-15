import HomePage from "../pages/HomePage/HomePage";
import LoginPage from "../pages/LoginPage/LoginPage";
import NotFoundPage from "../pages/NotFoundPage/NotFoundPage";
import ProductDetailPage from "../pages/ProductDetailPage/ProductDetailPage";
import RegisterPage from "../pages/RegisterPage/RegisterPage";
import ResetPasswordPage from "../pages/ResetPasswordPage/ResetPasswordPage";
import UserProfilePage from "../pages/UserProfilePage/UserProfilePage";
import ProductsFilterPage from "../pages/ProductsFilterPage/ProductsFilterPage";
import AdminPage from "../pages/AdminPage/AdminPage";
import OrderPage from "../pages/OrderPage/OrderPage";
import PaymentPage from "../pages/PaymentPage/PaymentPage";
import PaymentSuccessPage from "../pages/PaymentSuccessPage/PaymentSuccessPage";
import ProductSearchPage from "../pages/ProductSearchPage/ProductSearchPage";
import { Role } from "../model/UserModal";

export const routes = [
  {
    path: "/",
    page: HomePage,
    isShowHeaderFooter: true,
    role: [Role['[ROLE_ADMIN]'] , Role['[ROLE_SHOP_OWNER]'] , Role['[ROLE_USER]']]
  },
  {
    path: "/sign-in",
    page: LoginPage,
    isShowHeaderFooter: false,
    role: []
  },
  {
    path: "/sign-up",
    page: RegisterPage,
    isShowHeaderFooter: false,
    role: []
  },
  {
    path: "/forgot-password",
    page: ResetPasswordPage,
    isShowHeaderFooter: false,
    role: []
  },
  {
    path: "/profile-user",
    page: UserProfilePage,
    isShowHeaderFooter: true,
    role: [Role['[ROLE_USER]']]
  },
  {
    path: "/product-detail/:name",
    page: ProductDetailPage,
    isShowHeaderFooter: true,
    role: [Role['[ROLE_ADMIN]'] , Role['[ROLE_SHOP_OWNER]'] , Role['[ROLE_USER]']]
  },
  {
    path: "/product",
    page: ProductsFilterPage,
    isShowHeaderFooter: true,
    role: [Role['[ROLE_ADMIN]'] , Role['[ROLE_SHOP_OWNER]'] , Role['[ROLE_USER]']]
  },
  {
    path: "/search",
    page: ProductSearchPage,
    isShowHeaderFooter: true,
    role : [Role['[ROLE_ADMIN]'] , Role['[ROLE_SHOP_OWNER]'] , Role['[ROLE_USER]']]
  },
  {
    path: "/order",
    page: OrderPage,
    isShowHeaderFooter: true,
    role: [Role['[ROLE_USER]']]
  },
  {
    path: "/order/payment",
    page: PaymentPage,
    isShowHeaderFooter: false,
    role: [Role['[ROLE_USER]']]
  },
  {
    path: "/system",
    page: AdminPage,
    isShowHeaderFooter: true, // do trong admin có phần header
    role: [Role['[ROLE_ADMIN]'] , Role['[ROLE_SHOP_OWNER]']]
  },
  {
    path: "/payment/success",
    page: PaymentSuccessPage,
    isShowHeaderFooter: false,
    role: [Role['[ROLE_USER]']]
  },
  // {
  //     path: '/profile-user/information-user',
  //     page: InforUserComponent,
  //     isShowHeaderFooter: true
  // },
  // {
  //     path: '/profile-user/order-user',
  //     page: OrderHistoryComponent,
  //     isShowHeaderFooter: true
  // },
  {
    path: "*",
    page: NotFoundPage,
    isShowHeaderFooter: false,
    role : [Role['[ROLE_ADMIN]'] , Role['[ROLE_SHOP_OWNER]'] , Role['[ROLE_USER]']]
  },
];
