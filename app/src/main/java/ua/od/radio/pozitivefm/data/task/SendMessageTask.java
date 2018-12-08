package ua.od.radio.pozitivefm.data.task;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import retrofit2.Response;
import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.model.MessageResponse;
import ua.od.radio.pozitivefm.data.net.RestApi;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;

public class SendMessageTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private ResponseCallback callback;
    private String name;
    private String message;
    private Context context;

    public SendMessageTask(RestApi restApi, Handler uiHandler, Context context, ResponseCallback callback, String name, String message) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.context = context;
        this.name = "TwinsOD";
//        this.name = name;
        this.message = message;
        this.callback = callback;
    }

    @Override
    public void run() {
        boolean isSuccessful = false;
        SharedPreferencesManager manager = new SharedPreferencesManager(context);
        String cookieBody = manager.getCookieBody();
        Log.i("SendMessageTask", "cookieBody = " + cookieBody);
        try {
//            Integer response = restApi.sendMessage(name, message).execute().body();
            MessageResponse messageResponse = null;
            Response response = restApi.sendMessage(cookieBody, name, message).execute();
            if (response.body() instanceof MessageResponse) {
                messageResponse = (MessageResponse) response.body();
            }
            Log.i("SendMessageTask", "response.code = " + response.code());
            if (response.code() == 200 && messageResponse != null && messageResponse.getShouts().size() != 0) {
                Log.i("SendMessageTask", "me.getShouts().size() = " + messageResponse.getShouts().size());
                if (messageResponse.getShouts() != null)
                    for (String str : messageResponse.getShouts()) {
                        Log.i("SendMessageTask", "str = " + str);
                    }
                isSuccessful = true;
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
                callback.isSuccessful();
            else
                callback.isFailed();
        }
    }
}
