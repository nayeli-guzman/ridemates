import React, { useEffect, useState, useRef, useCallback } from "react";
import { Card, CardBody, Typography } from "@material-tailwind/react";
import { getMyTravels, getTravels } from "../../services/TravelService/GetTravels"; // Assume this service fetches travels data
import { CheckCircleIcon } from "@heroicons/react/24/outline";

const ITEMS_PER_PAGE = 4;

const sampleTravels = [
  {
    destination: "Madrid",
    createdAt: new Date().toISOString(),
    distance: 500,
  },
  {
    destination: "Barcelona",
    createdAt: new Date().toISOString(),
    distance: 300,
  },
  {
    destination: "Valencia",
    createdAt: new Date().toISOString(),
    distance: 350,
  },
  {
    destination: "Sevilla",
    createdAt: new Date().toISOString(),
    distance: 400,
  },
  {
    destination: "Bilbao",
    createdAt: new Date().toISOString(),
    distance: 600,
  },
  {
    destination: "Granada",
    createdAt: new Date().toISOString(),
    distance: 450,
  },
];

export default function MyTravelsComponent() {
  const [travels, setTravels] = useState<any[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const observer = useRef<IntersectionObserver | null>(null);
  const [status, setStatus] = useState("en-proceso");

  // Función para cambiar el estado del viaje
  const toggleStatus = () => {
    setStatus((prevStatus) =>
      prevStatus === "en-proceso" ? "completado" : "en-proceso"
    );
  }

  useEffect(() => {
    const fetchTravels = async () => {
      try {
        setLoading(true);
        const response = await getMyTravels(currentPage, ITEMS_PER_PAGE);
        if (response.data.length === 0 && currentPage === 1) {
          setTravels(sampleTravels);
        } else {
          setTravels((prevTravels) => [...prevTravels, ...response.data]);
        }
        if (response.data.length < ITEMS_PER_PAGE) {
          setHasMore(false);
        }
      } catch (error) {
        console.error("Error fetching travels:", error);
        if (travels.length === 0) {
          setTravels(sampleTravels);
        }
      } finally {
        setLoading(false);
      }
    };

    fetchTravels();
  }, [currentPage]);

  const lastTravelElementRef = useCallback(
    (node) => {
      if (observer.current) observer.current.disconnect();
      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasMore) {
          setCurrentPage((prevPage) => prevPage + 1);
        }
      });
      if (node) observer.current.observe(node);
    },
    [hasMore],
  );

  return (
    <div className="flex w-full h-full flex-col items-center justify-center">
      <Card className="h-full w-full  flex flex-col justify-center">
        
        <div className="items-center justify-center flex mb-10">
          <a className={`btn btn-ghost style-main-txt text-transparent bg-clip-text font-inter font-bold  w-full `}>
            Mis Rutas
          </a>
        </div>
        <CardBody>
          <div className="grid w-full grid-cols-1 md:grid-cols-3 gap-4">
            {travels.map((travel, index) => {
              if (travels.length === index + 1) {
                return (
                  <Card
                    key={index}
                    className="w-full"
                    ref={lastTravelElementRef}
                  >
                    <CardBody>
                      <Typography variant="h6" color="blue-gray">
                        {travel.destination}
                      </Typography>
                      <Typography variant="h5" color="gray">
                        {new Date(travel.createdAt).toISOString()}
                      </Typography>
                      <Typography variant="h5" color="gray">
                        {travel.distance} km
                      </Typography>
                    </CardBody>
                  </Card>
                );
              } else {
                return (
                  <Card
                  key={index}
                  className={`w-full ${
                    status === "en-proceso" ? "bg-yellow-200" : "bg-green-200"
                  }`}
                >
                  <CardBody className="flex flex-col items-center justify-center">
                    <Typography variant="h6" color="blue-gray">
                      {travel.destination}
                    </Typography>
                    <Typography variant="h5" color="gray">
                      {new Date(travel.createdAt).toISOString()}
                    </Typography>
                    <Typography variant="h5" color="gray">
                      {travel.distance} km
                    </Typography>
                    <div className="flex items-center space-x-2">
                      {status === "completado" && (
                        <CheckCircleIcon className="text-green-500" width={20} height={20} />
                      )}
                      <Typography variant="h6" color={status === "en-proceso" ? "yellow" : "green"}>
                        Estado: {status === "en-proceso" ? "En proceso" : "Completado"}
                      </Typography>
                    </div>
                    <button
                      onClick={toggleStatus}
                      className="mt-4 px-4 py-2 bg-blue-500 text-white rounded"
                    >
                      Cambiar estado
                    </button>
                  </CardBody>
                </Card>
                );
              }
            })}
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
