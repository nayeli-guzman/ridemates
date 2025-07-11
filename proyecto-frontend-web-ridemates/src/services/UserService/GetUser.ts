import { axiosClient } from "../../App";

export async function getUser(id: number) {
  const response = await axiosClient.get(`/user/${id}`);
  return response;
}

export async function getUserMe() {
  const response = await axiosClient.get("/user/me");
  return response;
}
