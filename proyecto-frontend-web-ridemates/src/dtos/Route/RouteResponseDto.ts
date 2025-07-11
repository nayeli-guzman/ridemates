//import { GeoLocationDto } from './GeoLocationDto.ts';

export interface RouteResponseDto {
  id: any;
  origin: any;
  destination: any;
  capacity: number;
  dateTime: string; // Use string for ISO date format
  driverFullName: string;
}
