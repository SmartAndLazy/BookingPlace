package ru.nutscoon.mapsproject.Screens;

import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.mapping.MapPolyline;

public class Haversin {

    public static final double R = 6371000; // In kilometers

    // calculate distance with haversine formula
    public static double mesure(double lat1, double lon1, double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    // example how to use

}
