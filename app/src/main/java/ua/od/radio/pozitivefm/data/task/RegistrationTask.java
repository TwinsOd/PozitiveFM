package ua.od.radio.pozitivefm.data.task;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Response;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.RegistrationModel;
import ua.od.radio.pozitivefm.data.net.RestApi;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;

public class RegistrationTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private DataCallback callback;
    private RegistrationModel registrationModel;
    private Context context;

    public RegistrationTask(RestApi restApi, Handler uiHandler, Context context, DataCallback callback, RegistrationModel registrationModel) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.callback = callback;
        this.context = context;
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
                Log.i("RegistrationTask", "responseAuth = " + responseAuth);
            }
            Log.i("RegistrationTask", "response.body = " + response.body());
            Log.i("RegistrationTask", "response.code = " + response.code());
            if (responseAuth != null && responseAuth == 1 && response.code() == 200) {
                String cookieBody = headerToCookie(response.headers());
                SharedPreferencesManager manager = new SharedPreferencesManager(context);
                manager.saveCookieBody(cookieBody);
                Log.i("RegistrationTask", "cookieBody = " + cookieBody);
                isSuccessful = true;
            } else {
                Log.i("RegistrationTask", "response.errorBody() == " + response.errorBody());
            }
        } catch (IOException e) {
            e.printStackTrace();
            isSuccessful = false;
        }

        uiHandler.post(new CallbackToUI(isSuccessful));
    }

    private String headerToCookie(Headers headers) {
        List<String> array = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).equals("Set-Cookie")) {
                Log.i("RegistrationTask", "headers. value " + headers.value(i));
                array.add(headers.value(i));
            }

        }
        if (array.size() == 1)
            return array.get(0);

        StringBuilder cookies = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            cookies.append(array.get(i)
                    .replace("path=/;", "")
                    .replace("httponly", "")
                    .replace("HttpOnly", "")
            );
        }
        return cookies.toString();
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
