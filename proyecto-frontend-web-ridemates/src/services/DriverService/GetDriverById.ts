import { axiosClient } from "../../App";

export default async function getDriverById(driverId: string) {
  console.log("Get Driver By Id:");
  console.log(driverId);

  const response = await axiosClient.get(`/driver/${driverId}`);

  console.log("Get Driver By Id Response:");
  console.log(response.data);

  return response;
}
