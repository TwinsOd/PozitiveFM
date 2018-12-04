package ua.od.radio.pozitivefm.data.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.APP_PREFERENCES;
import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.DATE_BIRTHDAY;
import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.EMAIL;
import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.FAMILY_STATUS;
import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.FLOOR;
import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.IS_AUTHORIZATION;
import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.LOGIN;
import static ua.od.radio.pozitivefm.data.shared_preferences.SharedPrefConst.PASSWORD;


public class SharedPreferencesManager {

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesManager(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setAuthorization(boolean isAuth) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_AUTHORIZATION, isAuth);
        editor.apply();
    }

    public boolean isAuthorization() {
        return sharedPreferences.getBoolean(IS_AUTHORIZATION, false);
    }

    public void saveLogin(String login) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN, login);
        editor.apply();
    }

    public String getLogin() {
        return sharedPreferences.getString(LOGIN, "");
    }

    public void saveDateBirthday(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DATE_BIRTHDAY, value);
        editor.apply();
    }

    public String getDateBirthday() {
        return sharedPreferences.getString(DATE_BIRTHDAY, "");
    }

    public void saveFamilyStatus(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAMILY_STATUS, value);
        editor.apply();
    }

    public String getFamilyStatus() {
        return sharedPreferences.getString(FAMILY_STATUS, "");
    }

    public void savePassword(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, value);
        editor.apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void saveEmail(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, value);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void saveFloor(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FLOOR, value);
        editor.apply();
    }

    public String getFloor() {
        return sharedPreferences.getString(FLOOR, "");
    }
}
