package bluetooth_demo.kct.com.socialdemobyfirebase.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ainul on 21/5/18.
 */

public class AppPrefernce
{
    public static String USERNAME="user_name";
    public static String PROFILEDP="profiledp";
    public static String EMAILID="email_id";
    public static String TOKENKEY="token_key";

    private static final String name = "prefernce";

    private Context context;
    private SharedPreferences sharedPreferences;

    public AppPrefernce(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);

    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);

    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void clearData() {
        sharedPreferences.edit().clear().commit();
    }

}
