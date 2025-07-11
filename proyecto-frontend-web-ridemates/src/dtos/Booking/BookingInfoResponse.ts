export interface BookingInfoResponse {
    id : number,
    driverId : number,
    origin : string,
    destination : string,
    distance : number,
    timeEstimated : number,
    duration : number,
    price : number
}