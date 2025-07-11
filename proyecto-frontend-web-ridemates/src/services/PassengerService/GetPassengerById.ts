import { axiosClient } from "../../App";

export default async function getPassengerById(passengerId: string) {
  console.log("Get Passenger By Id:");
  console.log(passengerId);

  const response = await axiosClient.get(`/passenger/${passengerId}`);

  console.log("Get Passenger By Id Response:");
  console.log(response.data);

  return response;
}
