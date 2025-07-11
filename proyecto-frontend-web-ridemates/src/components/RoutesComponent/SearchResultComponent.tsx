// SearchResults.jsx
import {
  Avatar,
  Button,
  Card,
  CardBody,
  Timeline,
  TimelineConnector,
  TimelineHeader,
  TimelineIcon,
  TimelineItem,
  Typography,
} from "@material-tailwind/react";
import React from "react";
import proof from "../../assets/proof.jpg";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import { ClockIcon, FlagIcon, MapPinIcon } from "@heroicons/react/24/solid";

interface ResultsProps {
  results: RouteResponseDto[];
  className?: React.ReactNode;
  handleClick: (result: RouteResponseDto) => void;
}

export default function SearchResults({
  results,
  className,
  handleClick,
}: ResultsProps) {
  return (
    <div className={`mt-4 w-80 p-4 ${className}`}>
      <div className="flex flex-col space-y-4">
        {results.map((route, index) => (
          <Card key={index} className="w-full max-w-2xl mx-auto">
            <CardBody>
              <div className="flex items-center mb-4">
                <Avatar src={proof} size="sm" className="rounded-full mr-4" />
                <div>
                  <Typography variant="h5" color="blue-gray">
                    {route.origin} - {route.destination}
                  </Typography>
                  <Typography variant="small" color="gray">
                    {route.driverFullName}
                  </Typography>
                </div>
              </div>
              <Timeline>
                <TimelineItem className="h-28">
                  <TimelineConnector className="!w-[10px] ml-8 bg-black" />
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
                  <TimelineConnector className="!w-[10px] bg-black ml-8" />
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

              <Button
                className="ml-2"
                size="sm"
                color="black"
                onClick={() => handleClick(route)}
              >
                Reservar
              </Button>
            </CardBody>
          </Card>
        ))}
      </div>
    </div>
  );
}
/*
        <Typography variant="small" color="gray" className='break-words mb-5'>
        {format(new Date(), "PPPP", { locale: es })}
        </Typography>
*/
/*
<div className="container mx-auto px-4 py-8">
<Typography variant="h4" color="blue-gray" className="mb-6 text-center">
  Reviews
</Typography>
<div className="flex flex-col space-y-4">
  {reviews.map((review) => (
    <Card key={review.id} className="w-full max-w-2xl mx-auto shadow-lg">
      <CardBody>
        <div className="flex items-center mb-4">
          <Avatar src={review.avatar} alt={review.name} size="sm" className="rounded-full mr-4" />
          <div>
            <Typography variant="h5" color="blue-gray">
              {review.name}
            </Typography>
            <Typography variant="small" color="gray">
            {format(new Date(review.date), "PPPP", { locale: es })}
            </Typography>
          </div>
        </div>
        <Typography variant="small" color="gray" className='break-words'>
          {review.review}
        </Typography>
      </CardBody>
    </Card>
  ))}
</div>
</div>
);
}*/
