package sync.contentstack.playground;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.contentstack.sdk.Error;
import com.contentstack.sdk.Stack;
import com.contentstack.sdk.SyncResultCallBack;
import com.contentstack.sdk.SyncStack;
import sync.contentstack.playground.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    // Holds for seconds for clear output
    private final int WAIT_FOR_SECONDS = 2000;
    // UI binding
    private ActivityMainBinding binding;
    // Stack instance
    private Stack stack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        stack = BaseApp.getStack();
    }

    // Click handler for Initial click
    public void onInitialClick(View view) {
        binding.container.tvStatus.setText("Loading...");
        initiateSync();
    }

    // Click handler for Subsequent click
    public void onSubsequentSync(View view){
        binding.container.tvStatus.setText("Loading...");
        subsquentsync();
    }

    /*
    It initialise very first time for full sync of the
    data in the application from the stack */
    private void initiateSync(){
        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    showData(syncStack);
                }
            }
        });
    }



    /**
     * This request is made to get updated data from the stack
     */
    private void subsequentSync(String syncToken){
        stack.syncToken(syncToken, new SyncResultCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    showData(syncStack);
                }
            }
        });
    }



    // Click Handler for Delta Button
    @SuppressLint("SetTextI18n")
    public void subsquentsync() {
        String syncToken = BaseApp.getSyncToken();
        if (syncToken != null){
            subsequentSync(syncToken);
        }else {
            binding.container.tvStatus.setText("You have not initialize sync call, initialise sync first");
        }

    }



    @SuppressLint("SetTextI18n")
    private void showData(SyncStack syncStack){
        int itemSize = syncStack.getCount();
        int items = syncStack.getItems().size();
        String syncToken = syncStack.getSyncToken();
        String message = null;
        if (syncToken!=null){
            message = "Item count: "+itemSize+"\n"+"Next Sync Token: "+syncToken;
            binding.container.messageString.setText(message);
            BaseApp.storeSyncToken(syncToken);
        }
        binding.container.tvStatus.setText("SYNC Completed");
    }


}
