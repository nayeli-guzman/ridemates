import { TrashIcon } from "@heroicons/react/24/outline";
import { Card, CardBody, Typography } from "@material-tailwind/react";
import { ChangeEvent, FormEvent, RefObject, useRef, useState } from "react";
import { postRoute } from "../../services/RouteService/PostRoute";
import { RouteRequestDto } from "../../dtos/Route/RouteRequestDto";
import { useNavigate } from "react-router-dom";
import { CustomMapControl } from "../MapComponent/map-control";
import MapHandler from "../MapComponent/map-handler";
import {ControlPosition, MapControl} from '@vis.gl/react-google-maps';
import { PlaceAutocompleteClassic } from "../MapComponent/autocomplete-classic";
import { on } from "events";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";

interface MapRequest {
  destinationPlace : string | null;
  setDestinationPlace: (place: string | null) => void;
  originPlace : string | null;
  setOriginPlace: (place: string | null) => void;
  setVisible : (visible: boolean) => void
  //path : google.maps.DirectionsResult | null;
  //setPath : (route:google.maps.DirectionsResult) => void
}

const bounds = {
  north: -11.55, // Límite norte de Lima
  south: -12.26, // Límite sur de Lima
  east: -76.77,  // Límite este de Lima
  west: -77.33,  // Límite oeste de Lima
};

