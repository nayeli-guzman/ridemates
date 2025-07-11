import RegisterForm from "../components/AuthComponent/RegisterForm";
import { useEffect, useState } from "react";
import { RegisterRequest } from "../dtos/Auth/RegisterRequest";
import RegisterVehiclePage from "../components/AuthComponent/RegisterVehicleForm";
import GoogleRegisterForm from "../components/AuthComponent/GoogleRegisterForm";

export default function RegisterPage() {
  const [isDriver, setIsDriver] = useState(false);
  const [google, setGoogle] = useState(false);
  const [credential, setCredential] = useState({});
  const [data, setData] = useState<RegisterRequest>({
    firstName: "",
    lastName: "",
    gender: "",
    email: "",
    password: "",
    phone: "",
    birthDate: "",
    isDriver: false,

    license: "",
    vehicle: {
      plate: "",
      capacity: 0,
      soat: "",
      model: "",
    },
  });


  return (
    <div className="h-screen w-full flex">
      <div className="w-1/2 h-full main-gradient-bg"></div>

      {!google ? 
      <div className="w-1/2 h-full flex items-center justify-center">
        {isDriver ? (
          <RegisterVehiclePage data={data} setData={setData} google={google} />
        ) : (
          <RegisterForm
            setIsDriver={setIsDriver}
            data={data}
            setData={setData}
            credential={credential}
            setCredential={setCredential}
            setGoogle={setGoogle}
          />
        )}
      </div> : 
      <div className="w-1/2 h-full flex items-center justify-center">
        {isDriver ? (
          <RegisterVehiclePage data={data} setData={setData} />
        ) : (
          <GoogleRegisterForm
            setIsDriver={setIsDriver}
            data={data}
            setData={setData}
            credential={credential}
            setCredential={setCredential}
          />
        )}
      </div>
      }
    </div>
  );
}
