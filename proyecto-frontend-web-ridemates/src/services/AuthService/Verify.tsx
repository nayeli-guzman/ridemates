import { axiosClient } from "../../App";
import { VerifyRequest } from "../../dtos/Auth/VerifyRequest";

export async function verify(verifyRequest: VerifyRequest) {
  console.log("Verify Request:");
  console.log(verifyRequest);

  const response = await axiosClient.post<VerifyRequest>(
    "auth/verify",
    verifyRequest,
  );

  console.log("Verify Response:");
  console.log(response);

  return response;
}
