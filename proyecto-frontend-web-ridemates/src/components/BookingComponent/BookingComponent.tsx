import { Card, CardBody, Typography } from "@material-tailwind/react";
import { useEffect, useState } from "react";
import MapComponentAlt from "../MapComponent/MapComponentAlt";
import BookingComponentForm from "./BookingComponentForm";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import getRoute from "../../services/RouteService/GetRoute";
import BookingComponentFormAft from "./BookingComponentFormAft";


export default function BookingComponent() {
	const queryParams = new URLSearchParams(location.search);
	const routeId = queryParams.get("route");
	const [originPlace, setOriginPlace] = useState<string | null>(null);
	const [destinationPlace, setDestinationPlace] = useState<string | null>(null);
	const [stopPlace, setStopPlace] = useState<google.maps.LatLng | null>(null);
	
	const [visible, setVisible] = useState(true);
	const isPolyline = true;
	const [route, setRoute] = useState<RouteResponseDto>( {
		id: routeId,
		origin: '',
		destination:'',
		capacity:0,
		dateTime: '',
		driverFullName : '',
	  });

	if (routeId === null) {
		return (
			<>
				<div className="flex items-center justify-center w-full">
					<Card className="w-full max-w-md">
						<CardBody>
							<Typography variant="h5" color="blue-gray">
								No se ha seleccionado una ruta
							</Typography>
						</CardBody>
					</Card>
				</div>
			</>
		);
	}

	useEffect(() => {

		const fetchRoute = async () => {
		  if (routeId) {
			try {
			  const response = await getRoute(routeId);
			  console.log("Get Route Response:");
			  console.log(response.data);
			  setOriginPlace(response.data.origin);
			  setDestinationPlace(response.data.destination);
			  setRoute(response.data);
				
			} catch (error) {
			  console.error("Error fetching route:", error);
			}
		  }
		};
	
		fetchRoute();
		
	  }, [routeId]);

	const [nextPage, setNextPage] = useState(false);

	return (
		<>
			<div className="w-full flex flex-row">

				<div className="w-1/2">
					{
						nextPage ? 
						<BookingComponentFormAft props={{ routeId: routeId.toString(), route, setNextPage, stopPlace }} /> :
						<BookingComponentForm props={{routeId, route, setNextPage}} />
					}
				</div>

				<div className="w-1/2">
					<MapComponentAlt props={{ originPlace, destinationPlace, visible, isPolyline, setStopPlace}} />
				</div>
				
			</div>
		</>
	);
}
