import { createBrowserRouter, Navigate } from "react-router-dom";
import App from "../App";
import BookingComponent from "../components/BookingComponent/BookingComponent";
import Logout from "../components/LogoutComponent/Logout";
import CheckoutForm from "../components/PaymentComponent/PayComponent";
import PaymentComponent from "../components/PaymentComponent/PaymentComponent";
import UserEditComponent from "../components/ProfileComponent/UserEditComponent";
import UserInfoComponent from "../components/ProfileComponent/UserInfoComponent";
import AddRoute from "../components/RoutesComponent/AddRoute";
import RouteComponent from "../components/RoutesComponent/RouteComponent";
import LiveChatComponent from "../components/SocketComponent/LiveChatComponent";
import MyTravelsComponent from "../components/TravelComponent/MyTravelsComponent";
import LoginPage from "../pages/LoginPage";
import MainPage from "../pages/MainPage";
import RegisterPage from "../pages/RegisterPage";
import UserPage from "../pages/UserPage";
import VerifyPage from "../pages/VerifyPage";
import ProtectedRoute from "./ProtectedRoute";

import MyBookingsComponent from "../components/BookingComponent/MyBookingsComponent";

export const router = createBrowserRouter([
	{
		path: "/",
		element: <App />,
		children: [
			{
				path: "/",
				element: <Navigate to="/landing" />,
			},
			{
				path: "landing",
				element: <MainPage />,
			},
			{
				path: "auth",
				children: [
					{
						path: "login",
						element: <LoginPage />,
					},
					{
						path: "register",
						element: <RegisterPage />,
					},
					{
						path: "verify",
						element: <VerifyPage />,
					},
				],
			},
			{
				path: "/",
				element: <ProtectedRoute />,
				children: [
					{
						path: "live-chat",
						element: <LiveChatComponent />,
					},
					{
						path: "available-routes",
						element: <UserPage children={<RouteComponent />} />,
					},
					{
						path: "booking",
						element: <UserPage children={<BookingComponent />} />,
					},
					{
						path: "payments",
						element: <UserPage children={<PaymentComponent />} />,
					},
					{
						path: "my-travels",
						element: <UserPage children={<MyTravelsComponent />} />,
					},
					{
						path: "my-bookings",
						element: <UserPage children={<MyBookingsComponent />} />,
					},
					{
						path: "my-info",
						element: (
							<UserPage
								children={<UserInfoComponent self={true} search={false} />}
							/>
						),
					},
					{
						path: "edit-info",
						element: <UserPage children={<UserEditComponent />} />,
					},
					{
						path: "pay",
						element: <UserPage children={<CheckoutForm />} />,
					},
					{
						path: "add-route",
						element: <UserPage children={<AddRoute />} />,
					},
				],
			},
			{
				path: "*",
			},
			{
				path: "logout",
				element: <Logout />,
			},
		],
	},
]);
