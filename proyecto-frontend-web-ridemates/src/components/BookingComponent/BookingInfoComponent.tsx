import { useState } from "react";
import { BookingInfoResponse } from "../../dtos/Booking/BookingInfoResponse";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import PayComponent from "../PaymentComponent/PayComponent";
import getBookingInfo from "../../services/BookingService/getBookingInfo";

interface BookingProps {
    bookingInfo : BookingInfoResponse
    route : RouteResponseDto
    placeName : string
}

export default function BookingInfoComponent({props} : {props : BookingProps}) {
    
    const [showPayComponent, setShowPayComponent] = useState(false);
    const handleSubmit = async () => {
        
        const response = await getBookingInfo(props.route.id, request);
        setBookingInfo(response.data);
        setShowPayComponent(true)
    //    postBooking({ routeId: routeId, passenger: asiento, price: precio });
      };
    
      const handleClosePayComponent = () => {
        setShowPayComponent(false);
      };
    return(
    <>
    <div className="w-full mt-6 bg-gray-100 p-4 rounded-lg shadow-md mb-10">
      <h3 className="text-lg font-bold text-gray-700 mb-2 text-center">Resumen del Viaje</h3>
      <ul className="text-gray-600 text-sm space-y-2">
        <li><strong>Origen:</strong> {props.route.origin}</li>
        <li><strong>Destino:</strong> {props.placeName}</li>
        <li><strong>Conductor:</strong> {props.route.driverFullName}</li>
        <li><strong>Duración:</strong> {}</li>
        <li><strong>Precio estimado:</strong> 5 soles</li>
      </ul>
    </div>
    <div className="w-full items-center justify-center flex flex-row space-x-10 ">
        <button
            className=" btn glass w-[40%] bg-purple-600 text-white "
            onClick={(e) => {
                e.preventDefault();  
                handleSubmit();
            }}
            >
            Ir a pagar
            </button>
        </div> 

        {showPayComponent && (
        <div className="fixed inset-0 z-50 bg-black bg-opacity-50 flex items-center justify-center">
            
            <PayComponent setShowPayComponent={setShowPayComponent} route={props.route}/>
        </div>
      )}
    </>
)
}