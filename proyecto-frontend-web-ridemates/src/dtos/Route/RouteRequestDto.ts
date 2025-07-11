//import { GeoLocationDto } from './GeoLocationDto.ts';

export interface RouteRequestDto {
  origin: string;
  destination: string;
  capacity: number;
  dateTime: string; // Use string for ISO date format
}
