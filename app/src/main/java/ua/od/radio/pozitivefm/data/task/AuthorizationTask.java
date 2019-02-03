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
import ua.od.radio.pozitivefm.data.net.RestApi;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;

public class AuthorizationTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private DataCallback callback;
    private String login;
    private String password;
    private Context context;

    public AuthorizationTask(RestApi restApi, Handler uiHandler, Context context, DataCallback callback, String login, String password) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.context = context;
        this.callback = callback;
        this.login = login;
        this.password = password;
    }

    @Override
    public void run() {
        boolean isSuccessful = false;
        try {
//            Integer response = restApi.authorization(login, password).execute().body();
            Response response = restApi.authorization(login, password).execute();
            Integer responseValue = null;
            if (response.body() instanceof Integer) {
                responseValue = (Integer) response.body();
            }
            if (responseValue != null && responseValue == 1 && response.headers() != null) {
                String cookieBody = headerToCookie(response.headers());
                Log.i("AuthorizationTask", "cookieBody = " + cookieBody);
                Log.i("AuthorizationTask", "responseValue = " + responseValue);
                SharedPreferencesManager manager = new SharedPreferencesManager(context);
                manager.saveCookieBody(cookieBody);
                isSuccessful = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            isSuccessful = false;
        }
        uiHandler.post(new CallbackToUI(isSuccessful));
    }

    public static String headerToCookie(Headers headers) {
        List<String> array = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).equals("Set-Cookie")) {
                Log.i("AuthorizationTask", "headers. value " + headers.value(i));
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
