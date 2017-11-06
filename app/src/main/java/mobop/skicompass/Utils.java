package mobop.skicompass;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

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

    /**
     * Returns drawable from url
     * @param url
     * @return
     */
    public static Drawable getDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "");
            return d;
        } catch (Exception e) {
            System.out.println("*******************Fehler: " + e);
            return null;
        }
    }

}
