package ua.od.radio.pozitivefm.data.task;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.SettingsModel;
import ua.od.radio.pozitivefm.data.net.RestApi;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;

public class UpdateTask implements Runnable {
    private RestApi restApi;
    private Handler uiHandler;
    private DataCallback<List<SettingsModel>> callback;
    private Context context;

    public UpdateTask(RestApi restApi, Handler uiHandler, DataCallback<List<SettingsModel>> callback, Context context) {
        this.restApi = restApi;
        this.uiHandler = uiHandler;
        this.callback = callback;
        this.context = context;
    }

    @Override
    public void run() {
        SharedPreferencesManager manager = new SharedPreferencesManager(context);
        String cookieBody = manager.getCookieBody();
//        try {
//            Response response = restApi.update(cookieBody).execute();

        Call<List<SettingsModel>> call = restApi.update(cookieBody);
        call.enqueue(new Callback<List<SettingsModel>>() {
            @Override
            public void onResponse(Call<List<SettingsModel>> call, Response<List<SettingsModel>> response) {
                List<SettingsModel> settingsModel;
                if (response.body() != null) {
                    Log.i("UpdateTask", response.body().toString());
                    Log.i("UpdateTask", response.errorBody().toString());
                    settingsModel = (List<SettingsModel>) response.body();
                    if (settingsModel != null)
                        Log.i("UpdateTask", "size  = " + settingsModel.size());
                    settingsModel.size();
                } else
                    Log.i("UpdateTask", "response.body() == null");
            }

            @Override
            public void onFailure(Call<List<SettingsModel>> call, Throwable t) {
                Log.i("UpdateTask", "error " + t.getMessage());


            }
        });


//            if (response.body() instanceof List<SettingsModel>) {
//                settingsModel = (List<SettingsModel>) response.body();
//            }
//
//            if (settingsModel != null && settingsModel.){
//
//            }
//
//            if (response.body() != null) {
//                Log.i("UpdateTask", response.body().toString());
//                Log.i("UpdateTask", response.errorBody().toString());
//                settingsModel = (List<SettingsModel>) response.body();
//                if (settingsModel != null)
//                    Log.i("UpdateTask", "size  = " + settingsModel.size());
//            } else
//                Log.i("UpdateTask", "response.body() == null");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
