package ua.od.radio.pozitivefm.ui.radio;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.Constants;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.TrackModel;

import static ua.od.radio.pozitivefm.Constants.TRACK_INTENT;

public class RadioFragment extends Fragment {
    private TrackAdapter adapter;
    private BroadcastReceiver receiver;
    private TrackModel currentTrackModel;
    private TextView currentTrackView;

    public RadioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        //init track list
        final RecyclerView recyclerView = view.findViewById(R.id.track_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new TrackAdapter();
        recyclerView.setAdapter(adapter);
        currentTrackView = view.findViewById(R.id.current_track_view);
        loadTrack();

        FloatingMusicActionButton playerView = view.findViewById(R.id.fab_play_view);
        App.getRepository().initPlayer(playerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TrackModel model = new TrackModel();
                model.setAuthor(intent.getStringExtra(Constants.AUTHOR));
                model.setTitle(intent.getStringExtra(Constants.TITLE));
                model.setDj(intent.getStringExtra(Constants.DJ));
                model.setTs(intent.getLongExtra(Constants.TS, 0));
                addLastItemToList(model);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(TRACK_INTENT);
        getActivity().registerReceiver(receiver, filter);

    }

    private void loadTrack() {
        App.getRepository().getTrackList(new DataCallback<List<TrackModel>>() {
            @Override
            public void onEmit(List<TrackModel> data) {
                currentTrackModel = data.get(0);
                currentTrackView.setText(String.format("%s - %s",
                        currentTrackModel.getAuthor(), currentTrackModel.getTitle()));
                data.remove(0);
                adapter.setList(data);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "Error loading tracks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addLastItemToList(TrackModel model) {
        if (currentTrackModel.getTs() == model.getTs())
            return;
        adapter.addItem(currentTrackModel);
        currentTrackModel = model;
        currentTrackView.setText(String.format("%s - %s",
                currentTrackModel.getAuthor(), currentTrackModel.getTitle()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (receiver != null && getActivity() != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onDestroy() {
        App.getRepository().disablePlayer();
        super.onDestroy();
    }
}
