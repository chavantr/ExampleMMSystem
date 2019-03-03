/**
 *
 */
package com.mywings.messmanagementsystem.routes;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tatyabhau
 */
public class DirectionsJSONParser {

    /**
     * @param jObject
     * @return routes Receives a JSONObject and returns a list of lists
     * containing latitude and longitude
     */
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();

        JSONArray jRoutes = null, jSteps = null, jLegs = null;
        try {
            jRoutes = jObject.getJSONArray(RouteConstants.ROUTES);

            // Traversing all the routes.

            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i))
                        .getJSONArray(RouteConstants.LEGS);
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j))
                            .getJSONArray(RouteConstants.STEPS);

                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get(RouteConstants.POLYLINE))
                                .get(RouteConstants.POINTS);
                        List<LatLng> list = decodePoly(polyline);

                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hmLinePoints = new HashMap<>();
                            hmLinePoints.put(RouteConstants.LAT, Double
                                    .toString(((LatLng) list.get(l)).latitude));
                            hmLinePoints
                                    .put(RouteConstants.LNG,
                                            Double.toString(((LatLng) list
                                                    .get(l)).longitude));
                            path.add(hmLinePoints);
                        }

                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return routes;
    }

    /**
     * Method to decode polyline points Courtesy :
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

}