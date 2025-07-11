import { Status } from "../../models/PaymentStatus";

export interface PaymentResponseDto {
  travelId: number;
  method: string;
  amount: number;
  createdAt: string;
  paymentIntentId: string;
  status: string;
}
