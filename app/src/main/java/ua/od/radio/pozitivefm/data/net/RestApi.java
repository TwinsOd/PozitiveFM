package ua.od.radio.pozitivefm.data.net;

import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ua.od.radio.pozitivefm.data.model.SettingsModel;

/**
 * Created by Twins on 14.09.2016.
 */

public interface RestApi {

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @GET("index.php?option=com_smartshoutbox&task=getshouts")
    Call<SettingsModel> update();

    @GET("radio_scripts/site/online.php")
    Call<List<JsonElement>> getTracks();

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @GET("index.php?option=com_soajax&controller=ajax&task=registerajax")
    Call<Integer> registration(
            @Query("username") String username,
            @Query("dob") String dob,//1995-01-01
            @Query("floor") String floor,//Мужской
            @Query("email") String email,
            @Query("family_status") String family_status,//Не женат
            @Query("password") String password);
    //Cookie: _ga=GA1.2.634654501.1538489876; joomla_user_state=logged_in; button-sounds=1; _gid=GA1.2.522474203.1543861105; _gat_gtag_UA_110938666_1=1; e58483f46a435e3dafb1b3b4e75abdc6=pd88fhqq08a5plg78sp3dahck2; joomla_remember_me_3e92b5696df48f70f5cb2816370e3ceb=Dgm7qHzbL6UNpJMS.XwBaZ1Arfv6QyE8z5B6E

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @GET("index.php?option=com_soajax&controller=ajax&task=userloginajax")
    Call<Integer> authorization(
            @Query("login") String username,
            @Query("password") String password);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @GET("index.php?option=com_smartshoutbox&task=addshout")
    Call<Integer> sendMessage(
//            @Query("shoutcategory") String shoutcategory,//0
            @Query("name") String name,
            @Query("message") String message
//            @Query("files") String files,
//            @Query("sid") String sid//0
    );
}
