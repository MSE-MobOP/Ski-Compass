package mobop.skicompass.dataarchitecture;

import java.io.Serializable;

/**
 * Created by Martin on 04.11.2017.
 * Christian: Cant be uppercase -> gson serialization failure, even with annotation @SerializedName
 */
public enum OperatingStatus implements Serializable{
    Operating(1),
    Unknown(2),
    Closed(3);

    private int value;

    OperatingStatus(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}