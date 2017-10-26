/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mse_mobop.ski_compass.DataArchitecture;

import java.util.List;

public class WeatherData {

	private List<Weather> weather;
	private Main main;

	public Weather getWeather() {
		return weather.get(0);
	}

	public Main getMain() {
		return main;
	}

}
