import { CredentialResponse } from "@react-oauth/google";
import { jwtDecode } from "jwt-decode";
import { ChangeEvent, FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../../contexts/AuthContext";
import { RegisterRequest } from "../../dtos/Auth/RegisterRequest";
import { googleSignup } from "../../services/googleService/GoogleAuth";

interface Props {
	setIsDriver: (value: boolean) => void;
	data: RegisterRequest;
	setData: (data: RegisterRequest) => void;
	credential: CredentialResponse;
	setCredential: (credential: CredentialResponse) => void;
}

interface Errors {
  firstName?: string;
  lastName?: string;
  email?: string;
  password?:string;
  phone?:string;
  gender?:string;
  birthDate?:string;
}

export default function GoogleRegisterForm(props: Props) {
  const [errors, setErrors] = useState<Errors>({});
  const navigate = useNavigate();
  const authContext = useAuthContext();

  
  const validate = (): Errors => {
    const newErrors: Errors = {};


    if (!props.data.phone.trim() || !/^\d{9}$/.test(props.data.phone)) {
      newErrors.phone = "Por favor, ingresa tu número.";
    }

    if (!props.data.gender.trim()) {
      newErrors.gender = "Por favor, selecciona un género.";
    }

    const today = new Date();
    const minDate = new Date(
      today.getFullYear() - 16,
      today.getMonth(),
      today.getDate()
    );

    if (!props.data.birthDate || new Date(props.data.birthDate) > minDate) {
      newErrors.birthDate = "Debes tener al menos 16 años.";
    }

    return newErrors;
  };

  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value, type, checked } = e.target as HTMLInputElement;

    if (name === "phone" && value.trim() === "") {
      setErrors({ ...errors, [name]: "Por favor, ingresa tu número de celular." });
    } 
    else if (name === "gender" && value.trim() === "") {
      setErrors({ ...errors, [name]: "Por favor, ingresa tu género." });
    } 
    else if (name === "birthDate" && !isValidBirthDate(value)) {
      setErrors({ ...errors, [name]: "Debes tener al menos 16 años." });
    }
    else {
      setErrors({ ...errors, [name]: "" });
    }
    console.log(errors)

    props.setData({
      ...props.data,
      [name]: type === "radio" ? checked && value === "true" : value,
    });
  };

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const validationErrors = validate();
    console.log(validationErrors)
    if (Object.keys(validationErrors).length > 0) {
      console.log("KAJNSKAN")
      setErrors(validationErrors);
      return;
    }

    try {
      if (props.data.isDriver) {
        props.setIsDriver(true);
        return;
      } else {
        const decodedCredential = jwtDecode<any>(props.credential.credential ? props.credential.credential : "");
        console.log(decodedCredential);
        props.data.email = decodedCredential.email;
        props.data.firstName = decodedCredential.given_name;
        props.data.lastName = decodedCredential.family_name;

        const formattedData = {
          ...props.data,
          birthDate: new Date(props.data.birthDate).toISOString().split("T")[0], // Java format
        };

        console.log("formatted")
        console.log(formattedData);

        await googleSignup({
          credential: props.credential.credential,
          clientId: props.credential.clientId,
          ...formattedData,
        });
        navigate(`/available-routes`);
      }
    } catch (error) {
      console.log(error);
    }
  }

  const isValidBirthDate = (date) => {
    const today = new Date();
    const minDate = new Date(
      today.getFullYear() - 16,
      today.getMonth(),
      today.getDate()
    ); // Fecha límite hace 16 años
    return new Date(date) <= minDate;
  };

  const maxBirthDate = (() => {
    const today = new Date();
    const maxDate = new Date(
      today.getFullYear() - 16,
      today.getMonth(),
      today.getDate()
    );
    return maxDate.toISOString().split("T")[0]; // Formato 'YYYY-MM-DD'
  })();

  return (
    <div className="flex items-center justify-center h-screen">
      <form className="w-full space-y-4" onSubmit={handleSubmit}>
        <div className="items-center justify-center flex mb-10">
          <a
            className={`btn btn-ghost style-main-txt text-transparent bg-clip-text font-inter font-bold `}
          >
            Únete a nosotros
          </a>
        </div>

        <div className="grid grid-cols-2 gap-4">
        <label className="input input-bordered flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all">
              <svg
                aria-hidden="true"
                focusable="false"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 512 512"
                className="h-3 w-3 opacity-70 flex-shrink-0"
              >
                <path d="M493.4 24.6l-104-24c-11.3-2.6-22.9 3.3-27.5 13.9l-48 112c-4.2 9.8-1.4 21.3 6.9 28l60.6 49.6c-36 76.7-98.9 140.5-177.2 177.2l-49.6-60.6c-6.8-8.3-18.2-11.1-28-6.9l-112 48C3.9 366.5-2 378.1.6 389.4l24 104C27.1 504.2 36.7 512 48 512c256.1 0 464-207.5 464-464 0-11.2-7.7-20.9-18.6-23.4z" />
              </svg>
              <input
                type="text"
                name="phone"
                id="phone"
                value={props.data.phone}
                onChange={handleChange}
                placeholder="Teléfono"
              />
            </label>
            {errors.phone && (<p className="text-red-500 text-sm mt-1">{errors.phone}</p>)}

            <label className="input input-bordered flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
                className="h-5 w-5 opacity-70 flex-shrink-0"
              >
                <path d="M12 2a5 5 0 0 0-5 5 5 5 0 0 0 6.865 4.79l2.41 2.41a1 1 0 1 0 1.415-1.415l-2.41-2.41A5 5 0 0 0 17 7a5 5 0 0 0-5-5ZM10 7a3 3 0 1 1 6 0 3 3 0 0 1-6 0Zm3.5 8.793 2.293 2.293A1 1 0 0 1 16 19h-3v3a1 1 0 1 1-2 0v-3H8a1 1 0 0 1-.707-1.707L9.5 15.793a1 1 0 0 1 1.415 1.414L9.914 18H14l-1.793-1.793a1 1 0 0 1 1.414-1.414Z" />
              </svg>
              <select
                name="gender"
                id="gender"
                value={props.data.gender}
                onChange={handleChange}
                className="grow bg-transparent outline-none"
              >
                <option value="" disabled>
                  Género
                </option>
                <option value="F">Femenino</option>
                <option value="M">Masculino</option>
              </select>
            </label>
            {errors.gender && (<p className="text-red-500 text-sm mt-1">{errors.gender}</p>)}
          
        </div>

        <label className="input input-bordered flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 16 16"
              fill="currentColor"
              className="h-4 w-4 opacity-70"
            >
              <path
                fillRule="evenodd"
                d="M8 0a1 1 0 0 1 1 1v1h5a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1h5V1a1 1 0 0 1 1-1z"
                clipRule="evenodd"
              />
            </svg>
            <input
              type="date"
              name="birthDate"
              id="birthDate"
              value={props.data.birthDate}
              onChange={handleChange}
              className="grow"
              placeholder="Fecha de nacimiento"
              max={maxBirthDate} 
            />
          </label>
          {errors.birthDate && (<p className="text-red-500 text-sm mt-1">{errors.birthDate}</p>)}

        <div className="grid grid-cols-2 gap-4 ">
          <label className="flex items-center justify-center gap-2 py-4 ">
            <span className="text-center">¿Eres conductor?</span>
          </label>

					<div className="grid grid-cols-2 gap-4">
						<label className="flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all">
							<input
								type="radio"
								name="isDriver"
								value="true"
								checked={props.data.isDriver === true}
								className="accent-purple-500"
								onChange={handleChange}
							/>
							Sí
						</label>
						<label className="flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all">
							<input
								type="radio"
								name="isDriver"
								value="false"
								checked={props.data.isDriver === false}
								className="accent-purple-500"
								onChange={handleChange}
							/>
							No
						</label>
					</div>
				</div>

				<div className="flex justify-center ">
					<button
						className="btn glass bg-purple-500  duration-300 ease-in-out transform hover:scale-105 transition-all"
						id="registerSubmit"
						type="submit"
					>
						Completar
					</button>
				</div>
			</form>
		</div>
	);
}
