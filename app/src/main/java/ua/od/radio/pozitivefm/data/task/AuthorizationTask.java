package ua.od.radio.pozitivefm.data.task;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.net.RestApi;

public class AuthorizationTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private DataCallback callback;
    private String login;
    private String password;

    public AuthorizationTask(RestApi restApi, Handler uiHandler, DataCallback callback, String login, String password) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.callback = callback;
        this.login = login;
        this.password = password;
    }

    @Override
    public void run() {
        boolean isSuccessful = false;

        try {
            Integer response = restApi.authorization(login, password).execute().body();
            Log.i("AuthorizationTask", "response = " + response);
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
                callback.onCompleted();
            else
                callback.onError(new Throwable());
        }
    }
}
