package ua.od.radio.pozitivefm.data.repository;

import java.util.List;

import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.TrackModel;

public interface Repository {
    void getTrackList(DataCallback<List<TrackModel>> callback);
}
