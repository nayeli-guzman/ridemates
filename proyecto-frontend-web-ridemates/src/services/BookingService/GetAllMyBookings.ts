import { axiosClient } from "../../App";

export async function getAllMyBookings(page: number) {
  console.log("Get All My Bookings Request:");
  console.log(page);

  const response = await axiosClient.get(`/route/all?page=${page}&size=10`);

  console.log("Get All My Bookings Response:");
  console.log(response.data);

  return response.data;
}
