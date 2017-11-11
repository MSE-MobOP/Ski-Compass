/*
 * Ski-Compass App 2017 MobOp MSE
 * C.Schmid, M.Messmer, A.Papaj
 * @author C.Schmid
 */
package mobop.skicompass.dataarchitecture;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Weather implements Serializable{
    
	@SerializedName("icon")
    private String icon;
	
	@SerializedName("description")
    private String description;
	
	private String descriptionDE;
	
	private String descriptionFR;
	
	@SerializedName("main")
    private String main;
	
	@SerializedName("id")
    private int id;

    public Weather(){}

    public Weather(String icon, String description, String descriptionDE, String descriptionFR, String main, int id) {
        this.icon = icon;
        this.description = description;
        this.descriptionDE = descriptionDE;
        this.descriptionFR = descriptionFR;
        this.main = main;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
		this.description = description;
	}
    
    public String getDescriptionDE() {
		return descriptionDE;
	}
    
    public void setDescriptionDE(String descriptionDE) {
		this.descriptionDE = descriptionDE;
	}
    
    public String getDescriptionFR() {
		return descriptionFR;
	}
    
    public void setDescriptionFR(String descriptionFR) {
		this.descriptionFR = descriptionFR;
	}

    public String getMain() {
        return main;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
