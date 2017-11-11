/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mobop.skicompass.dataarchitecture;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wind implements Serializable {
    
	@SerializedName("deg")
    private double deg;
	
	@SerializedName("speed")
    private double speed;
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public double getDeg() {
		return deg;
	}
	
	public void setDeg(double deg) {
		this.deg = deg;
	}
}
