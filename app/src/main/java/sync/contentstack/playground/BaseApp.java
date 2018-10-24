package sync.contentstack.playground;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.contentstack.sdk.Config;
import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Stack;

import static sync.contentstack.playground.BuildConfig.PLAYGROUND_SYNC_TOKEN;
import static sync.contentstack.playground.BuildConfig.SYNC_TOKEN_KEY;

public class BaseApp extends Application {

    private static Stack stackInstance;
    private static SharedPreferences SHARED_PREFERENCES;

    @Override
    public void onCreate() {
        super.onCreate();

        SHARED_PREFERENCES = getSharedPreferences(PLAYGROUND_SYNC_TOKEN, Context.MODE_PRIVATE);

        try {

            Config config = new Config();
            config.setHost(BuildConfig.STACK_URL);
            stackInstance = Contentstack.stack(getApplicationContext(), BuildConfig.API_KEY,
                    BuildConfig.DELIVERY_TOKEN, BuildConfig.ENVIRONMENT, config);

        } catch (Exception e) {
            Log.e("Base App", "Exception occurred");
            e.printStackTrace();
        }
    }


    public static Stack getStack(){
        return stackInstance;
    }


    public static String getSyncToken() {
        return SHARED_PREFERENCES.getString(SYNC_TOKEN_KEY, null);
    }


    public static void storeSyncToken(String sync_token) {

        SharedPreferences.Editor editor = SHARED_PREFERENCES.edit();
        editor.putString(SYNC_TOKEN_KEY, sync_token);
        editor.apply();
    }




}
