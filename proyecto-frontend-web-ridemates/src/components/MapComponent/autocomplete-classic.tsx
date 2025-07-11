import React, {useRef, useEffect, useState} from 'react';
import {useMapsLibrary} from '@vis.gl/react-google-maps';
import { set } from 'date-fns';

interface Props {
  onPlaceSelect: (place: string | null) => void;
  placeHolder: string
}

// This is an example of the classic "Place Autocomplete" widget.
// https://developers.google.com/maps/documentation/javascript/place-autocomplete
export const PlaceAutocompleteClassic = ({onPlaceSelect, placeHolder}: Props) => {
  const [placeAutocomplete, setPlaceAutocomplete] =
    useState<google.maps.places.Autocomplete | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const places = useMapsLibrary('places');

  useEffect(() => {
    if (!places || !inputRef.current) return;

    const limaBounds = new google.maps.LatLngBounds(
      { lat: -12.0268, lng: -77.1528 }, // Suroeste
      { lat: -11.7625, lng: -76.8853 }  // Noreste
    );
    const options = {
      fields: ['geometry', 'name', 'formatted_address'],
      bounds: limaBounds, // Restricción por límites
      componentRestrictions: { country: 'PE' }
    };
    setPlaceAutocomplete(new places.Autocomplete(inputRef.current, options));
  }, [places]);

  useEffect(() => {
    if (!placeAutocomplete) return;

    placeAutocomplete.addListener('place_changed', () => {
      onPlaceSelect(placeAutocomplete.getPlace().formatted_address ?? null);
      console.log("Place selected:");
      console.log(placeAutocomplete.getPlace());
    });
  }, [onPlaceSelect, placeAutocomplete]);

  return (
      <input
        type="text"
        placeholder={placeHolder}
        ref={inputRef}
        className=" w-[90%] p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600 duration-300 ease-in-out transform hover:scale-105 transition-all"
      />
  );
};