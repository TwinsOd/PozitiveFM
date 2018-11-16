package ua.od.radio.pozitivefm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Response;
import ua.od.radio.pozitivefm.data.model.SettingsModel;
import ua.od.radio.pozitivefm.data.net.RestModule;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        context = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SettingsModel response = new RestModule(context)
                            .provideFirebaseRestApi()
                            .getSettings()
                            .execute()
                            .body();
                    if (response != null){
                        Log.i("MainActivity", "response.getUrl:" + response.getUrl());
                        Log.i("MainActivity","response.getActiveRadio:" +  response.getActiveRadio());
                    }else
                        Log.i("MainActivity", "response == null" );
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("Error:", e.getMessage());
                }
            }
        }).start();
    }

}
