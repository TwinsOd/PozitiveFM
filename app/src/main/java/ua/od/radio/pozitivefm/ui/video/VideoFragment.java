package ua.od.radio.pozitivefm.ui.video;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import ua.od.radio.pozitivefm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        loadYoutube();
        return view;
    }

    private void loadYoutube() {
        final String API_KEY = "AIzaSyDWdz76IuzaT_OPNZGfya_A2yUIjFCuM-Q";

        //https://www.youtube.com/watch?v=<VIDEO_ID>
        //https://www.youtube.com/watch?v=KRIUFyo9nYg
        final String VIDEO_ID = "Kyck5UJFVx8";
        FragmentManager fm = getFragmentManager();
        String tag = YouTubePlayerFragment.class.getSimpleName();
        FragmentTransaction ft = fm.beginTransaction();
        YouTubePlayerFragment playerFragment = YouTubePlayerFragment.newInstance();
        ft.add(R.id.player_content, playerFragment, tag);
        ft.commit();

        playerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(VIDEO_ID);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getActivity(), "Error while initializing YouTubePlayer.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
