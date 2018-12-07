package ua.od.radio.pozitivefm.data.task;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import retrofit2.Response;
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
            Response response = restApi.registration(
                    registrationModel.getUsername(),
                    registrationModel.getDob(),
                    registrationModel.getFloor(),
                    registrationModel.getEmail(),
                    registrationModel.getFamily_status(),
                    registrationModel.getPassword()
            ).execute();

            Integer responseAuth = null;
            if (response.body() instanceof Integer) {
                responseAuth = (Integer) response.body();
            }
            Log.i("RegistrationTask", "response.body = " + response.body());
            Log.i("RegistrationTask", "response.code = " + response.code());
            if (responseAuth != null && responseAuth == 1 || response.code() == 200)
                isSuccessful = true;
            else {
                Log.i("RegistrationTask", "response.errorBody() == " + response.errorBody());
            }
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
