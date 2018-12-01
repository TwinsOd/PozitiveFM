package ua.od.radio.pozitivefm.ui.radio;


import android.app.Fragment;
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
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.TrackModel;

public class RadioFragment extends Fragment {
    private TrackAdapter adapter;

    public RadioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.track_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new TrackAdapter();
        recyclerView.setAdapter(adapter);
        final TextView currentTrackView = view.findViewById(R.id.current_track_view);
        App.getRepository().getTrackList(new DataCallback<List<TrackModel>>() {
            @Override
            public void onEmit(List<TrackModel> data) {
                TrackModel currentTrackModel = data.get(0);
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
        FloatingMusicActionButton playerView = view.findViewById(R.id.fab_play_view);
        App.getRepository().initPlayer(playerView);
        return view;
    }

    @Override
    public void onDestroy() {
        App.getRepository().disablePlayer();
        super.onDestroy();
    }
}
