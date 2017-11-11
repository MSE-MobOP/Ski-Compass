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
	
	@SerializedName("tempMin")
    private double tempMin;
	
	@SerializedName("tempMax")
    private double tempMax;

    public Main(){};

    public Main(double temp, double tempMin, double tempMax) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
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

}
