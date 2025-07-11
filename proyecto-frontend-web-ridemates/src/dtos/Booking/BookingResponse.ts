export interface BookingResponse {
    id: number;
    driver: string;
    passenger: string;
    dateTime: string; // Use string for ISO date-time format
    status: string;
}