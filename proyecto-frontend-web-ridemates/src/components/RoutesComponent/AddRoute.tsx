import { useState } from "react";
import MapComponentAlt from "../MapComponent/MapComponentAlt";
import AddRouteForm from "./AddRouteForm";

export default function AddRoute() {
	const [path, setPath] = useState<google.maps.DirectionsResult | null>(null);

	const [originPlace, setOriginPlace] = useState<string | null>(null);
	const [destinationPlace, setDestinationPlace] = useState< string | null>(null);
	
	const [visible, setVisible] = useState(false);
	const isPolyline = false;
	const [stopPlace, setStopPlace] = useState<google.maps.LatLng | null>(null);


	return (
		<>
			<div className="w-full flex flex-row">
				<div className="w-1/2">
					<MapComponentAlt props={{ originPlace, destinationPlace, visible, isPolyline, setStopPlace}} />
				</div>
				<div className="w-1/2">
					<AddRouteForm props={{ originPlace, setOriginPlace, destinationPlace, setDestinationPlace, setVisible }} />
				</div>
			</div>
		</>
	);
}
