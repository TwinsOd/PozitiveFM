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
import android.widget.RelativeLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import ua.od.radio.pozitivefm.App;
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
    private RelativeLayout topView;
    //    private BroadcastReceiver receiver;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        fragmentManager = getFragmentManager();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "menu");

                switch (item.getItemId()) {
                    case R.id.navigation_radio:
                        fragment = new RadioFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "radio");
                        break;
                    case R.id.navigation_video:
                        fragment = new VideoFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "video");
                        break;
                    case R.id.navigation_about_as:
                        fragment = new AboutUsFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "about_as");
                        break;
                }
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

        findViewById(R.id.chat_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.setVisibility(View.GONE);
                topView.setVisibility(View.GONE);
                isShowChat = true;
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, ChatFragment.newInstance());
                transaction.addToBackStack("chat_fragment");
                transaction.commit();
            }
        });
        topView = findViewById(R.id.top_bar);

        switch (App.getRepository().getSettingsApp().getMenu()) {
            case 1:
                navigation.setSelectedItemId(R.id.navigation_radio);
                break;
            case 2:
                navigation.setSelectedItemId(R.id.navigation_video);
                break;
            default:
                navigation.setSelectedItemId(R.id.navigation_radio);
        }
    }

    @Override
    public void onBackPressed() {
        if (isShowChat) {
            navigation.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
        }
        super.onBackPressed();
    }
}
