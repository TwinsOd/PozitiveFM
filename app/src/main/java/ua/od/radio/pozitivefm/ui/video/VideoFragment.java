package ua.od.radio.pozitivefm.ui.video;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.List;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.ChatModel;
import ua.od.radio.pozitivefm.ui.chat.ChatAdapter;
import ua.od.radio.pozitivefm.ui.chat.ChatFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private ChatAdapter adapter;
    private RecyclerView recyclerView;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        switch (App.getRepository().getSettingsApp().getVideo()) {
            case 1:
                loadWebView(view, "https://goodgame.ru/player?152500");
                break;
            case 2:
                loadWebView(view, "https://player.twitch.tv/?channel=pozitivfm");
                break;
            case 3:
                loadYoutube();
                break;
            default:
                loadWebView(view, "https://goodgame.ru/player?152500");
        }


        recyclerView = view.findViewById(R.id.chat_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.show();
        App.getRepository().getFullMessage(new DataCallback<List<ChatModel>>() {
            @Override
            public void onEmit(List<ChatModel> data) {
                Log.i("ChatFragment", "data.size " + data.size());
                adapter.setList(data.subList(0, 9));
                progressDialog.cancel();
            }

            @Override
            public void onCompleted() {
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onError(Throwable throwable) {
                progressDialog.cancel();
            }
        });

        return view;
    }

    private void loadChat() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.chat_content, ChatFragment.newInstance());
        ft.commit();
    }

    private void loadWebView(View view, String url) {
        WebView webView = new WebView(view.getContext());
        Log.i("VideoFragment", "width = " + getWidth());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = getWidth();
        params.height = getWidth() / 10 * 8;
        webView.setLayoutParams(params);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDatabasePath(getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
        LinearLayout layout = view.findViewById(R.id.player_content);
        layout.addView(webView);
    }

    private int getWidth() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    private void loadPlayer(View view, String url) {
        VideoView myVideoView = new VideoView(view.getContext());
        myVideoView.setVideoPath(url);
        myVideoView.setMediaController(new MediaController(view.getContext()));
        LinearLayout layout = view.findViewById(R.id.player_content);
        layout.addView(myVideoView);
    }

    private void loadPlayerFull(View view, String url) {
        final ProgressDialog pDialog;
        final VideoView videoview = new VideoView(view.getContext());

        // Create a progressbar
        pDialog = new ProgressDialog(view.getContext());
        // Set progressbar title
        pDialog.setTitle("Android Video Streaming Tutorial");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    view.getContext());
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });
        LinearLayout layout = view.findViewById(R.id.player_content);
        layout.addView(videoview);
    }

    private void loadYoutube() {
        final String API_KEY = "AIzaSyDWdz76IuzaT_OPNZGfya_A2yUIjFCuM-Q";

        //https://www.youtube.com/watch?v=<VIDEO_ID>
        //https://www.youtube.com/watch?v=KRIUFyo9nYg
        final String VIDEO_ID = "KRIUFyo9nYg";
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
