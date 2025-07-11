import React, { useEffect, useState, useRef, useCallback } from "react";
import { Card, CardBody, Typography } from "@material-tailwind/react";
import { getAllMyBookings } from "../../services/BookingService/GetAllMyBookings";
import { BookingResponseDto } from "../../dtos/BookingResponseDto"; // Adjust the import based on your project structure

const ITEMS_PER_PAGE = 4;

const MyBookingsComponent: React.FC = () => {
  const [bookings, setBookings] = useState<BookingResponseDto[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const observer = useRef<IntersectionObserver | null>(null);

  const fetchBookings = async (page: number) => {
    /*
    try {
      setLoading(true);
      const response = await getAllMyBookings(page, ITEMS_PER_PAGE);
      setBookings((prevBookings) => [...prevBookings, ...response.data.content]);
      if (response.data.content.length < ITEMS_PER_PAGE) {
        setHasMore(false);
      }
      setLoading(false);
    } catch (error) {
      console.error("Error fetching bookings:", error);
      setLoading(false);
    }*/
  };

  useEffect(() => {
    fetchBookings(currentPage);
  }, [currentPage]);

  const lastBookingElementRef = useCallback(
    (node) => {
      if (observer.current) observer.current.disconnect();
      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasMore) {
          setCurrentPage((prevPage) => prevPage + 1);
        }
      });
      if (node) observer.current.observe(node);
    },
    [hasMore]
  );

  return (
    <div className="flex w-full h-full flex-col items-center justify-center">
      <Card className="w-full max-w-4xl flex flex-col justify-center">
        <Typography variant="h6" color="black" className="text-3xl mb-4 text-center">
          Mis Reservas
        </Typography>
        <CardBody>
          <div className="grid w-full grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            {bookings.map((booking, i) => {
              if (bookings.length === i + 1) {
                return (
                  <div key={booking.id} ref={lastBookingElementRef}>
                    <BookingCard booking={booking} />
                  </div>
                );
              } else {
                return (
                  <div key={booking.id}>
                    <BookingCard booking={booking} />
                  </div>
                );
              }
            })}
          </div>
          {loading && <p>Cargando...</p>}
        </CardBody>
      </Card>
    </div>
  );
};

interface BookingCardProps {
  booking: BookingResponseDto;
}

const BookingCard: React.FC<BookingCardProps> = ({ booking }) => {
  return (
    <Card className="w-full">
      <CardBody>
        <Typography variant="h6" color="blue-gray">
          {booking.destination}
        </Typography>
        <Typography variant="h2" color="gray">
          Fecha: {new Date(booking.date).toLocaleDateString()}
        </Typography>
        <Typography variant="h2" color="gray">
          Hora: {new Date(booking.date).toLocaleTimeString()}
        </Typography>
        <Typography variant="h2" color="gray">
          Estado: {booking.status}
        </Typography>
      </CardBody>
    </Card>
  );
};

export default MyBookingsComponent;