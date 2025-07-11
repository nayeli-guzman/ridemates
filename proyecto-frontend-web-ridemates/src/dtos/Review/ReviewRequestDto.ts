export interface ReviewRequestDto {
  comment: string;
  roleType: string; // de quien viene rol
  score: number;
  travelId: number;
  passengerId: number; // Solo para el conductor que califica al pasajero
  dirigidoA: number; // Campo dirigidoA
}
