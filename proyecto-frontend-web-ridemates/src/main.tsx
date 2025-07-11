/*
import React, {useState} from 'react';
import {createRoot} from 'react-dom/client';
import {APIProvider, ControlPosition, Map} from '@vis.gl/react-google-maps';

import ControlPanel from './components/MapComponent/control-panel';
import {CustomMapControl} from './components/MapComponent/map-control';
import MapHandler from './components/MapComponent/map-handler';

const API_KEY = import.meta.env.VITE_APP_GOOGLE_MAPS_API_KEY as string;

export type AutocompleteMode = {id: string; label: string};

const autocompleteModes: Array<AutocompleteMode> = [
  {id: 'classic', label: 'Google Autocomplete Widget'},
];

const App = () => {
  const [selectedAutocompleteMode, setSelectedAutocompleteMode] =
    useState<AutocompleteMode>(autocompleteModes[0]);

  const [selectedPlace, setSelectedPlace] =
    useState<google.maps.places.PlaceResult | null>(null);

  return (
    <APIProvider apiKey={API_KEY}>
      <Map
          style={{width: '100vw', height: '100vh'}}
          defaultCenter={{lat: 22.54992, lng: 0}}
          defaultZoom={3}
          gestureHandling={'greedy'}
          disableDefaultUI={true}
        />

      <CustomMapControl
        controlPosition={ControlPosition.TOP}
        selectedAutocompleteMode={selectedAutocompleteMode}
        onPlaceSelect={setSelectedPlace}
      />

      <MapHandler place={selectedPlace} />

    </APIProvider>
  );
};

export default App;

createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
*/

import { APIProvider } from "@vis.gl/react-google-maps";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext.tsx";
import "./index.css";
import { router } from "./router/routes.tsx";
import { GoogleOAuthProvider } from '@react-oauth/google';

createRoot(document.getElementById("root")!).render(
  <GoogleOAuthProvider clientId={`${import.meta.env.VITE_GOOGLE_CLIENT_ID}`}>
	<APIProvider apiKey="AIzaSyBGWn1KTubE2hPItoCro-fwmIcqCeRUxx0">
		<AuthProvider>
			<StrictMode>
				<RouterProvider router={router} />
			</StrictMode>
		</AuthProvider>
	</APIProvider>
  </GoogleOAuthProvider>,
);
