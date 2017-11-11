package mobop.skicompass.dataarchitecture;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Martin on 06.11.2017.
 */

public class Clouds implements Serializable {

	@SerializedName("all")
	private int all;

	private Clouds() {
		// Needed for GSon and FireBase
	}

	public Clouds(int all) {
		this.all = all;
	}

	public int getAll() {
		return all;
	}
}
