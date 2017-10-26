/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mse_mobop.ski_compass.DataArchitecture;

public class Wind {
    
    private String deg;
    private String speed;
    
    public double getDeg() {
        return Double.parseDouble(deg);
    }
    
    public double getSpeed() {
        return Double.parseDouble(speed);
    }
    
}
