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

	public Wind() {
	}

	public Wind(double deg, double speed) {
		this.deg = deg;
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public double getDeg() {
		return deg;
	}
}
