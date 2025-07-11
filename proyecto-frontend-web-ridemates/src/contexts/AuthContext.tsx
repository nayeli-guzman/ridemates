import { CredentialResponse } from "@react-oauth/google";
import { createContext, ReactNode, useContext } from "react";
import { axiosClient } from "../App";
import { LoginRequest } from "../dtos/Auth/LoginRequest";
import { RegisterRequest } from "../dtos/Auth/RegisterRequest";
import { useStorageState } from "../hooks/useStorageState";
import { login } from "../services/AuthService/Login";
import { register } from "../services/AuthService/Signup";
import { googleSignin } from "../services/googleService/GoogleAuth";

interface AuthContextType {
	register: (request: RegisterRequest) => Promise<void>;
	login: (request: LoginRequest) => Promise<void>;
	googleLogin: (request: CredentialResponse) => Promise<void>;
	googleRegister: (request: CredentialResponse) => Promise<void>;
	logout: () => void;
	session?: string | null;
	isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

async function loginHandler(
	loginRequest: LoginRequest,
	setSession: (value: string | null) => void,
) {
	setSession(null);
	const response = await login(loginRequest);
	setSession(response.token);
}

async function googleLoginHandler(
	loginResponse: CredentialResponse,
	setSession: (value: string | null) => void,
) {
	setSession(null);
	const request = {
		accessToken: loginResponse.credential,
	};
	const response = await googleSignin(request);
}

async function googleRegisterHandler(
	loginResponse: CredentialResponse,
	setSession: (value: string | null) => void,
) {
	setSession(loginResponse.credential ? loginResponse.credential : null);
}

async function signupHandler(
	signupRequest: RegisterRequest,
	setSession: (value: string | null) => void,
) {
	setSession(null);
	await register(signupRequest);
}

export function AuthProvider(props: { children: ReactNode }) {
	const [[isLoading, session], setSession] = useStorageState("session");
	if (session) {
		axiosClient.interceptors.request.use(
			(config) => {
				config.headers.Authorization = `Bearer ${session}`;
				return config;
			},
			(error) => Promise.reject(error),
		);

	}

	return (
		<AuthContext.Provider
			value={{
				register: (signupRequest) => signupHandler(signupRequest, setSession),
				login: (loginRequest) => loginHandler(loginRequest, setSession),
				googleLogin: (loginRequest) =>
					googleLoginHandler(loginRequest, setSession),
				googleRegister: (registerRequest) =>
					googleRegisterHandler(registerRequest, setSession),
				logout: () => {
					setSession(null);
				},
				session,
				isLoading,
			}}
		>
			{props.children}
		</AuthContext.Provider>
	);
}

export function useAuthContext() {
	const context = useContext(AuthContext);
	if (context === undefined) {
		throw new Error("useAuthContext must be used within an AuthProvider");
	}
	return context;
}