export default function AddRouteForm({props} : { props: MapRequest}) {
  const [markerPosition, setMarkerPosition] = useState<{ lat: number; lng: number } | null>(null);
  
  const [data, setData] = useState<RouteRequestDto>({
    origin: "",
    destination: "",
    capacity: 0,
    dateTime: ""
  });


  const [date, setDate] = useState<string>("");
  const [time, setTime] = useState<string>(''); 
  const [showAlert, setShowAlert] = useState(false);
  const [route, setRoute] = useState<RouteResponseDto>({
    id: "",
    origin: "",
    destination: "",
    capacity: 0,
    dateTime: "", // Use string for ISO date format
    driverFullName: ""
  })
  const navigate = useNavigate();


  function clearRoute() {
    props.setVisible(false);
  }

  async function calculateRoute() {

    if(props.originPlace === null || props.destinationPlace === null ) 
      return;

    console.log("saaaaa")

    console.log(data)



    setData({
      ...data,
      origin: props.originPlace,
      destination: props.destinationPlace
    });

    props.setVisible(true);
  }

  async function handleSubmit(e:FormEvent<HTMLFormElement>){
    e.preventDefault();
    try {
        console.log(data)
        const response =await postRoute(data);
        
        setRoute(response);
        setShowAlert(true);
        /*
        setTimeout(() => {
          setShowAlert(false);
          navigate("/available-routes")
        }, 3); // 3 segundos en milliseconds
        */
      } catch (error) {
        console.log("error")
        console.log(error)
    }
}

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const _dateTime = date + "T" + time + "Z"
        const { name, value } = e.target;

        setData({
            ...data,
            dateTime: _dateTime,
            [name]: value,
        });
    }

    const handleResetInputs = () => {
      setData({
        origin: "",
        destination: "",
        capacity: 0,
        dateTime: ""
      });
    };

  return (
    <div className="flex flex-col w-full items-center justify-center h-full">

        <Card className="p-4 w-[75%] shadow-lg">
          <CardBody className="flex flex-col items-center">

          <a className={`mb-10 btn btn-ghost style-navbar-link text-transparent bg-clip-text font-inter font-bold`}>
            Publica tu ruta
          </a>
            
            <form className="space-y-8" onSubmit={handleSubmit}>
              <div className="w-full flex flex-row  items-center justify-between gap-4">
                    
                <PlaceAutocompleteClassic onPlaceSelect={props.setOriginPlace} placeHolder="Origen" />
                <PlaceAutocompleteClassic onPlaceSelect={props.setDestinationPlace} placeHolder="Destino" />

              {/*<MapHandler place={props.originPlace} />*/}
              </div>

              <div className="w-full items-center justify-center flex flex-row space-x-10">
                <button
                  aria-label="clear route"
                  className="w-[6%] text-gray-600 rounded-full hover:bg-gray-100 focus:outline-none duration-300 ease-in-out transform hover:scale-105 transition-all"
                  onClick={clearRoute}
                  >
                  <TrashIcon className="h-6 w-6" />
                </button> 
                <button
                  className=" btn glass w-[50%] bg-purple-600 text-white "

                  onClick={(e) => {
                    e.preventDefault();  
                    calculateRoute(); 
                  }}
                >
                  Calcular Ruta
                </button> 
              </div>

              <div className="w-full flex items-center justify-center space-x-10">
                <p className="w-[50%]">Fecha de la Ruta</p>
                    
                    <label className="input input-bordered flex items-center gap-2 w-full duration-300 ease-in-out transform hover:scale-105 transition-all ">
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    viewBox="0 0 16 16"
                                    fill="currentColor"
                                    className="h-4 w-4 opacity-70">
                                    <path
                                        fillRule="evenodd"
                                        d="M8 0a1 1 0 0 1 1 1v1h5a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1h5V1a1 1 0 0 1 1-1z"
                                        clipRule="evenodd" />
                                </svg>
                                <input 
                                    type="date" 
                                    name="date" 
                                    id="date" 
                                    value={date} 
                                    onChange={(event) => setDate(event.target.value)}
                                    className="grow  " 
                                    placeholder="Fecha Ruta"
                                />
                    </label>
                </div>
              
              <div className="w-full flex items-center justify-center space-x-10">
                <p className="w-[50%]">Hora de la Ruta</p>
                  <label className="input input-bordered flex items-center gap-2 w-full duration-300 ease-in-out transform hover:scale-105 transition-all">

                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 16 16"
                        fill="currentColor"
                        className="h-4 w-4 opacity-70">
                        <path
                            fillRule="evenodd"
                            d="M8 0a1 1 0 0 1 1 1v1h5a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1h5V1a1 1 0 0 1 1-1z"
                            clipRule="evenodd" />
                    </svg>
                  <input 
                      type="time" 
                      name="dateTime" 
                      id="dateTime" 
                      value={time.slice(0, 5)} 
                      onChange={(event) => setTime(`${event.target.value}:00`)}
                      className="grow" 
                      placeholder="Hora"
                            />
                  </label>
              </div>

              <div className="w-full flex items-center justify-center space-x-10">
                <p className="w-[50%]">Capacidad de la Ruta</p>
  
                    <label className="input input-bordered flex items-center gap-2 w-full duration-300 ease-in-out transform hover:scale-105 transition-all">
                        <input 
                              type="number" 
                              name="capacity"
                              id="capacity"
                              value={data.capacity} 
                              onChange={handleChange}
                              placeholder="Capacidad"
                              min="1" 
                              max="10" 
                              step="1"
                              className="grow bg-transparent outline-none px-4 py-2 h-full flex-shrink-0"
                            />    
                    </label>

                    
              </div>
              
              <div className="w-full items-center justify-center flex flex-row space-x-10">
                <button
                  className=" btn glass w-1/3 bg-purple-600 text-white "
                  type="submit"
                >
                  Enviar
                </button> 
              </div>

            </form>

            {showAlert && (
              <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                <Card className="flex items-center justify-between w-1/3 rounded-3xl relative">
                  <button
                    className="absolute top-2 mr-3 mt-2 right-2 text-gray-600 hover:text-gray-800"
                    onClick={() => {
                      setShowAlert(false)
                      handleResetInputs();
                    }} // Agregar funcionalidad para cerrar
                  >
                    ✕
                  </button>
                  <CardBody className="w-[95%]">
                    <Typography variant="h6" className="text-center text-purple-600 text-lg font-bold">
                      Ruta creada con éxito
                    </Typography>
                    <div className="mt-4 space-y-2">
                      <Typography className="text-gray-600">
                        <strong className="text-purple-600">Origen:</strong> {route.origin}
                      </Typography>
                      <Typography className="text-gray-600">
                        <strong className="text-purple-600">Destino:</strong> {route.destination}
                      </Typography>
                      <Typography className="text-gray-600">
                        <strong className="text-purple-600">Capacidad:</strong> {route.capacity} pasajeros
                      </Typography>
                      <Typography className="text-gray-600">
                        <strong className="text-purple-600">Fecha y Hora:</strong> {new Date(route.dateTime).toLocaleString()}
                      </Typography>
                      <Typography className="text-gray-600">
                        <strong className="text-purple-600">Conductor:</strong> {route.driverFullName}
                      </Typography>
                    </div>
                  </CardBody>
                </Card>
              </div>
            )}
          </CardBody>
        </Card>
    </div>
  )
}