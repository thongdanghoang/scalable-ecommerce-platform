import HomePage from "../pages/HomePage/HomePage";
import LoginPage from "../pages/LoginPage/LoginPage";
import NotFoundPage from "../pages/NotFoundPage/NotFoundPage";
import ProductDetailPage from "../pages/ProductDetailPage/ProductDetailPage";
import RegisterPage from "../pages/RegisterPage/RegisterPage";
import ResetPasswordPage from "../pages/ResetPasswordPage/ResetPasswordPage";
import UserProfilePage from "../pages/UserProfilePage/UserProfilePage";
<<<<<<< HEAD

export const routes = [
  {
    path: "/",
    page: HomePage,
    isShowHeader: true,
  },
  {
    path: "/sign-in",
    page: LoginPage,
    isShowHeader: false,
  },
  {
    path: "/sign-up",
    page: RegisterPage,
    isShowHeader: false,
  },
  {
    path: "/reset-password",
    page: ResetPasswordPage,
    isShowHeader: false,
  },
  {
    path: "/profile-user",
    page: UserProfilePage,
    isShowHeader: true,
  },
  {
    path: "/product-detail",
    page: ProductDetailPage,
    isShowHeader: true,
  },
  // {
  //     path: '/profile-user/information-user',
  //     page: InforUserComponent,
  //     isShowHeader: true
  // },
  // {
  //     path: '/profile-user/order-user',
  //     page: OrderHistoryComponent,
  //     isShowHeader: true
  // },
  {
    path: "*",
    page: NotFoundPage,
  },
];
=======
import ProductPage from "../pages/ProductPage/ProductPage";

export const routes = [
    {
        path: '/',
        page: HomePage,
        isShowHeader: true
    },
    {
        path: '/sign-in',
        page: LoginPage,
        isShowHeader: false
    },
    {
        path: '/sign-up',
        page: RegisterPage,
        isShowHeader: false
    },
    {
        path: '/reset-password',
        page: ResetPasswordPage,
        isShowHeader: false,
    },
    {
        path: '/profile-user',
        page: UserProfilePage,
        isShowHeader: true
    },
    {
        path: '/product',
        page: ProductPage,
        isShowHeader: true
    },
    // {
    //     path: '/profile-user/information-user',
    //     page: InforUserComponent,
    //     isShowHeader: true
    // },
    // {
    //     path: '/profile-user/order-user',
    //     page: OrderHistoryComponent,
    //     isShowHeader: true
    // },
    {
        path: '*',
        page: NotFoundPage
    }
]
>>>>>>> e0b896090426454e2e4b968033da2311ebffd49e
