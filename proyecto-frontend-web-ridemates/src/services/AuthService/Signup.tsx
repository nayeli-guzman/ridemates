import { axiosClient } from "../../App";
import { RegisterRequest } from "../../dtos/Auth/RegisterRequest";
import { RegisterResponse } from "../../dtos/Auth/RegisterResponse";

export async function register(registerRequest: RegisterRequest) {
  console.log("Register Request:");
  console.log(registerRequest);

  const response = await axiosClient.post<RegisterRequest, RegisterResponse>(
    "/auth/signup",
    registerRequest,
  );

  console.log("Register Response:");
  console.log(response);

  return response;
}
