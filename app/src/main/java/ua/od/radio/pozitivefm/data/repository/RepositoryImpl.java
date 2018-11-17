package ua.od.radio.pozitivefm.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;

import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.executor.JobExecutor;
import ua.od.radio.pozitivefm.data.model.TrackModel;
import ua.od.radio.pozitivefm.data.net.RestApi;
import ua.od.radio.pozitivefm.data.net.RestModule;

public class RepositoryImpl implements Repository {
    private final Handler uiHandler;
    private final ExecutorService executorService;
    private final RestApi restApi;


    public RepositoryImpl(@NonNull Context context) {
//        this.context = context;
        restApi = new RestModule(context).provideRestApi();
        JobExecutor jobExecutor = new JobExecutor();
        executorService = jobExecutor.getThreadPoolExecutor();
        uiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void getTrackList(DataCallback<List<TrackModel>> callback) {

    }
}
