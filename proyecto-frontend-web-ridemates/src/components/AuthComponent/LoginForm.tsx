import { ChangeEvent, FormEvent, useState } from "react";
import { LoginRequest } from "../../dtos/Auth/LoginRequest";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../../contexts/AuthContext";
import { CredentialResponse, GoogleLogin } from '@react-oauth/google';

interface Props {
  data: LoginRequest;
  setData: (data: LoginRequest) => void;
}

interface Errors {
  email?: string;
  password?:string;
}

export default function LoginForm(props: Props) {
  const navigate = useNavigate();
  const authContext = useAuthContext();

  const [errors, setErrors] = useState<Errors>({});

  const validate = (): Errors => {
    const newErrors: Errors = {};

    // if (!props.data.email.trim() || !/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(props.data.email)) {
    //   newErrors.email = "Por favor, ingresa un email válido.";
    // }

    if (!props.data.password.trim()) {
      newErrors.password = "La contraseña debe tener al menos 8 caracteres.";
    }
    return newErrors;
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    
    if (name === "email" && value.trim() === "") {
      setErrors({ ...errors, [name]: "Por favor, ingresa tu correo." });
    } 
    else if (name === "password" && value.trim() === "") {
      setErrors({ ...errors, [name]: "Por favor, ingresa tu contraseña." });
    } 
    else {
      setErrors({ ...errors, [name]: "" });
    }

    props.setData({
      ...props.data,
      [name]: value,
    });
  };

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      console.log("KAJNSKAN")
      setErrors(validationErrors);
      return;
    }
    try {
      await authContext.login(props.data);
      navigate("/available-routes");
    } catch (error) {
      console.log(error);
    }
  }

  async function handleGoogleLogin(response: CredentialResponse) {
    try {
      await authContext.googleLogin(response);
      navigate("/available-routes");
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <div className="flex items-center justify-center h-screen  w-1/2">
      <form className="w-full space-y-4" onSubmit={handleSubmit}>
        <div className="items-center justify-center flex mb-10">
          <a
            className={`btn btn-ghost style-main-txt text-transparent bg-clip-text font-inter font-bold  `}
          >
            ¡Qué bueno verte de nuevo!
          </a>
        </div>

        <div className="flex flex-col">
          <label
            className="input input-bordered flex items-center gap-2 duration-300 ease-in-out transform hover:scale-105 transition-all"
            id="email"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 16 16"
              fill="currentColor"
              className="h-4 w-4 opacity-70"
            >
              <path d="M2.5 3A1.5 1.5 0 0 0 1 4.5v.793c.026.009.051.02.076.032L7.674 8.51c.206.1.446.1.652 0l6.598-3.185A.755.755 0 0 1 15 5.293V4.5A1.5 1.5 0 0 0 13.5 3h-11Z" />
              <path d="M15 6.954 8.978 9.86a2.25 2.25 0 0 1-1.956 0L1 6.954V11.5A1.5 1.5 0 0 0 2.5 13h11a1.5 1.5 0 0 0 1.5-1.5V6.954Z" />
            </svg>
            <input
              type="email"
              name="email"
              id="email"
              value={props.data.email}
              onChange={handleChange}
              className="grow"
              placeholder="Email"
            />
          </label>
          {errors.email && (<p className="text-red-500 text-sm mt-1">{errors.email}</p>)}

        </div>
        <div className="flex flex-col">
          <label className="input input-bordered flex items-center gap-2 duration-300 ease-in-out transform hover:scale-105 transition-all">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 16 16"
              fill="currentColor"
              className="h-4 w-4 opacity-70"
            >
              <path
                fillRule="evenodd"
                d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
                clipRule="evenodd"
              />
            </svg>
            <input
              type="password"
              className="grow"
              name="password"
              id="password"
              value={props.data.password}
              onChange={handleChange}
              placeholder="Contraseña"
            />
          </label>
          {errors.password && (<p className="text-red-500 text-sm mt-1">{errors.password}</p>)}
        </div>

        <div className="flex justify-center">
          <button
            className="btn glass bg-purple-500 duration-300 ease-in-out transform hover:scale-105 transition-all"
            id="registerSubmit"
            type="submit"
          >
            Continuar
          </button>
        </div>

        <div className="flex justify-center">
          <a
            href="/auth/register"
            className="text-center text-sm text-blue-500 hover:text-blue-700"
          >
            Aún no soy parte de Ridemates
          </a>
        </div>
        <GoogleLogin
          text="signin_with"
          onSuccess={credentialResponse => {
            handleGoogleLogin(credentialResponse);
          }}
          onError={() => {
            console.log('Login Failed');
          }}
      />
      </form>
    </div>
  );
}
