import { CredentialResponse } from "@react-oauth/google";
import { axiosClient } from "../../App";
import { GoogleRegisterRequest } from "../../dtos/Auth/GoogleRegisterRequest";


export async function googleSignin(response: any) {
    return await axiosClient.post("/google/signin", response);
}

export async function googleSignup(response: any) {
    return await axiosClient.post("/google/signup", response);
}