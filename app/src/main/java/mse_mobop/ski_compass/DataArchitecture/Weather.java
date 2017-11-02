/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mse_mobop.ski_compass.DataArchitecture;

public class Weather {
    
    public String icon;
    public String description;
    public String main;
    public int id;

    public int getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }
    
}
