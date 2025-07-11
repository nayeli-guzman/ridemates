import { axiosClient } from "../../App";
import { ReviewRequestDto } from "../../dtos/Review/ReviewRequestDto";

export async function postReview(review: ReviewRequestDto) {
  console.log("Post Review Request:");
  console.log(review);

  const response = await axiosClient.post("/review", review);

  console.log("Post Review Response:");
  console.log(response.data);

  return response.data;
}
