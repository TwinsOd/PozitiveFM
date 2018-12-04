package ua.od.radio.pozitivefm.data.task;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.net.RestApi;

public class SendMessageTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private ResponseCallback callback;
    private String name;
    private String message;

    public SendMessageTask(RestApi restApi, Handler uiHandler, ResponseCallback callback, String name, String message) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.name = name;
        this.message = message;
        this.callback = callback;
    }

    @Override
    public void run() {
        boolean isSuccessful = false;
        try {
            Integer response = restApi.sendMessage(name, message).execute().body();
            Log.i("SendMessageTask", "response = " + response);
            if (response != null && response == 1)
                isSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccessful = false;
        }

        uiHandler.post(new CallbackToUI(isSuccessful));
    }

    private class CallbackToUI implements Runnable {

        private final boolean isSuccessful;

        CallbackToUI(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        @Override
        public void run() {
            Log.i("EventTask", "run: postModels callback to ui");
            if (isSuccessful)
                callback.isSuccessful();
            else
                callback.isFailed();
        }
    }
}
