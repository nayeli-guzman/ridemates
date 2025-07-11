import Api from "../../api";
import { PaymentResponseDto } from "../../dtos/Payment/PaymentResponseDto";
import { Page } from "../../models/SpringPage";

export async function getMyPayments(page: number, size: number) {
  const instance = await Api.getInstance();
  const response = instance.get<any, Page<PaymentResponseDto>>({
    url: `/payments/me?page=${page}&size=${size}`,
  });
  return response;
}
