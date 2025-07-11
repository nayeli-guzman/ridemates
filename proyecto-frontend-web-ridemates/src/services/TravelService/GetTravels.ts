import { axiosClient } from "../../App";

export async function getTravels(userId: number, page: number, limit: number) {
  return await axiosClient.get(
    `/travels/${userId}?page=${page}&limit=${limit}`,
  );
}

export async function getMyTravels(page: number, limit: number) {
  return await axiosClient.get(`/travel/driver/me?page=${page}&limit=${limit}`);
}
