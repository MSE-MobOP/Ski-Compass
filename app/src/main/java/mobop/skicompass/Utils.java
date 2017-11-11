package mobop.skicompass;

/**
 * Created by Christian on 11.11.2017.
 */

public class Utils {

    private Utils() {
    }

    /**
     * Converts absolute temperature to normal temperature
     * @param temp
     * @return
     */
    public static double absToNormal(double temp) {
        return temp - 273.0;
    }

}
