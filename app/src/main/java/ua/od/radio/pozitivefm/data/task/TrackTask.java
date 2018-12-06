package ua.od.radio.pozitivefm.data.task;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.TrackModel;
import ua.od.radio.pozitivefm.data.net.RestApi;

public class TrackTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private DataCallback<List<TrackModel>> callback;

    public TrackTask(RestApi restApi, Handler uiHandler, DataCallback<List<TrackModel>> callback) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.callback = callback;
    }

    @Override
    public void run() {
        ArrayList<TrackModel> list;
        try {
            List<JsonElement> response = restApi.getTracks().execute().body();
            if (response != null) {
                Type listType = new TypeToken<ArrayList<TrackModel>>() {
                }.getType();
                list = (new Gson()).fromJson(response.get(1), listType);
                if (list != null) {
                    List<TrackModel> newList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getAuthor() != null && list.get(i).getAuthor().length() > 0
                                && list.get(i).getTitle() != null && list.get(i).getTitle().length() > 0)
                            newList.add(list.get(i));
                    }
                    uiHandler.post(new CallbackToUI(newList));
                }
            } else
                Log.i("TrackTask", "response is null");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CallbackToUI implements Runnable {

        private final List<TrackModel> list;

        CallbackToUI(List<TrackModel> list) {
            this.list = list;
        }

        @Override
        public void run() {
            Log.i("EventTask", "run: postModels callback to ui");
            callback.onEmit(list);
            callback.onCompleted();
        }
    }
}
