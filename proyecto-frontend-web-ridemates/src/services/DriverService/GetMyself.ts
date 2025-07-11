import { axiosClient } from "../../App";

export default async function getMyself() {
  console.log("Get Myself Driver:");

  const response = await axiosClient.get(`/driver/me`);

  console.log("Get Myself Driver Response:");
  console.log(response.data);

  return response;
}
