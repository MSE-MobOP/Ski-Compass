package mobop.skicompass;

import com.firebase.geofire.GeoLocation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

import mobop.skicompass.dataarchitecture.Clouds;
import mobop.skicompass.dataarchitecture.OperatingStatus;
import mobop.skicompass.dataarchitecture.SkiResort;
import mobop.skicompass.dataarchitecture.Weather;
import mobop.skicompass.dataarchitecture.WeatherData;

/**
 * Created by Martin on 09.11.2017.
 */

public class SortingUnitTest {

    private SkiResort[] skiResortList;
    GeoLocation myLocation;

    @Before
    public void prepareTest() {
        myLocation = new GeoLocation(48, 9);
        skiResortList = new SkiResort[3];
        skiResortList[0] = new SkiResort();
        skiResortList[1] = new SkiResort();
        skiResortList[2] = new SkiResort();
    }

    @Test
    public void testOperatingStatusSorting() {
        skiResortList[0].setOperatingStatus(OperatingStatus.Closed);
        skiResortList[1].setOperatingStatus(OperatingStatus.Unknown);
        skiResortList[2].setOperatingStatus(OperatingStatus.Operating);

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getComparator(myLocation, SortPriority.OPERATING));

        assertEquals(3, skiResortList.length);
        assertEquals(OperatingStatus.Operating, skiResortList[0].getOperatingStatus());
        assertEquals(OperatingStatus.Unknown, skiResortList[1].getOperatingStatus());
        assertEquals(OperatingStatus.Closed, skiResortList[2].getOperatingStatus());
    }

    @Test
    public void testWeatherSorting() {
        String sun = "01d";
        String cloudy = "02d";
        String snow = "13d";

        WeatherData weatherBest = new WeatherData(null, new ArrayList<Weather>(), null);
        weatherBest.getWeather().add(new Weather(sun, "", "", "", "", 1));
        WeatherData weatherCloudy = new WeatherData(null, new ArrayList<Weather>(), null);
        weatherCloudy.getWeather().add(new Weather(cloudy, "", "", "", "", 1));
        WeatherData weatherWors = new WeatherData(null, new ArrayList<Weather>(), null);
        weatherWors.getWeather().add(new Weather(snow, "", "", "", "", 1));

        skiResortList[0].setWeatherData(weatherWors);
        skiResortList[1].setWeatherData(weatherBest);
        skiResortList[2].setWeatherData(weatherCloudy);

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getComparator(myLocation, SortPriority.WEATHER));

        assertEquals(3, skiResortList.length);
        assertEquals(sun, skiResortList[0].getWeatherData().getWeather().get(0).getIcon());
        assertEquals(cloudy, skiResortList[1].getWeatherData().getWeather().get(0).getIcon());
        assertEquals(snow, skiResortList[2].getWeatherData().getWeather().get(0).getIcon());
    }

    @Test
    public void testOpenedLiftsSorting() {
        int mostOpened = 50;
        int middleOpened = 25;
        int leastOpened = 0;

        skiResortList[0].setOpenedLifts(middleOpened);
        skiResortList[1].setOpenedLifts(leastOpened);
        skiResortList[2].setOpenedLifts(mostOpened);

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getComparator(myLocation, SortPriority.OPENED_LIFTS));

        assertEquals(3, skiResortList.length);
        assertEquals(mostOpened, skiResortList[0].getOpenedLifts());
        assertEquals(middleOpened, skiResortList[1].getOpenedLifts());
        assertEquals(leastOpened, skiResortList[2].getOpenedLifts());
    }

    @Test
    public void testOpenedSlopsSorting() {
        int mostOpened = 50;
        int middleOpened = 25;
        int leastOpened = 0;

        skiResortList[0].setOpenedSlops(middleOpened);
        skiResortList[1].setOpenedSlops(leastOpened);
        skiResortList[2].setOpenedSlops(mostOpened);

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getComparator(myLocation, SortPriority.OPENED_SLOPS));

        assertEquals(3, skiResortList.length);
        assertEquals(mostOpened, skiResortList[0].getOpenedSlops(), 0.0);
        assertEquals(middleOpened, skiResortList[1].getOpenedSlops(), 0.0);
        assertEquals(leastOpened, skiResortList[2].getOpenedSlops(), 0.0);
    }

    @Test
    public void testLocationSorting() {
        GeoLocation nearestLocation = new GeoLocation(48.1, 9.1);
        GeoLocation middleLocation = new GeoLocation(49, 9);
        GeoLocation farthestLocation = new GeoLocation(48, 5);

        skiResortList[0].setLatitude(middleLocation.latitude);
        skiResortList[0].setLongitude(middleLocation.longitude);


        skiResortList[1].setLatitude(farthestLocation.latitude);
        skiResortList[1].setLongitude(farthestLocation.longitude);

        skiResortList[2].setLatitude(nearestLocation.latitude);
        skiResortList[2].setLongitude(nearestLocation.longitude);

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getComparator(myLocation, SortPriority.LOCATION));
        assertEquals(3, skiResortList.length);
        assertEquals(nearestLocation.latitude, skiResortList[0].getLatitude(), 0.0);
        assertEquals(nearestLocation.longitude, skiResortList[0].getLongitude(), 0.0);
        assertEquals(middleLocation.latitude, skiResortList[1].getLatitude(), 0.0);
        assertEquals(middleLocation.longitude, skiResortList[1].getLongitude(), 0.0);
        assertEquals(farthestLocation.latitude, skiResortList[2].getLatitude(), 0.0);
        assertEquals(farthestLocation.longitude, skiResortList[2].getLongitude(), 0.0);

    }
}
