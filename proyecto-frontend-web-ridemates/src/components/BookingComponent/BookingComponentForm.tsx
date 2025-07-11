import { Avatar, Button, Card, CardBody, Timeline, TimelineConnector, TimelineHeader, TimelineIcon, TimelineItem, Typography } from "@material-tailwind/react";
import { GoogleMap, useJsApiLoader } from "@react-google-maps/api";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import { useEffect, useState } from "react";
import getRoute from "../../services/RouteService/GetRoute";
import { postBooking } from "../../services/BookingService/PostBooking";
import { ClockIcon, FlagIcon, MapPinIcon, TagIcon } from "@heroicons/react/24/outline";
import proof from "../../assets/proof.jpg";


interface BookingProps {
    routeId : string;
    route : RouteResponseDto;
    setNextPage : (confirmation:boolean) => void
    //    setPath : (route:google.maps.DirectionsResult) => void
}
export default function BookingComponentForm({props} : {props : BookingProps}) {

  const handleSubmit = async () => {
    props.setNextPage(true);
//    postBooking({ routeId: routeId, passenger: asiento, price: precio });
  };

  return (
    <>
      <div className="flex items-center justify-center w-full h-full">
      <Card key={props.routeId} className="w-[75%] max-w-2xl mx-auto">
        <CardBody className="flex flex-col items-center">
          <a className={`mb-10 btn btn-ghost style-navbar-link text-transparent bg-clip-text font-inter font-bold`}>
            Información de la ruta
          </a>
              <div className="flex items-center mb-4">
                <Avatar src={proof} size="sm" className="rounded-full mr-4" />
                <div>
                  <Typography variant="h5" color="blue-gray">
                    {props.route.driverFullName}
                  </Typography>
                  
                </div>
              </div>
              <Timeline>
                <TimelineItem className="h-28">
                  <TimelineConnector className="!w-[10px] ml-8 bg-purple-600" />
                  <TimelineHeader className="relative rounded-xl border border-blue-gray-50 bg-white py-3 pl-4 pr-8 shadow-lg shadow-blue-gray-900/5">
                    <TimelineIcon className="p-3" variant="ghost">
                      <MapPinIcon className="h-5 w-5" />
                    </TimelineIcon>
                    <div className="flex flex-col gap-1">
                      <Typography variant="h6" color="blue-gray">
                        Origen
                      </Typography>
                      <Typography
                        variant="small"
                        color="gray"
                        className="font-normal"
                      >
                        {props.route.origin}
                      </Typography>
                    </div>
                  </TimelineHeader>
                </TimelineItem>
                <TimelineItem className="h-28">
                  <TimelineConnector className="!w-[10px] bg-purple-600 ml-8" />
                  <TimelineHeader className="relative rounded-xl border border-blue-gray-50 bg-white py-3 pl-4 pr-8 shadow-lg shadow-blue-gray-900/5">
                    <TimelineIcon className="p-3" variant="ghost" color="red">
                      <ClockIcon className="h-5 w-5" />
                    </TimelineIcon>
                    <div className="flex flex-col gap-1">
                      <Typography variant="h6" color="blue-gray">
                        Tiempo estimado
                      </Typography>
                      <Typography
                        variant="small"
                        color="gray"
                        className="font-normal"
                      >
                        1 Hora
                      </Typography>
                    </div>
                  </TimelineHeader>
                </TimelineItem>
                <TimelineItem className="h-28">
                  <TimelineConnector className="!w-[10px] bg-purple-600 ml-8" />
                  <TimelineHeader className="relative rounded-xl border border-blue-gray-50 bg-white py-3 pl-4 pr-8 shadow-lg shadow-blue-gray-900/5">
                    <TimelineIcon className="p-3" variant="ghost" color="red">
                      <FlagIcon className="h-5 w-5" />
                    </TimelineIcon>
                    <div className="flex flex-col gap-1">
                      <Typography variant="h6" color="blue-gray">
                        Destino
                      </Typography>
                      <Typography
                        variant="small"
                        color="gray"
                        className="font-normal"
                      >
                        {props.route.destination}
                      </Typography>
                    </div>
                  </TimelineHeader>
                </TimelineItem>
                <TimelineItem className="h-28">
                  <TimelineHeader className="relative rounded-xl border border-blue-gray-50 bg-white py-3 pl-4 pr-8 shadow-lg shadow-blue-gray-900/5">
                    <TimelineIcon className="p-3" variant="ghost" color="green">
                      <TagIcon className="h-5 w-5" />
                    </TimelineIcon>
                    <div className="flex flex-col gap-1">
                      <Typography variant="h6" color="blue-gray">
                        Precio
                      </Typography>
                      <Typography
                        variant="small"
                        color="gray"
                        className="font-normal"
                      >
                        5 soles
                      </Typography>
                    </div>
                  </TimelineHeader>
                </TimelineItem>
              </Timeline>

              <div className="w-full items-center justify-center flex flex-row space-x-10">
              <button
                  className=" btn glass w-[40%] bg-purple-600 text-white "

                  onClick={(e) => {
                    e.preventDefault();  
                    handleSubmit();
                  }}
                >
                  Haz una reserva!
                </button>
                </div> 
            </CardBody>
          </Card>
        
      </div>
    </>
  );
}
