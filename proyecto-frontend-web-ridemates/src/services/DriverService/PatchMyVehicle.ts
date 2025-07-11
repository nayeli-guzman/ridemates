import { axiosClient } from "../../App";
import { VehicleRequest } from "../../dtos/Auth/VehicleRequest";

export default async function patchMyVehicle(vehicleRequest: VehicleRequest) {
  console.log("Patch Vehicle Request:");
  console.log(vehicleRequest);

  const response = await axiosClient.patch(`/driver/me/vehicle`);

  console.log("Patch Vehicle Response:");
  console.log(response.data);

  return response.data;
}
