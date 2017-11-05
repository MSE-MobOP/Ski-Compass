package mobop.skicompass;

import android.content.Context;

/**
 * Created by Christian on 05.11.2017.
 */

public class Utils {

    /**
     * Returns the localized string with the name specified.
     * Attention: Uses reflection -> possible loss of performance!
     * @param context
     * @param name
     * @return
     */
    public static int getStringResourceByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

}
