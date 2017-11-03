/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mse_mobop.ski_compass.DataArchitecture;

import java.util.List;

public class WeatherData {

	private Main main;
	private List<Weather> weather;

	public Main getMain() {
		return main;
	}

	public List<Weather> getWeather() {
		return weather;
	}

}
