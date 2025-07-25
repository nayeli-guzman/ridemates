import * as React from 'react';
import {AutocompleteMode} from '../../main';

interface Props {
  autocompleteModes: Array<AutocompleteMode>;
  selectedAutocompleteMode: AutocompleteMode;
  onAutocompleteModeChange: (autocompleteMode: AutocompleteMode) => void;
}

function ControlPanel({
  autocompleteModes,
  selectedAutocompleteMode,
  onAutocompleteModeChange
}: Props) {
  return (
    <div className="control-panel">
      <h3>Autocomplete Example</h3>

      <p>
        This example demonstrates three different methods of adding autocomplete
        functionality to your appplication using the Google Places API.
      </p>

      <div className="autocomplete-mode">
        <h4>Choose the example style: </h4>
        <select
          value={selectedAutocompleteMode.id}
          onChange={event => {
            const newMode = autocompleteModes.find(
              mode => mode.id === event.target.value
            );
            if (newMode) {
              onAutocompleteModeChange(newMode);
            }
          }}>
          {autocompleteModes.map(({id, label}) => (
            <option key={id} value={id}>
              {label}
            </option>
          ))}
        </select>
      </div>

      {selectedAutocompleteMode.id === 'classic' && (
        <div>
          This is the easiest to setup. You only need an input element and
          Google handles the rest.
        </div>
      )}
      
      <div className="links">
        <a
          href="https://codesandbox.io/s/github/visgl/react-google-maps/tree/main/examples/autocomplete"
          target="_new">
          Try on CodeSandbox ↗
        </a>

        <a
          href="https://github.com/visgl/react-google-maps/tree/main/examples/autocomplete"
          target="_new">
          View Code ↗
        </a>
      </div>
    </div>
  );
}

export default React.memo(ControlPanel);