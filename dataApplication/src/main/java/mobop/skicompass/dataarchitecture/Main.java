/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mobop.skicompass.dataarchitecture;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Main implements Serializable{
    
	@SerializedName("temp")
    private double temp;
	
	@SerializedName("temp_min")
    private double tempMin;
	
	@SerializedName("temp_max")
    private double tempMax;
	
	@SerializedName("pressure")
	private double pressure;
	
	@SerializedName("humidity")
	private int humidity; 
	
    public Main(){};
    
    public Main(double temp, double tempMin, double tempMax, double pressure, int humidity) {
		this.temp = temp;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
		this.pressure = pressure;
		this.humidity = humidity;
	}

	public double getTemp() {
        return temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }
    
    public int getHumidity() {
		return humidity;
	}
    
    public double getPressure() {
		return pressure;
	}

}
