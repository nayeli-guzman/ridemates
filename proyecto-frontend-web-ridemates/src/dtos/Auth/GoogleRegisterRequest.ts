import { VehicleRequest } from "./VehicleRequest";

export interface GoogleRegisterRequest {
    credential?: string;
    clientId?: string;
    isDriver: boolean;
    vehicle?: VehicleRequest;
}