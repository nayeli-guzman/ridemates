import { Map } from "@vis.gl/react-google-maps"; 
import Directions from "./DirectionsComponent";

interface MapComponentAltProps {
  originPlace: string | null;
  destinationPlace: string | null;
  visible: boolean;
  isPolyline : boolean
  setStopPlace : (place: google.maps.LatLng | null) => void
}

export default function MapComponentAlt({ props }: { props: MapComponentAltProps }) {
  console.log("OLASLOALOLA")
  console.log(props.isPolyline);
  return (
    <Map
      id="map"
      style={{ width: "100%", height: "100%", borderRadius: "100px" }}
      defaultCenter={{ lat: -12.135, lng: -77.02167 }}
      defaultZoom={16}
      gestureHandling={"greedy"}
      disableDefaultUI={true}
    >
      {props.visible && (
        <Directions
          _origin={props.originPlace}
          _destination={props.destinationPlace}
          isPolyline={props.isPolyline}
          setStopPlace={props.setStopPlace}
        />
      )}
    </Map>
  );
}