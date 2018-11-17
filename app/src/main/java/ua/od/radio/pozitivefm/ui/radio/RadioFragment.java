package ua.od.radio.pozitivefm.ui.radio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.model.TrackModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioFragment extends Fragment {


    public RadioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.track_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<TrackModel> list = new ArrayList<>();
        TrackModel model = new TrackModel();
        model.setAuthor("Katy Perry");
        model.setTitle("Hot N Cold");
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        list.add(model);
        TrackAdapter adapter = new TrackAdapter(list);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
