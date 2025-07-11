package com.ridemates.app.ors;

public interface OrsAPI {

    enum Profile {
        DRIVING_CAR, DRIVING_HGV, CYCLING_REGULAR, CYCLING_ROAD, CYCLING_ELECTRIC, FOOT_WALKING, WHEELCHAIR;

        public String asText() {
            return name().toLowerCase().replaceAll("_", "-");
        }
    }

    String ORS_DIRECTIONS = "https://api.openrouteservice.org/v2/directions/";

    String ORS_DISTANCE_MATRIX = "https://api.openrouteservice.org/v2/matrix/";

    String ORS_ISOLINES = "https://api.openrouteservice.org/v2/isochrones/";

    String ORS_OPTIMIZATION = "https://api.openrouteservice.org/v2/optimization/";

    String ORS_POIS = "https://api.openrouteservice.org/pois";

    String ORS_POIS_CATEGORY = "https://api.openrouteservice.org/pois/categories";

    String ORS_POIS_CATEGORY_ID = "https://api.openrouteservice.org/pois/categories/{category_id}";

    String ORS_POIS_ID = "https://api.openrouteservice.org/pois/{poi_id}";

    String ORS_POIS_NEARBY = "https://api.openrouteservice.org/pois/nearby";

    String ORS_POIS_NEARBY_RADIUS = "https://api.openrouteservice.org/pois/nearby/{radius}";

    String ORS_POIS_NEARBY_RADIUS_CATEGORY = "https://api.openrouteservice.org/pois/nearby/{radius}/{category_id}";

    String UNIT_KM = "km";
}
