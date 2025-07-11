import { axiosClient } from "../../App";

export default async function postPayment(
  travelId: string,
  paymentRequest: PaymentRequest,
) {
  console.log("Post Payment:");
  console.log(travelId);
  console.log(paymentRequest);

  const response = await axiosClient.post(
    `/payment/${travelId}`,
    paymentRequest,
    { params: { id: travelId } },
  );

  console.log("Post Payment response:");
  console.log(response.data);

  return response;
}
