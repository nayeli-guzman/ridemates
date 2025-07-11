import { axiosClient } from "../../App";
import { BookingRequest } from "../../dtos/Booking/BookingRequest";

export async function postBooking(bookingRequest: BookingRequest) {
  console.log("Post Booking Request:");
  console.log(bookingRequest);

  const response = await axiosClient.post(`/booking/book`, bookingRequest);

  console.log("Post Booking Response:");
  console.log(response.data);

  return response;
}
