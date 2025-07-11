import {
	DirectionsRenderer,
	GoogleMap,
	LoadScript,
	Marker,
} from "@react-google-maps/api";
import { memo, useEffect, useState } from "react";

const containerStyle = {
	width: "100%",
	height: "100%",
	borderRadius: "25px",
};

const center: google.maps.LatLngLiteral = { lat: -12.13493, lng: -77.0217 };

interface MapRequest {
	path: google.maps.DirectionsResult | null;
}

function MapComponent({ props }: { props: MapRequest }) {
	const [map, setMap] = useState<google.maps.Map | null>(null);

	return (
		<>
			<div className=" overflow-hidden left-0 top-0 h-full">
				<LoadScript
					googleMapsApiKey={
						import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY as string
					}
					libraries={["places"]}
				>
					<GoogleMap
						mapContainerStyle={containerStyle}
						center={center}
						zoom={5}
						onLoad={(map) => setMap(map)}
						options={{
							zoomControl: false,
							streetViewControl: false,
							mapTypeControl: false,
							fullscreenControl: false,
						}}
					>
						<Marker position={center} />
						{/*
            {markerPosition && <Marker position={markerPosition} />}
*/}
						{props.path && <DirectionsRenderer directions={props.path} />}
					</GoogleMap>
				</LoadScript>
			</div>
		</>
	);
}

export default memo(MapComponent);
