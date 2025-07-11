import { axiosClient } from "../../App";

export async function getReviewsOf(
  userId: string,
  page: number,
  limit: number,
) {
  return await axiosClient.get(`/review/${userId}?page=${page}&limit=${limit}`);
}
