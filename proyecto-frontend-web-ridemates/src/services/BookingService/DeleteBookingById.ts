import { axiosClient } from "../../App";

export async function deleteBooking(idx: number) {
  console.log("Delete Booking Id:");
  console.log(idx);

  const response = await axiosClient.delete(`/booking/${idx}`, {
    params: { id: idx },
  });

  console.log("Delete Booking Response:");
  console.log(response);

  return response;
}
