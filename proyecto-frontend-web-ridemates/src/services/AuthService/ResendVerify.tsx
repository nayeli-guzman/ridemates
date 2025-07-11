import { axiosClient } from "../../App";

export async function verify(email: string) {
  console.log("Resend Verify Request:");
  console.log(email);

  const response = await axiosClient.post("auth/resend", null, {
    params: { email },
  });

  console.log("Verify Response:");
  console.log(response.data);

  return response;
}
