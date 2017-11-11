package mobop.skicompass;

import com.firebase.geofire.GeoLocation;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

import mobop.skicompass.dataarchitecture.Clouds;
import mobop.skicompass.dataarchitecture.OperatingStatus;
import mobop.skicompass.dataarchitecture.SkiResort;
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

        assertEquals(skiResortList.length, 3);
        assertEquals(skiResortList[0].getOperatingStatus(), OperatingStatus.Operating);
        assertEquals(skiResortList[1].getOperatingStatus(), OperatingStatus.Unknown);
        assertEquals(skiResortList[2].getOperatingStatus(), OperatingStatus.Closed);
    }

    @Test
    public void testWeatherSorting() {
        int bestCondition = 10;
        int middleCondition = 50;
        int weakCondition = 80;

        WeatherData weatherBest = new WeatherData();
        weatherBest.setClouds(new Clouds(bestCondition));
        WeatherData weatherMiddle = new WeatherData();
        weatherMiddle.setClouds(new Clouds(middleCondition));
        WeatherData weatherWeak = new WeatherData();
        weatherWeak.setClouds(new Clouds(weakCondition));

        skiResortList[0].setWeatherData(weatherWeak);
        skiResortList[1].setWeatherData(weatherBest);
        skiResortList[2].setWeatherData(weatherMiddle);

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getComparator(myLocation, SortPriority.WEATHER));

        assertEquals(skiResortList.length, 3);
        assertEquals(skiResortList[0].getWeatherData().getClouds().getAll(), bestCondition);
        assertEquals(skiResortList[1].getWeatherData().getClouds().getAll(), middleCondition);
        assertEquals(skiResortList[2].getWeatherData().getClouds().getAll(), weakCondition);
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

        assertEquals(skiResortList.length, 3);
        assertEquals(skiResortList[0].getOpenedLifts(), mostOpened);
        assertEquals(skiResortList[1].getOpenedLifts(), middleOpened);
        assertEquals(skiResortList[2].getOpenedLifts(), leastOpened);
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

        assertEquals(skiResortList.length, 3);
        assertEquals(skiResortList[0].getOpenedSlops(), mostOpened, 0.0);
        assertEquals(skiResortList[1].getOpenedSlops(), middleOpened, 0.0);
        assertEquals(skiResortList[2].getOpenedSlops(), leastOpened, 0.0);
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
        assertEquals(skiResortList.length, 3);
        assertEquals(skiResortList[0].getLatitude(), nearestLocation.latitude, 0.0);
        assertEquals(skiResortList[0].getLongitude(), nearestLocation.longitude, 0.0);
        assertEquals(skiResortList[1].getLatitude(), middleLocation.latitude, 0.0);
        assertEquals(skiResortList[1].getLongitude(), middleLocation.longitude, 0.0);
        assertEquals(skiResortList[2].getLatitude(), farthestLocation.latitude, 0.0);
        assertEquals(skiResortList[2].getLongitude(), farthestLocation.longitude, 0.0);

    }
}
