/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package DataArchitecture;

public class WeatherData {
    
    private Weather[] weather;
    private Main main;
    
    public Weather getWeather() {
        return weather[0];
    }
    
    public Main getMain() {
        return main;
    }
    
}
