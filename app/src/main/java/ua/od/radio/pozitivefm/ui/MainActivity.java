package ua.od.radio.pozitivefm.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.ui.about_us.AboutUsFragment;
import ua.od.radio.pozitivefm.ui.radio.RadioFragment;
import ua.od.radio.pozitivefm.ui.video.VideoFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_radio:
                        fragment = new RadioFragment();
                        break;
                    case R.id.navigation_video:
                        fragment = new VideoFragment();
                        break;
                    case R.id.navigation_about_as:
                        fragment = new AboutUsFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
        navigation.setSelectedItemId(R.id.navigation_radio);
//        navigation.setSelectedItemId(R.id.navigation_video);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    SettingsModel response = new RestModule(context)
//                            .provideFirebaseRestApi()
//                            .getSettings()
//                            .execute()
//                            .body();
//                    if (response != null){
//                        Log.i("MainActivity", "response.getUrl:" + response.getUrl());
//                        Log.i("MainActivity","response.getActiveRadio:" +  response.getActiveRadio());
//                    }else
//                        Log.i("MainActivity", "response == null" );
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.i("Error:", e.getMessage());
//                }
//            }
//        }).start();
    }

}
