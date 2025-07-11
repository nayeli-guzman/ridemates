import { loadStripe } from "@stripe/stripe-js";
import axios from "axios";
import { Helmet } from "react-helmet";
import { Outlet } from "react-router-dom";

export const stripePromise = loadStripe(
	`${import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY}`,
);

// export const baseURL = `${import.meta.env.VITE_BASE_URL}`;
export const baseURL = "http://127.0.0.1:8081";



export const axiosClient = axios.create({
  baseURL: `${baseURL}`, // backend port
  headers: {
    "Content-Type": "application/json",
  },
});

export function App() {
	return (
		<>
			<Helmet>
				<title>RideMates</title>
			</Helmet>
			<Outlet />
		</>
	);
}

export default App;
