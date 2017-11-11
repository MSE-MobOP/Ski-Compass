package mobop.skicompass;

import com.firebase.geofire.GeoLocation;

import java.util.Comparator;

import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by Christian on 10.11.2017.
 */

public class ComparatorFactory {

    public DistanceComparator getDistanceComparator(GeoLocation location) {
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

    private class DistanceComparator implements Comparator<SkiResort> {

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
            String icon1 = skiResort.getWeatherData().getWeather().get(0).getIcon();
            String icon2 = skiResort.getWeatherData().getWeather().get(0).getIcon();
            return Integer.compare(weightWeather(icon2), weightWeather(icon1));
        }

        private int weightWeather(String weather) {
            int weight = Integer.parseInt(weather.substring(0, weather.length() - 2));
            int corrWeight;
            switch(weight) {
                case 13:
                    corrWeight = 5;
                    break;
                case 10:
                    corrWeight = 7;
                    break;
                case 50:
                    corrWeight = 6;
                    break;
                default:
                    corrWeight = weight;
                    break;
            }
            return corrWeight;
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

