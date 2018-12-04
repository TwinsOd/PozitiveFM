package ua.od.radio.pozitivefm.data.repository;

import java.util.List;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.model.ChatModel;
import ua.od.radio.pozitivefm.data.model.RegistrationModel;
import ua.od.radio.pozitivefm.data.model.TrackModel;

public interface Repository {
    void getTrackList(DataCallback<List<TrackModel>> callback);
    void getFullMessage(DataCallback<List<ChatModel>> callback);
    void authorization(String login, String password, DataCallback callback);

    void registration(RegistrationModel registrationModel, DataCallback callback);
    void sendMessage(String message, ResponseCallback callback);

    void initPlayer(FloatingMusicActionButton playerView);
    void disablePlayer();
}
