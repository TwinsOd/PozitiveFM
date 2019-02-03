package ua.od.radio.pozitivefm.data.task;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private int countUpdate = 0;

    public SendMessageTask(RestApi restApi, Handler uiHandler, Context context, ResponseCallback callback, String name, String message) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.context = context;
        this.name = name;
        this.message = message;
        this.callback = callback;
    }

    @Override
    public void run() {
        SharedPreferencesManager manager = new SharedPreferencesManager(context);
        String cookieBody = manager.getCookieBody();
        Log.i("SendMessageTask", "cookieBody = " + cookieBody);
        if (checkCookie(cookieBody)) {
            Log.i("SendMessageTask", "check true ");
            String newCookie = updateCookie(manager);
            if (newCookie == null)
                uiHandler.post(new CallbackToUI(false));
            else
                sendMessage(newCookie);
        } else {
            Log.i("SendMessageTask", "check false ");
            sendMessage(cookieBody);
        }
    }

    private String updateCookie(SharedPreferencesManager manager) {
        Log.i("AuthorizationTask", "updateCookie: starting ");

        countUpdate++;
        try {
            Response response = restApi.authorization(manager.getLogin(), manager.getPassword()).execute();
            Integer responseValue = null;
            if (response.body() instanceof Integer) {
                responseValue = (Integer) response.body();
            }
            Log.i("AuthorizationTask", "updateCookie: response " + response);
            Log.i("AuthorizationTask", "updateCookie: responseValue " + responseValue);
            if (responseValue != null && responseValue == 1 && response.headers() != null) {
                String newCookieBody = AuthorizationTask.headerToCookie(response.headers());
                Log.i("AuthorizationTask", "newCookieBody = " + newCookieBody);
                Log.i("AuthorizationTask", "responseValue = " + responseValue);
                manager.saveCookieBody(newCookieBody);
                return newCookieBody;
            } else
                uiHandler.post(new CallbackToUI(false));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private void sendMessage(String cookieBody) {
        boolean isSuccessful = false;
        try {
            MessageResponse messageResponse = null;
            Response response = restApi.sendMessage(cookieBody, name, message).execute();
            if (response.body() instanceof MessageResponse) {
                messageResponse = (MessageResponse) response.body();
            }
            Log.i("SendMessageTask", "response.code = " + response.code());
            Log.i("SendMessageTask", "response = " + response.toString());
            Log.i("SendMessageTask", "messageResponse = " + messageResponse);
            if (messageResponse != null) {
                Log.i("SendMessageTask", "messageResponse = " + messageResponse.getShouts());
                //Log.i("SendMessageTask", "messageResponse = " + messageResponse.getShouts().size());

            }
            //joomla_remember_me_9e7af3a713354dee3d46fd357c64914b=ehfPxZgHfbHr4U05.x3LLJQg41aP831tUwBHi; expires=Thu, 04-Apr-2019 11:50:11 GMT; path=/; httponly
            if (response.code() == 200 && messageResponse != null &&
                    messageResponse.getShouts() != null && messageResponse.getShouts().size() != 0) {
                Log.i("SendMessageTask", "me.getShouts().size() = " + messageResponse.getShouts().size());
                if (messageResponse.getShouts() != null)
                    for (String str : messageResponse.getShouts()) {
                        Log.i("SendMessageTask", "str = " + str);
                    }
                isSuccessful = true;
            } else if (response.code() == 200 && countUpdate < 3 &&
                    (messageResponse == null || messageResponse.getShouts() == null)) {
                Log.i("SendMessageTask", " (response.code() == 200 && messageResponse == null && countUpdate < 3) ");
                updateCookie(new SharedPreferencesManager(context));
            } else {
                Log.i("SendMessageTask", " else ");

                uiHandler.post(new CallbackToUI(false));
            }
        } catch (IOException e) {
            e.printStackTrace();
            isSuccessful = false;
        }
        uiHandler.post(new CallbackToUI(isSuccessful));
    }

    //return true if need to update cookie
    private boolean checkCookie(String cookieBody) {
        if (cookieBody == null || cookieBody.length() < 21)
            return true;
        int time = cookieBody.indexOf("GMT;");
        String dateStr = cookieBody.substring(time - 21, time - 10);
        Log.i("SendMessageTask", "date = _" + dateStr + "_");
        Date dateFinish = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            dateFinish = simpleDateFormat.parse(dateStr);
            Log.i("SendMessageTask", "dateFinish = _" + dateFinish.toString() + "_");
            Log.i("SendMessageTask", "getTime = _" + dateFinish.getTime() + "_");

        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("SendMessageTask", "error = _" + e.getMessage() + "_");
        }

        return dateFinish == null || dateFinish.getTime() < Calendar.getInstance().getTimeInMillis();
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
