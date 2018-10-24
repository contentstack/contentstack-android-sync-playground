package sync.contentstack.playground;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.contentstack.sdk.Error;
import com.contentstack.sdk.Stack;
import com.contentstack.sdk.SyncResultCallBack;
import com.contentstack.sdk.SyncStack;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import sync.contentstack.playground.databinding.ActivityPlayBinding;


public class PlaygroundActivity extends AppCompatActivity {

    private int WAIT_FOR_SECONDS = 2000;

    private ActivityPlayBinding binding;

    private Stack stack;

    private String syncToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        stack = BaseApp.getStack();
        syncToken = BaseApp.getSyncToken();
    }



    /* It initialise very first time for full sync of the data in the application from the stack */
    @SuppressLint("SetTextI18n")
    private void initiateSync(){
        new Handler().postDelayed(() -> stack.syncInit(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null){

                    int itemSize = syncStack.getItems().size();
                    String sync_token = syncStack.getSyncToken();
                    binding.tvMessage.setText("Item count: "+itemSize+"\n"+"Next Sync Token: "+sync_token);
                    binding.btnInitiate.setEnabled(false);
                    binding.tvStatus.setText("SYNC Completed");

                    if (sync_token!=null) {
                        BaseApp.storeSyncToken(sync_token);
                    }
                }
            }
        }), WAIT_FOR_SECONDS);
    }


    /**
     * This request is made to get updated data from the stack
     */
    private void deltaRequestWithSyncToken(){
        new Handler().postDelayed(() -> stack.syncWithSyncToken(syncToken, new SyncResultCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null){
                    int itemSize = syncStack.getCount();
                    binding.tvMessage.setText("Item count: "+itemSize+"\n"+"Next Sync Token: "+syncStack.getSyncToken());
                    binding.tvStatus.setText("SYNC Completed");
                }
            }
        }), WAIT_FOR_SECONDS);

    }



    // Click Handler for Delta Button
    @SuppressLint("SetTextI18n")
    public void onDeltaClick(View view) {

        if (syncToken != null){

            binding.tvStatus.setText("Loading...");
            deltaRequestWithSyncToken();
        }else {

            binding.tvStatus.setText("You have not initialize sync call, initialise sync first");
        }

    }


    // Click handler for Initial click
    @SuppressLint("SetTextI18n")
    public void onInitialClick(View view) {

        binding.tvStatus.setText("Loading...");
        initiateSync();
    }



}



