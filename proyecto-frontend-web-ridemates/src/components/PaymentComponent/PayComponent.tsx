import React, { useEffect, useState } from "react";
import {
  Card,
  CardHeader,
  CardBody,
  Input,
  Button,
  Typography,
  Tabs,
  TabsHeader,
  TabsBody,
  Tab,
  TabPanel,
  Select,
  Option,
} from "@material-tailwind/react";
import {
  BanknotesIcon,
  CreditCardIcon,
  LockClosedIcon,
} from "@heroicons/react/24/solid";
import { PaymentElement, useElements, useStripe } from "@stripe/react-stripe-js";
import { Elements } from "@stripe/react-stripe-js";
import { axiosClient, stripePromise } from "../../App";
import { set } from "date-fns";
import { StripeRequest } from "../../dtos/StripeRequest";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import { useNavigate } from "react-router-dom";
 
function formatCardNumber(value: string) {
  const val = value.replace(/\s+/g, "").replace(/[^0-9]/gi, "");
  const matches = val.match(/\d{4,16}/g);
  const match = (matches && matches[0]) || "";
  const parts = [];
 
  for (let i = 0, len = match.length; i < len; i += 4) {
    parts.push(match.substring(i, i + 4));
  }
 
  if (parts.length) {
    return parts.join(" ");
  } else {
    return value;
  }
}
 
function formatExpires(value: string) {
  return value
    .replace(/[^0-9]/g, "")
    .replace(/^([2-9])$/g, "0$1")
    .replace(/^(1{1})([3-9]{1})$/g, "0$1/$2")
    .replace(/^0{1,}/g, "0")
    .replace(/^([0-1]{1}[0-9]{1})([0-9]{1,2}).*/g, "$1/$2");
}
 
function CheckoutForm() {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();

  const handleSubmit = async (event: any) => {
    event.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    const result = await stripe.confirmPayment({
      elements,
      confirmParams: {
        return_url: `http://${import.meta.env.VITE_BASE_URL}/home`,
      },
      redirect: "if_required"
    });

    if (result.error) {
      console.log(result.error.message);
    } else {
      console.log('Payment successful');
    }
    navigate("/available-routes");
  };


  return (
    <form onSubmit={handleSubmit} className=" flex flex-col w-full items-center justify-between ">
      <PaymentElement id="stripe-id" className="w-full"/>
      <button
        className=" btn glass w-[40%] bg-purple-600 text-white mt-6"
        onClick={(e) => {
          e.preventDefault();  
          //handleSubmit();
        }}
      >
      Pagar
      </button>
      <Typography
        variant="small"
        color="gray"
        className="mt-5 flex items-center justify-center gap-2 font-medium opacity-60"
      >
        <LockClosedIcon className="-mt-0.5 h-4 w-4" /> Los pagos son seguros y encriptados
      </Typography>
    </form>
  );
};

interface PayComponentProps {
  setShowPayComponent: (show: boolean) => void;
  route: RouteResponseDto;
}

export default function PayComponent({ setShowPayComponent, route }: PayComponentProps) {
  const [clientSecret, setClientSecret] = useState('');

  const handleClosePayComponent = () => {
    setShowPayComponent(false);
  };

  console.log(route)
  
  const createPaymentIntent = async () => {
    console.log("waiting")
    const request = { email: "salvador.donayre@utec.edu.pe", amount: 1000 };
    const response = await axiosClient.post("/stripe/payment-sheet", request);
    console.log(response.data);
    setClientSecret(response.data.clientSecret);
  };

  useEffect(() => {
    createPaymentIntent();
  }, []);

  const options = {
    clientSecret,
  };

  if (!clientSecret) {
    return (
      <div className="flex items-center justify-center bg-gray-100">
        <Card className="w-full max-w-[32rem] mx-auto ">
          <CardBody>
            <Typography variant="h6" color="blue-gray" className="mt-5 text-2xl">
              Cargando...
            </Typography>
          </CardBody>
        </Card>
      </div>
    );
  }

  return (
    <Elements stripe={stripePromise} options={options}>
      <div className="flex items-center justify-center rounded-3xl bg-white">
        <Card className="w-full m-6 rounded-sm">
          <button
              className="absolute top-2 right-2 text-gray-600 hover:text-gray-800"
              onClick={handleClosePayComponent}
            >
              ✕
            </button>
          <CardBody>
            <Typography variant="h6" color="blue-gray" className="mt-5 text-2xl text-center">
              Pasarela de Pago
            </Typography>
            <CheckoutForm />
          </CardBody>
        </Card>
      </div>
    </Elements>
  );
};