import { axiosClient } from "../../App";
import { BookingInfo } from "../../dtos/Booking/BookingInfo";

export default async function getBookingInfo(routeId : string, bookingInfo : BookingInfo) {
  return await axiosClient.post(`/booking/${routeId}/intent`, bookingInfo);
}
