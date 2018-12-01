package ua.od.radio.pozitivefm.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.ui.about_us.AboutUsFragment;
import ua.od.radio.pozitivefm.ui.chat.ChatFragment;
import ua.od.radio.pozitivefm.ui.radio.RadioFragment;
import ua.od.radio.pozitivefm.ui.video.VideoFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private boolean isShowChat = false;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        navigation = findViewById(R.id.navigation);
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
        findViewById(R.id.chat_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.setVisibility(View.GONE);
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, ChatFragment.newInstance());
                transaction.addToBackStack("chat_fragment");
                transaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isShowChat)
            navigation.setVisibility(View.VISIBLE);
    }
}
