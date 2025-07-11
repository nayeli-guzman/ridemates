import { VehicleRequest } from "./VehicleRequest";

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  gender: string;
  email: string;
  password: string;
  phone: string;
  birthDate: string;
  isDriver: boolean;

  // driver atributes

  license: string;
  vehicle: VehicleRequest;
}
