package ua.od.radio.pozitivefm.data.net;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import ua.od.radio.pozitivefm.data.model.SettingsModel;

/**
 * Created by Twins on 14.09.2016.
 */

public interface RestApi {

    @GET("/v0/b/meeter-b58ce.appspot.com/o/settings.json?alt=media&token=637ae08e-f777-4eb5-a82e-dc9d4efa94ac")
    Call<SettingsModel> getSettings();

    @GET("radio_scripts/site/online.php")
    Call<Map<Integer, List<Object>>> getTracks();
}
