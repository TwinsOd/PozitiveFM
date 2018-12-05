package ua.od.radio.pozitivefm.ui.chat;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;


public class AuthorizationFragment extends DialogFragment implements View.OnClickListener {
    private boolean isAuth = true;
    private TextView authorizationType;
    private TextView registrationType;
    private LinearLayout registrationLayout;
    private Button enterView;
    private EditText loginView;
    private EditText passwordView;
    private ResponseCallback callback;

    public AuthorizationFragment() {
        // Required empty public constructor
    }

    public static AuthorizationFragment newInstance() {
        return new AuthorizationFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);

        //top bar
        authorizationType = view.findViewById(R.id.authorization_type);
        authorizationType.setOnClickListener(this);
        registrationType = view.findViewById(R.id.registration_type);
        registrationType.setOnClickListener(this);
        registrationLayout = view.findViewById(R.id.registration_layout);
        //ok button
        enterView = view.findViewById(R.id.enter_view);
        enterView.setOnClickListener(this);
        updateType();
        //views
        loginView = view.findViewById(R.id.login_view);
        passwordView = view.findViewById(R.id.password_view);
        TextView textView = view.findViewById(R.id.date_view);
        textView.setOnClickListener(this);

        return view;
    }

    private void updateType() {
        if (isAuth) {
            authorizationType.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            registrationType.setTextColor(getResources().getColor(R.color.colorDark));
            registrationLayout.setVisibility(View.GONE);
            enterView.setText(getString(R.string.enter));
        } else {
            authorizationType.setTextColor(getResources().getColor(R.color.colorDark));
            registrationType.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            registrationLayout.setVisibility(View.VISIBLE);
            enterView.setText(getString(R.string.to_registration));
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.authorization_type:
                isAuth = true;
                updateType();
                break;
            case R.id.registration_type:
                isAuth = false;
                updateType();
                break;
            case R.id.enter_view:
                if (isAuth) {
                    toAuthorization(view.getContext());
                } else {
                    Toast.makeText(view.getContext(), "В разработке", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.date_view:
                createDatePicker(view.getContext());
                break;
        }
    }

    private void createDatePicker(Context context) {
        final Calendar calendarMax = Calendar.getInstance();
        calendarMax.add(Calendar.DAY_OF_MONTH, 7);

        final Calendar calendarToday = Calendar.getInstance();
        int year = calendarToday.get(Calendar.YEAR);
        int month = calendarToday.get(Calendar.MONTH);
        calendarToday.add(Calendar.DAY_OF_MONTH, 1);
        int day = calendarToday.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, null,
                year, month, day);
//        datePickerDialog.getDatePicker().setMinDate(calendarToday.getTimeInMillis());
//        datePickerDialog.getDatePicker().setMaxDate(calendarMax.getTimeInMillis());
        datePickerDialog.show();
    }

    private void toAuthorization(final Context context) {
        if (checkAuthValue()) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            App.getRepository().authorization(
                    loginView.getText().toString(),
                    passwordView.getText().toString(),
                    new DataCallback() {
                        @Override
                        public void onEmit(Object data) {

                        }

                        @Override
                        public void onCompleted() {
                            progressDialog.cancel();
                            Toast.makeText(context, "Авторизация прошла успешно", Toast.LENGTH_LONG).show();
                            SharedPreferencesManager preferencesManager = new SharedPreferencesManager(context);
                            preferencesManager.saveLogin(loginView.getText().toString());
                            preferencesManager.savePassword(passwordView.getText().toString());
                            callback.isSuccessful();
                            dismiss();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.cancel();
                            Toast.makeText(context, "Неверные даные.", Toast.LENGTH_LONG).show();
                            callback.isFailed();
                            dismiss();
                        }
                    });
        } else
            Toast.makeText(context, "Заполните даные.", Toast.LENGTH_LONG).show();
    }

    private boolean checkAuthValue() {
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        return login.length() > 2 && password.length() > 3;
    }

    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }
}
