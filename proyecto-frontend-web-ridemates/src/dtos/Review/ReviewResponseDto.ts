import { Role } from "../../models/Role";

export interface ReviewResponseDto {
  id: number;
  comment: string;
  roleType: Role;
  score: number;
  travelId: number;
  dirigidoA: number; // Campo dirigidoA
}
