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
    private String deg;
	
	@SerializedName("speed")
    private String speed;
    
    public double getDeg() {
        return Double.parseDouble(deg);
    }
    
    public double getSpeed() {
        return Double.parseDouble(speed);
    }
    
}
