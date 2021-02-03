package sync.contentstack.playground;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.contentstack.sdk.Config;
import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Stack;

import java.util.Objects;

import static sync.contentstack.playground.BuildConfig.SYNC_TOKEN_KEY;


public class BaseApp extends Application {
    //Stack instance variable
    private static Stack stackInstance;
    // shared pref for saving pagination token/ sync token
    private static SharedPreferences SHARED_PREFERENCES;
    // String shared pref KEY
    private final String SHARED_PREF = Objects.requireNonNull(BaseApp.class.getPackage()).getName();

    @Override
    public void onCreate() {
        super.onCreate();
        /*Initialise shared preference*/
        SHARED_PREFERENCES = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        try {
            // stack config to set host url
            Config config = new Config();
            // set your host url
            config.setHost(BuildConfig.STACK_URL);
            // To initialise Stack provide api_key, delivery_token & environment
            stackInstance = Contentstack.stack(getApplicationContext(), BuildConfig.API_KEY, BuildConfig.DELIVERY_TOKEN, BuildConfig.ENVIRONMENT, config);
        } catch (Exception e) {
            // In case app meets Exception
            Log.e("Base App", "Exception occurred");
            e.printStackTrace();
        }
    }


    /**
     *
     * @return stack instance
     */
    public static Stack getStack(){
        return stackInstance;
    }


    /**
     *
     * @return @{@link String} sync_token
     */
    public static String getSyncToken() { return SHARED_PREFERENCES.getString(SYNC_TOKEN_KEY, null); }


    /**
     *
     * @param sync_token accepted sync token @{@link String} to persists
     */
    public static void storeSyncToken(String sync_token) {
        SharedPreferences.Editor editor = SHARED_PREFERENCES.edit();
        editor.putString(SYNC_TOKEN_KEY, sync_token);
        editor.apply();
    }


}
