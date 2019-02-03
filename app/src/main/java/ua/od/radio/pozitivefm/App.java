package ua.od.radio.pozitivefm;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import ua.od.radio.pozitivefm.data.repository.Repository;
import ua.od.radio.pozitivefm.data.repository.RepositoryImpl;

public class App extends Application {
    private static Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        repository = new RepositoryImpl(this);
    }

    public static Repository getRepository() {
        return repository;
    }
}
