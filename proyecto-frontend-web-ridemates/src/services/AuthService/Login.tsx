import { axiosClient } from "../../App";
import { LoginRequest } from "../../dtos/Auth/LoginRequest";

export async function login(loginRequest: LoginRequest) {
	console.log("Login Request:");
	console.log(loginRequest);

	const response = await axiosClient.post("/auth/login", loginRequest);

	console.log("Login Response:");
	console.log(response.data);

	return response.data;
}
