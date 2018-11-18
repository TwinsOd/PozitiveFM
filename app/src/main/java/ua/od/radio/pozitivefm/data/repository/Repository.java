package ua.od.radio.pozitivefm.data.repository;

import java.util.List;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.TrackModel;

public interface Repository {
    void getTrackList(DataCallback<List<TrackModel>> callback);

    void playRadio();

    void stopRadio();

    void pauseRadio();

    void initPlayer(FloatingMusicActionButton playerView);

    void disablePlayer();
}
