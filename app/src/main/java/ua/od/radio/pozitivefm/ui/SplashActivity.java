package ua.od.radio.pozitivefm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.data.model.SettingsAppModel;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference settingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        settingsRef = database.getReference("settings");
//        settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
        settingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SettingsAppModel model = new SettingsAppModel(
                        dataSnapshot.child("menu").getValue(Integer.class),
                        dataSnapshot.child("video").getValue(Integer.class));
                Log.i("SplashActivity", "menu is: " + model.getMenu());
                Log.i("SplashActivity", "video is: " + model.getVideo());
                App.getRepository().setSettingsApp(model);
                showMainActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.i("SplashActivity_Failed", error.toException().getMessage());
                showMainActivity();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        settingsRef.keepSynced(false);
        settingsRef = null;
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
