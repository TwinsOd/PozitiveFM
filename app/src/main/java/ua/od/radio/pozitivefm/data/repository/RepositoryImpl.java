package ua.od.radio.pozitivefm.data.repository;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;

import java.util.List;
import java.util.concurrent.ExecutorService;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.executor.JobExecutor;
import ua.od.radio.pozitivefm.data.model.ChatModel;
import ua.od.radio.pozitivefm.data.model.RegistrationModel;
import ua.od.radio.pozitivefm.data.model.TrackModel;
import ua.od.radio.pozitivefm.data.net.RestApi;
import ua.od.radio.pozitivefm.data.net.RestModule;
import ua.od.radio.pozitivefm.data.service.PlayerService;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;
import ua.od.radio.pozitivefm.data.task.AuthorizationTask;
import ua.od.radio.pozitivefm.data.task.FullChatTask;
import ua.od.radio.pozitivefm.data.task.RegistrationTask;
import ua.od.radio.pozitivefm.data.task.SendMessageTask;
import ua.od.radio.pozitivefm.data.task.TrackTask;

public class RepositoryImpl implements Repository {
    private final Handler uiHandler;
    private final ExecutorService executorService;
    private final RestApi restApi;
    private Context context;
    //radio
    private boolean isPlaying = false;
    private MediaControllerCompat mediaController = null;
    private MediaControllerCompat.Callback callback = null;
    private ServiceConnection serviceConnection = null;
    private PlayerService.PlayerServiceBinder playerServiceBinder = null;

    public RepositoryImpl(@NonNull Context context) {
        this.context = context;
        restApi = new RestModule(context).provideRestApi();
        JobExecutor jobExecutor = new JobExecutor();
        executorService = jobExecutor.getThreadPoolExecutor();
        uiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void getTrackList(DataCallback<List<TrackModel>> callback) {
        executorService.execute(new TrackTask(restApi, uiHandler, callback ));
    }

    @Override
    public void getFullMessage(DataCallback<List<ChatModel>> callback) {
        executorService.execute(new FullChatTask(callback, uiHandler));
    }

    @Override
    public void authorization(String login, String password, DataCallback callback) {
        executorService.execute(new AuthorizationTask(restApi, uiHandler, context, callback, login, password));
    }

    @Override
    public void registration(RegistrationModel registrationModel, DataCallback callback) {
        executorService.execute(new RegistrationTask(restApi, uiHandler, callback, registrationModel));
    }

    @Override
    public void sendMessage(String message, ResponseCallback callback) {
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(context);
        executorService.execute(new SendMessageTask(restApi, uiHandler, context, callback, preferencesManager.getLogin(), message));
    }

    @Override
    public void initPlayer(final FloatingMusicActionButton playerView) {
        callback = new MediaControllerCompat.Callback() {
            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {
                super.onPlaybackStateChanged(state);
                if (state == null || playerView == null)
                    return;

                boolean playing = state.getState() == PlaybackStateCompat.STATE_PLAYING;
                if (playing)
                    playerView.changeMode(FloatingMusicActionButton.Mode.STOP_TO_PLAY);
                else
                    playerView.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_STOP);
            }
        };
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                playerServiceBinder = (PlayerService.PlayerServiceBinder) iBinder;
                try {
                    mediaController = new MediaControllerCompat(context, playerServiceBinder.getMediaSessionToken());
                    mediaController.registerCallback(callback);
                    callback.onPlaybackStateChanged(mediaController.getPlaybackState());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    mediaController = null;
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                playerServiceBinder = null;
                if (mediaController != null) {
                    mediaController.unregisterCallback(callback);
                    mediaController = null;
                }
            }
        };
        context.bindService(new Intent(context, PlayerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
                    mediaController.getTransportControls().play();
                    isPlaying = true;
                } else {
                    mediaController.getTransportControls().stop();
                    isPlaying = false;
                }
            }
        });
    }

    @Override
    public void disablePlayer() {
        playerServiceBinder = null;
        if (mediaController != null) {
            mediaController.unregisterCallback(callback);
            mediaController = null;
        }
        if (serviceConnection != null)
            context.unbindService(serviceConnection);
    }
}
