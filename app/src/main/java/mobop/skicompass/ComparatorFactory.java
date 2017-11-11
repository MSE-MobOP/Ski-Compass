package mobop.skicompass;

import com.firebase.geofire.GeoLocation;

import java.util.Comparator;

import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by Christian on 10.11.2017.
 */

public class ComparatorFactory {

    public static DistanceComparator getDistanceComparator(GeoLocation location) {
        return new DistanceComparator(location);
    }

    public static WeatherComparator getWeatherComparator() {
        return new WeatherComparator();
    }

    public static OperatingComparator getOperatingComparator() {
        return new OperatingComparator();
    }

    public static OpenedLiftsComparator getOpenedLiftsComparator() {
        return new OpenedLiftsComparator();
    }

    public OpenedSlopsComparator getOpenedSlopsComparator() {
        return new OpenedSlopsComparator();
    }

    private static class DistanceComparator implements Comparator<SkiResort> {

        private GeoLocation location;

        DistanceComparator(GeoLocation location) {
            this.location = location;
        }

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            double dist1 = distFrom(location.latitude, location.longitude, skiResort.getLatitude(), skiResort.getLongitude());
            double dist2 = distFrom(location.latitude, location.longitude, t1.getLatitude(), t1.getLongitude());
            return Double.compare(dist1, dist2);
        }

        /**
         * Calculates distance between two coordinates given with lat and long
         *
         * @param lat1
         * @param lng1
         * @param lat2
         * @param lng2
         * @return
         */
        private double distFrom(double lat1, double lng1, double lat2, double lng2) {
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return earthRadius * c;
        }
    }

    private static class WeatherComparator implements Comparator<SkiResort> {
        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            return Integer.compare(skiResort.getWeatherData().getClouds().getAll(),
                    t1.getWeatherData().getClouds().getAll());
        }
    }

    private static class OperatingComparator implements Comparator<SkiResort> {

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            return Integer.compare(skiResort.getOperatingStatus().getValue(), t1.getOperatingStatus().getValue());
        }
    }

    private static class OpenedLiftsComparator implements Comparator<SkiResort> {

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            // Changed -> More is better
            return Integer.compare(t1.getOpenedLifts(), skiResort.getOpenedLifts());
        }
    }

    private static class OpenedSlopsComparator implements Comparator<SkiResort> {

        @Override
        public int compare(SkiResort skiResort, SkiResort t1) {
            // More is Better
            return Double.compare(t1.getOpenedSlops(), skiResort.getOpenedSlops());
        }
    }
}

