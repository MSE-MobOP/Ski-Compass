package mobop.skicompass;

import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void prepareTest() {
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

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getOperatingComparator());

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

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getWeatherComparator());

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

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getOpenedLiftsComparator());

        assertEquals(skiResortList.length, 3);
        assertEquals(skiResortList[0].getOpenedLifts(), mostOpened);
        assertEquals(skiResortList[1].getOpenedLifts(), middleOpened);
        assertEquals(skiResortList[2].getOpenedLifts(), leastOpened);
    }

    @Test
    public void testOpenedSlopsSorting(){
        int mostOpened = 50;
        int middleOpened = 25;
        int leastOpened = 0;

        skiResortList[0].setOpenedLifts(middleOpened);
        skiResortList[1].setOpenedLifts(leastOpened);
        skiResortList[2].setOpenedLifts(mostOpened);

        java.util.Arrays.sort(skiResortList, ComparatorFactory.getOpenedLiftsComparator());

        assertEquals(skiResortList.length, 3);
        assertEquals(skiResortList[0].getOpenedLifts(), mostOpened);
        assertEquals(skiResortList[1].getOpenedLifts(), middleOpened);
        assertEquals(skiResortList[2].getOpenedLifts(), leastOpened);
    }
}
