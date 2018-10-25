package sync.contentstack.playground;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import com.contentstack.sdk.Error;
import com.contentstack.sdk.Stack;
import com.contentstack.sdk.SyncResultCallBack;
import com.contentstack.sdk.SyncStack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import sync.contentstack.playground.databinding.ActivityMainBinding;

import android.os.Handler;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    // Holds for seconds for clear output
    private int WAIT_FOR_SECONDS = 2000;

    // UI binding
    private ActivityMainBinding binding;

    // Stack instance
    private Stack stack;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);
        stack = BaseApp.getStack();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subsquentsync();
            }
        });
    }



    /* It initialise very first time for full sync of the
        data in the application from the stack */

    private void initiateSync(){
        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null)
                {
                    showData(syncStack);
                }
            }
        });
    }





    /**
     * This request is made to get updated data from the stack
     */
    private void deltaRequestWithSyncToken(String syncToken){
        stack.syncToken(syncToken, new SyncResultCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null){
                    showData(syncStack);
                }
            }
        });

    }



    // Click Handler for Delta Button
    public void subsquentsync() {

        String syncToken = BaseApp.getSyncToken();
        if (syncToken != null){
            binding.container.tvStatus.setText("Loading...");
            deltaRequestWithSyncToken(syncToken);
        }else {
            binding.container.tvStatus.setText("You have not initialize sync call, initialise sync first");
        }

    }


    // Click handler for Initial click
    public void onInitialClick(View view) {
        binding.container.tvStatus.setText("Loading...");
        initiateSync();
    }



    private void showData(SyncStack syncStack){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int itemSize = syncStack.getCount();
                String syncToken = syncStack.getSyncToken();
                String message = null;
                if (syncToken!=null){
                    message = "Item count: "+itemSize+"\n"+"Next Sync Token: "+syncToken;
                    binding.container.messageString.setText(message);
                    BaseApp.storeSyncToken(syncToken);
                }
                binding.container.tvStatus.setText("SYNC Completed");
            }
        }, WAIT_FOR_SECONDS);
    }


}
