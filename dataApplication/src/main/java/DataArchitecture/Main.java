/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package DataArchitecture;

public class Main {
    
    private String temp;
    private String temp_min;
//    private String humidity;
//    private String pressure;
    private String temp_max;
    
    public double getTemp() {
        return calcTemp(temp);
    }
    
    public double getTempMax() {
        return calcTemp(temp_max);
    }
    
    public double getTempMin() {
        return calcTemp(temp_min);
    }
    
    private double calcTemp(String text) {
        return Double.parseDouble(text) - 273;
    }
}
