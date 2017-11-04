/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mobop.skicompass.dataarchitecture;

import com.google.gson.annotations.SerializedName;

public class Weather {
    
	@SerializedName("icon")
    public String icon;
	
	@SerializedName("description")
    public String description;
	
	@SerializedName("main")
    public String main;
	
	@SerializedName("id")
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
