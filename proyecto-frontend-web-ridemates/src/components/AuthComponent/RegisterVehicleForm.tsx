import { ChangeEvent, FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../../contexts/AuthContext";
import { RegisterRequest } from "../../dtos/Auth/RegisterRequest";
import { VehicleRequest } from "../../dtos/Auth/VehicleRequest";

interface RegisterVehicleProps {
	data: RegisterRequest;
	google: boolean;
	setData: (data: RegisterRequest) => void;
}

interface Errors {
	license?: string;
	model?: string;
	plate?: string;
	soat?: string;
	capacity?: string;
}

export default function RegisterVehiclePage(props: RegisterVehicleProps) {
	const navigate = useNavigate();
	const [errors, setErrors] = useState<Errors>({});
	const [vehicleData, setVehicleData] = useState<VehicleRequest>({
		model: "",
		plate: "",
		soat: "",
		capacity: 0,
	});

	const authContext = useAuthContext();

	const validate = (): Errors => {
		const newErrors: Errors = {};

		if (!props.data.license.trim()) {
			newErrors.model = "La licencia es obligatoria.";
		}

		if (!props.data.vehicle.model.trim()) {
			newErrors.model = "El modelo es obligatorio.";
		}

		if (!props.data.vehicle.plate.trim()) {
			newErrors.plate = "La placa es obligatoria.";
		}

		if (
			!props.data.vehicle.soat.trim() ||
			!/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(props.data.email)
		) {
			newErrors.soat = "El soat es obligatorio.";
		}

		if (!props.data.vehicle.capacity.toString().trim()) {
			newErrors.capacity = "La capacidad es obligatoria.";
		}

		return newErrors;
	};

	function handleChange(e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
		const { name, value } = e.target;
		if (name === "soat" && value.trim() === "") {
			setErrors({ ...errors, [name]: "Por favor, ingresa tu soat." });
		} else if (name === "license" && value.trim() === "") {
			setErrors({ ...errors, [name]: "Por favor, ingresa tu licencia." });
		} else if (name === "model" && value.trim() === "") {
			setErrors({
				...errors,
				[name]: "Por favor, ingresa el modelo de tu vehiculo.",
			});
		} else if (name === "plate" && value.trim() === "") {
			setErrors({ ...errors, [name]: "Por favor, ingresa tu placa." });
		} else if (name === "capacity" && value.trim() === "") {
			setErrors({
				...errors,
				[name]: "Por favor, ingresa una capacidad adecuada.",
			});
		} else {
			setErrors({ ...errors, [name]: "" });
		}
		if (name in vehicleData) {
			setVehicleData((prevData) => ({
				...prevData,
				[name]: value,
			}));
			props.setData({
				...props.data,
				vehicle: {
					...props.data.vehicle,
					[name]: value,
				},
			});
		}
	}

	async function handleSubmit(e: FormEvent<HTMLFormElement>) {
		e.preventDefault();
		const validationErrors = validate();
		if (Object.keys(validationErrors).length > 0) {
			console.log(111111111111);
			console.log(validationErrors);
			setErrors(validationErrors);
			return;
		}

		try {
			console.log(props.data);

			await authContext.register(props.data);
			console.log(props.data);
			navigate(`/auth/verify?email=${props.data.email}`);
		} catch (error) {
			console.log(error);
		}
	}

	const handleChangeDriver = (e: ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;

		if (name === "license" && value.trim() === "") {
			setErrors({ ...errors, [name]: "Por favor, ingresa tu licencia." });
		} else {
			setErrors({ ...errors, [name]: "" });
		}

		props.setData({
			...props.data,
			[name]: value,
		});
	};

	return (
		<div className="flex items-center justify-center h-screen">
			<form className="w-full space-y-4" onSubmit={handleSubmit}>
				<div className="items-center justify-center flex mb-10">
					<a
						className={`btn btn-ghost style-navbar-link text-transparent bg-clip-text font-inter font-bold hover:text-black`}
					>
						Registra tu vehiculo
					</a>
				</div>

				<div className="flex flex-col">
					<label
						className="input input-bordered flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all"
						id="license"
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
							type="text"
							name="license"
							id="license"
							value={props.data.license}
							onChange={handleChangeDriver}
							className="grow"
							placeholder="Licencia"
						/>
					</label>
					{errors.license && <p className="text-red-500">{errors.license}</p>}
				</div>

				<div className="flex flex-col">
					<label
						className="input input-bordered flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all"
						id="soat"
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
							type="text"
							name="soat"
							id="soat"
							value={vehicleData.soat}
							onChange={handleChange}
							className="grow"
							placeholder="Soat"
						/>
					</label>
					{errors.soat && <p className="text-red-500">{errors.soat}</p>}
				</div>

				<div className="flex flex-col">
					<label className="input input-bordered flex items-center gap-2  duration-300 ease-in-out transform hover:scale-105 transition-all">
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
							type="text"
							className="grow"
							name="model"
							id="model"
							value={vehicleData.model}
							onChange={handleChange}
							placeholder="Modelo"
						/>
					</label>
					{errors.model && <p className="text-red-500">{errors.model}</p>}
				</div>

				<div className="grid grid-cols-2 gap-4">
					<div className="flex flex-col">
						<label className="input input-bordered flex items-center gap-2 duration-300 ease-in-out transform hover:scale-105 transition-all">
							<svg
								xmlns="http://www.w3.org/2000/svg"
								viewBox="0 0 16 16"
								fill="currentColor"
								className="h-4 w-4 opacity-70 flex-shrink-0"
							>
								<path
									fillRule="evenodd"
									d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
									clipRule="evenodd"
								/>
							</svg>
							<input
								type="text"
								name="plate"
								id="plate"
								value={vehicleData.plate}
								onChange={handleChange}
								placeholder="Placa"
							/>
						</label>
						{errors.plate && <p className="text-red-500">{errors.plate}</p>}
					</div>

					<div className="flex flex-col">
						<label className="input input-bordered flex items-center gap-2 duration-300 ease-in-out transform hover:scale-105 transition-all">
							<input
								type="number"
								name="capacity"
								id="capacity"
								value={vehicleData.capacity}
								onChange={handleChange}
								placeholder="Capacidad Vehiculo"
								min="1"
								max="10"
								step="1"
								className="grow bg-transparent outline-none px-4 py-2 h-full flex-shrink-0"
							/>
						</label>
						{errors.capacity && (
							<p className="text-red-500">{errors.capacity}</p>
						)}
					</div>
				</div>

				<div className="flex justify-center">
					<button
						className="btn glass bg-purple-500 shadow-md duration-300 ease-in-out transform hover:scale-105 transition-all"
						id="registerSubmit"
						type="submit"
					>
						Continuar
					</button>
				</div>
			</form>
		</div>
	);
}
