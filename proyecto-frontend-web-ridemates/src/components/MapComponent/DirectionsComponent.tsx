import { useEffect, useRef, useState } from "react";
import { useMap, useMapsLibrary } from "@vis.gl/react-google-maps";

interface DirectionsProps {
  _origin: string | null;
  _destination: string | null;
  isPolyline : boolean
  setStopPlace : (place: google.maps.LatLng | null) => void
}

export default function Directions({ _origin, _destination, isPolyline, setStopPlace }: DirectionsProps) {
  const map = useMap();
  const routesLibrary = useMapsLibrary("routes");
  const [directionsService, setDirectionsService] = useState<google.maps.DirectionsService>();
  const [directionsRenderer, setDirectionsRenderer] = useState<google.maps.DirectionsRenderer>();
  const [routeResponse, setRouteResponse] = useState<google.maps.DirectionsResult | null>(null);
  const routePathRef = useRef<google.maps.Polyline | null>(null);
  const markersRef = useRef<google.maps.Marker[]>([]);
  const clickedPointsRef = useRef<google.maps.LatLngLiteral[]>([]); // Arreglo para almacenar las coordenadas clickeadas

  // Initialize directions service and renderer
  useEffect(() => {
    if (!routesLibrary || !map) return;
    setDirectionsService(new routesLibrary.DirectionsService());
    setDirectionsRenderer(new routesLibrary.DirectionsRenderer({ map }));
  }, [routesLibrary, map]);

  // Use directions service to calculate route
  useEffect(() => {
    if (!directionsService || !directionsRenderer || !_origin || !_destination) return;

    directionsService
      .route({
        origin: _origin,
        destination: _destination,
        travelMode: google.maps.TravelMode.DRIVING,
      })
      .then((response) => {
        directionsRenderer.setDirections(response);
        setRouteResponse(response);
      })
      .catch((error) => {
        console.error("Error calculating route:", error);
      });

    return () => {
      directionsRenderer.setMap(null);
    };
  }, [directionsService, directionsRenderer, _origin, _destination]);

  // Create Polyline and add click listener
  useEffect(() => {

    if (!isPolyline || !routeResponse || !map ) return;

    // Clean up previous Polyline and markers
    if (routePathRef.current) {
      routePathRef.current.setMap(null);
      routePathRef.current = null;
    }
    markersRef.current.forEach((marker) => marker.setMap(null));
    markersRef.current = [];
    clickedPointsRef.current = [];

    // Create Polyline from route
    const routePath = new google.maps.Polyline({
      path: routeResponse.routes[0].overview_path,
      strokeOpacity: 0,
      clickable: true, // Asegurarse de que sea clickeable
      map: map,
    });

    // Add click listener to Polyline
    routePath.addListener("click", (event: google.maps.PolyMouseEvent) => {
      markersRef.current.forEach((marker) => marker.setMap(null));
      clickedPointsRef.current = [];
      markersRef.current = [];
      const clickedLocation = event.latLng;
      if (clickedLocation) {
        // Log the exact coordinates
        const lat = clickedLocation.lat();
        const lng = clickedLocation.lng();
        console.log("Clicked location:", { lat, lng });
        setStopPlace(clickedLocation)

        // Store the coordinates in the array
        clickedPointsRef.current.push({ lat, lng });

        // Add a marker at the clicked location
        const marker = new google.maps.Marker({
          position: clickedLocation,
          map: map,
        });
        markersRef.current.push(marker);
      } else {
        console.warn("Clicked location is null");
      }
    });

    // Store Polyline reference
    routePathRef.current = routePath;

    return () => {
      // Cleanup Polyline
      routePath.setMap(null);
      // Cleanup markers
      markersRef.current.forEach((marker) => marker.setMap(null));
      markersRef.current = [];
      clickedPointsRef.current = [];
    };
  }, [routeResponse, map]);

  // Example of how to access the clicked points array
  useEffect(() => {
    // This effect runs whenever clickedPointsRef.current changes
    // Since we're using useRef, we need to monitor changes manually if needed
    // For example, you can log the clicked points when a new point is added
    console.log("Clicked points array updated:", clickedPointsRef.current);
  }, [clickedPointsRef.current]);

  return null; // No need to render anything here
}
