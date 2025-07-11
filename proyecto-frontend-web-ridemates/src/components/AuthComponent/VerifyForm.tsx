import { Card, CardBody, Typography } from "@material-tailwind/react";
import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { VerifyRequest } from "../../dtos/Auth/VerifyRequest";
import { verify } from "../../services/AuthService/Verify";

export default function VerifyForm() {
	const navigate = useNavigate();
	const location = useLocation();
	const [error, setError] = useState<string | null>(null);

	const queryParams = new URLSearchParams(location.search);
	const email_ = queryParams.get("email");

	const [code, setCode] = useState(["", "", "", "", "", ""]);
	const [verificationData, setVerificationData] = useState<VerifyRequest>({
		email: email_ ?? "",
		verificationCode: "",
	});

	if (email_ == null) {
		navigate("/main");
	}

	async function handleSubmit(e: React.FormEvent) {
		e.preventDefault();

		console.log(verificationData.verificationCode);

		if (verificationData.verificationCode === "") {
			setError(() => "El código de verificación no puede ser nulo");
			return;
		} else {
			try {
				// setError(null);
				const response = await verify(verificationData);

				if (response.status === 200) {
					setError(null);
					navigate("/auth/login");
				} else {
					setError("Código de verificación incorrecto");
					return;
				}
			} catch (error) {
				setError("Código de verificación incorrecto");
			}
		}
	}

	const handleChange = (
		e: React.ChangeEvent<HTMLInputElement>,
		index: number,
	) => {
		const value = e.target.value;
		if (/[^0-9]/.test(value)) return;

		const newCode = [...code];
		newCode[index] = value;
		setCode(() => newCode);

		if (value && index < code.length - 1) {
			const nextInput = document.getElementById(`input-${index + 1}`);
			if (nextInput) nextInput.focus();
		}
		const verificationCode_ = newCode.join(""); // Unir el array de códigos en una sola cadena

		setVerificationData((prev) => ({
			...prev,
			verificationCode: verificationCode_,
		}));
	};

	const handleBackspace = (
		e: React.KeyboardEvent<HTMLInputElement>,
		index: number,
	) => {
		if (e.key === "Backspace" && !code[index]) {
			const prevInput = document.getElementById(`input-${index - 1}`);
			if (prevInput) prevInput.focus();
		}
	};

	return (
		<div className="flex items-center justify-center h-screen main-gradient-bg w-full">
			<Card className=" w-[60%] h-[80%] flex flex-col items-center justify-center">
				<CardBody className="w-full flex flex-col items-center justify-center">
					<a
						className={`btn btn-ghost text-9xl style-main-txt text-transparent bg-clip-text font-inter font-bold `}
					>
						Verificate
					</a>
					<form onSubmit={handleSubmit}>
						<Typography
							className={`bg-white rounded-lg p-4 flex justify-center items-center text-gray-600 text-center text-sm font-medium max-w-96 mx-auto ${error ? "text-red-500" : ""}`}
						>
							{error
								? error
								: `Se ha enviado un código de verificación a ${email_}`}
						</Typography>
						<div className="verification-code flex gap-2 justify-center mt-4">
							{code.map((digit, index) => (
								<input
									key={index}
									id={`input-${index}`}
									type="text"
									value={digit}
									onChange={(e) => handleChange(e, index)}
									onKeyDown={(e) => handleBackspace(e, index)}
									maxLength={1}
									className="input input-bordered max-w-xs text-center w-16 h-14 text-black"
									autoFocus={index === 0} // Foco en el primer campo por defecto
								/>
							))}
						</div>
						<div className="flex justify-center mt-10">
							<button
								className="btn glass text-white bg-purple-500 duration-300 ease-in-out transform hover:scale-105 transition-all"
								id="registerSubmit"
								type="submit"
							>
								Verificar código
							</button>
						</div>
					</form>
				</CardBody>
			</Card>
		</div>
	);
}
