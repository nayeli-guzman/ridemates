
import React from "react";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import { Avatar, Button, Card, CardBody, Timeline, TimelineConnector, TimelineHeader, TimelineIcon, TimelineItem, Typography } from "@material-tailwind/react";
import { ClockIcon, FlagIcon, MapPinIcon } from "@heroicons/react/24/outline";
import { useNavigate } from "react-router-dom";

interface RouteProps {
  route: RouteResponseDto;
}


const Route = React.forwardRef<HTMLDivElement, RouteProps>(({ route }, ref) => {

  const navigate = useNavigate();

  function formatDateTime(dateTime: string) {
    if (!dateTime) return { date: null, time: null }; // Manejo de casos nulos
    // Asegurarse de que sea un objeto Date
    const dateObj = new Date(dateTime);

    if (isNaN(dateObj.getTime())) {
      throw new Error("Invalid dateTime format");
    }

    // Opciones de formato

    const optionsDate = { year: "numeric", month: "2-digit", day: "2-digit" };
    const optionsTime = { hour: "2-digit", minute: "2-digit", hour12: false };

    const date = dateObj.toLocaleDateString("en-GB", optionsDate); // Formato DD/MM/YYYY
    const time = dateObj.toLocaleTimeString("en-GB", optionsTime); // Formato HH:MM (24 horas)

    return { date, time };
  }
  const handleClick = async (route: RouteResponseDto) => {
    navigate(`/booking?route=${route.id}`);
    console.log("to booking");
  };

  const mapa = `https://maps.googleapis.com/maps/api/staticmap?center=-12.135,-77.02167&zoom=auto&size=600x300&maptype=roadmap
&markers=color:red%7Clabel:A%7C${route.origin}
&markers=color:red%7Clabel:B%7C${route.destination}
&path=color:0x0000ff%7Cweight:5%7C${route.origin}%7C${route.destination}
&key=AIzaSyBGWn1KTubE2hPItoCro-fwmIcqCeRUxx0`

  const { date, time } = formatDateTime(route.dateTime);


  const routeBody = (
    <>
      <Card key={route.id}  onClick={() => handleClick(route)} className="cursor-pointer w-full max-w-2xl mx-auto duration-300 ease-in-out transform hover:scale-105 transition-all">
        <CardBody>
          <img src={mapa} alt="Mapa Estático" />
          <div className="flex items-center justify-center mb-4 mt-4">
            <div className="text-center">
              <Typography variant="small" color="gray" className="font-bold">
                Conductor: {route.driverFullName}
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
                    {route.origin}
                  </Typography>
                </div>
              </TimelineHeader>
            </TimelineItem>
            <TimelineItem className="h-28">
              <TimelineHeader className="relative rounded-xl border border-blue-gray-50 bg-white py-3 pl-4 pr-8 shadow-lg shadow-blue-gray-900/5">
                <TimelineIcon className="p-3" variant="ghost" color="green">
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
                    {route.destination}
                  </Typography>
              </div>
                  </TimelineHeader>
            </TimelineItem>
          </Timeline>
        </CardBody>
      </Card>
    </>
  );

  const content = ref ? (
    <article ref={ref}>{routeBody}</article>
  ) : (
    <article>{routeBody}</article>
  );

  return content;
});

export default Route;

/*
import React from "react";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";

interface RouteProps {
  route: RouteResponseDto;
}

const Route = React.forwardRef<HTMLDivElement, RouteProps>(({ route }, ref) => {
  function formatDateTime(dateTime: string) {
    if (!dateTime) return { date: null, time: null }; // Manejo de casos nulos

    // Asegurarse de que sea un objeto Date
    const dateObj = new Date(dateTime);

    if (isNaN(dateObj.getTime())) {
      throw new Error("Invalid dateTime format");
    }

    // Opciones de formato

    const optionsDate = { year: "numeric", month: "2-digit", day: "2-digit" };
    const optionsTime = { hour: "2-digit", minute: "2-digit", hour12: false };

    const date = dateObj.toLocaleDateString("en-GB", optionsDate); // Formato DD/MM/YYYY
    const time = dateObj.toLocaleTimeString("en-GB", optionsTime); // Formato HH:MM (24 horas)

    return { date, time };
  }

  const { date, time } = formatDateTime(route.dateTime);

  const routeBody = (
    <>
      <div
        className="overflow-hidden mx-auto shadow-md rounded-lg duration-300 ease-in-out transform hover:scale-105 transition-all w-[1500px]"
        style={{ width: "30%" }}
      >
        <div className="p-10">
          <div className="flex justify-between items-center">
            <div>
              <h3 className="text-purple-600 font-semibold">UTEC</h3>
              <p className="text-sm text-gray-500">{date}</p>
              <p className="text-sm text-gray-500">{time}</p>
              <div className="border-l-2 border-dotted border-gray-300 my-2 h-6"></div>
              <h3 className="text-purple-600 font-semibold">
                {route.destination}
              </h3>
            </div>
            <span className="text-purple-600 font-semibold bg-purple-100 px-3 py-1 rounded-full">
              price
            </span>
          </div>
          <p className="text-gray-400 text-xs mt-2">{time}</p>
        </div>
        <div className="border-t border-gray-200"></div>
        <div className="flex items-center p-7">
          
          <div className="flex-1">
            <h4 className="font-semibold text-gray-700">
              {route.driverFullName} <span className="text-blue-500">✔️</span>
            </h4>
            <p className="text-gray-400 text-sm">⭐ 5</p>
          </div>
          <div className="text-purple-600">
            <span className="material-icons"></span>
            <span>{route.capacity}</span>
          </div>
        </div>
      </div>
    </>
  );

  const content = ref ? (
    <article ref={ref}>{routeBody}</article>
  ) : (
    <article>{routeBody}</article>
  );

  return content;
});

export default Route;
*/