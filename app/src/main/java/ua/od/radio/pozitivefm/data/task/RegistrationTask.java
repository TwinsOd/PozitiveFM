package ua.od.radio.pozitivefm.data.task;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.RegistrationModel;
import ua.od.radio.pozitivefm.data.net.RestApi;

public class RegistrationTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private DataCallback callback;
    private RegistrationModel registrationModel;

    public RegistrationTask(RestApi restApi, Handler uiHandler, DataCallback callback, RegistrationModel registrationModel) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.callback = callback;
        this.registrationModel = registrationModel;
    }

    @Override
    public void run() {
        boolean isSuccessful = false;

        try {
            Integer response = restApi.registration(registrationModel).execute().body();
            Log.i("RegistrationTask", "response = " + response);
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
