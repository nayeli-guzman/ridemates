enum Status {
  PENDING,
  ACCEPTED,
  CANCELLED,
}

export interface BookingResponseDto {
  id: number;
  driver: string;
  passenger: string;
  dateTime: Date;
  status: Status;
}
