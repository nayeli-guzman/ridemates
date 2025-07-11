import { Avatar, Card, CardBody, Timeline, TimelineConnector, TimelineHeader, TimelineIcon, TimelineItem, Typography } from "@material-tailwind/react";
import proof from "../../assets/proof.jpg";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import { ClockIcon, FlagIcon, MapPinIcon, TagIcon } from "@heroicons/react/24/outline";
import { useEffect, useState } from "react";
import getBookingInfo from "../../services/BookingService/getBookingInfo";
import { BookingInfo } from "../../dtos/Booking/BookingInfo";
import BookingInfoComponent from "./BookingInfoComponent";
import { BookingInfoResponse } from "../../dtos/Booking/BookingInfoResponse";

interface BookingProps {
    routeId : string;
    route : RouteResponseDto;
    setNextPage : (confirmation:boolean) => void
    stopPlace : google.maps.LatLng | null
}
export default function BookingComponentFormAft({props} : {props : BookingProps}) {

    const [placeName, setPlaceName] = useState("Cargando...");
    const [bookingInfo, setBookingInfo] = useState<BookingInfoResponse>({
        distance : 0,
        duration : 0,
        price : 0
    });

    const [isMore, setIsMore] = useState(false);

    
    useEffect(() => {
        async function fetchPlaceName() {
            if (!props.stopPlace) {
                console.log("No location provided");
                setPlaceName("Cargando...");
            }
            
            const geocoder = new google.maps.Geocoder();
            
            geocoder.geocode({ location: props.stopPlace }, (results, status) => {
                if (results && status === google.maps.GeocoderStatus.OK && results[0]) {
                  console.log("Place name:", results[0].formatted_address);
                  setPlaceName(results[0].formatted_address);
                } else {
                  console.warn("Geocoding failed: " + status);
                  setPlaceName("Cargando...");
      
                }
              });
        }
    
        fetchPlaceName();
      }, [props.stopPlace]); 

      
  const handleSubmit = async () => {
    const request : BookingInfo = {
      origin : props.route.origin,
      destination : placeName
    }
    
    //const response = await getBookingInfo(props.routeId, request);
    //setBookingInfo(response.data);
    setIsMore(true)
//    postBooking({ routeId: routeId, passenger: asiento, price: precio });
  };
    
    return (
        <>
      <div className="flex items-center justify-center w-full h-full">
      <Card key={props.routeId} className="w-[75%] max-w-2xl mx-auto">
        <CardBody className="flex flex-col items-center">
            <a className={`mb-4 btn btn-ghost style-navbar-link text-transparent bg-clip-text font-inter font-bold`}>
                Haz tu reserva
            </a>
            <Typography className="bg-white rounded-lg p-1 flex justify-between items-center  text-gray-600 text-center text-sm font-medium max-w-96 mx-auto">
                {`Selecciona tu punto de bajada dentro de la ruta`}
            </Typography>
              <div className="flex items-center mb-4">
                {/*
                <Avatar src={proof} size="sm" className="rounded-full mr-4" />
                <div>
                  <Typography variant="h5" color="blue-gray">
                    {props.route.driverFullName}
                  </Typography>
                </div>
                */}

              </div>

              <div className="w-full flex flex-row  items-center justify-between mb-10 ">
                <div>
                    <label className="text-gray-400 font-semibold mb-4">Origen</label>
                    <input
                            type="text"
                            value={props.route.origin}
                            readOnly
                            className=" w-[90%] p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600 duration-300 ease-in-out transform hover:scale-105 transition-all"
                        />
                </div>
                <div>
                    <label className="text-gray-400 font-semibold mb-14">Destino</label>
                    <input
                            type="text"
                            value={props.stopPlace ? placeName : "Elige tu lugar de bajada..."}
                            readOnly
                            className=" w-[90%] p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600 duration-300 ease-in-out transform hover:scale-105 transition-all"
                        />
                </div>
            </div>
                <div className="w-full items-center justify-center flex flex-row space-x-10 mb-10">
                    <button
                    className=" btn glass w-[40%] bg-purple-600 text-white "

                    onClick={(e) => {
                        e.preventDefault();  
                        handleSubmit();
                    }}
                    >
                    Genera tu ruta
                    </button>
                </div> 

                {isMore && <BookingInfoComponent props={{ bookingInfo, route: props.route, placeName }} />}
                </CardBody>
        </Card>
        
      </div>
    </>

    );
}