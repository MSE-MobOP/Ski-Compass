package mobop.skicompass.dataarchitecture;

import java.io.Serializable;

/**
 * Created by Martin on 04.11.2017.
 * Christian: Cant be uppercase -> gson serialization failure, even with annotation @SerializedName
 */
public enum OperatingStatus implements Serializable{
    Operating,
    Unknown,
    Closed,
}