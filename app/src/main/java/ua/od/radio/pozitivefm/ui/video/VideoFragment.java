package ua.od.radio.pozitivefm.ui.video;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

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
        WebView webView = view.findViewById(R.id.plaer_web_view);
        webView.loadUrl("https://goodgame.ru/channel/PozitivFM");
        return view;
    }

}
