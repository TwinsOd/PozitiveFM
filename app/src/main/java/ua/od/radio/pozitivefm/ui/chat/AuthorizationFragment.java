package ua.od.radio.pozitivefm.ui.chat;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.model.RegistrationModel;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;


public class AuthorizationFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private boolean isAuth = true;
    private TextView authorizationType;
    private TextView registrationType;
    private LinearLayout registrationLayout;
    private Button enterView;
    private EditText loginView;
    private EditText passwordView;
    private ResponseCallback callback;
    private String gender, state;
    private EditText emailView;
    private TextView dateView;
    private boolean isChangeDate = false;

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
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.authorization_dialog);
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
        emailView = view.findViewById(R.id.email_view);
        dateView = view.findViewById(R.id.date_view);
        dateView.setOnClickListener(this);
        Spinner genderSpinner = view.findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(this);
        Spinner stateSpinner = view.findViewById(R.id.state_spinner);
        stateSpinner.setOnItemSelectedListener(this);

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
                    toRegistration(view.getContext());
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateView.setText(parseDate(year, month, dayOfMonth));
                dateView.setTextColor(getResources().getColor(R.color.black));
                isChangeDate = true;
            }
        },
                year, month, day);
//        datePickerDialog.getDatePicker().setMinDate(calendarToday.getTimeInMillis());
//        datePickerDialog.getDatePicker().setMaxDate(calendarMax.getTimeInMillis());
        datePickerDialog.show();
    }

    private void toAuthorization(final Context context) {
        if (!checkAuthValue()) {
            Toast.makeText(context, "Заполните даные.", Toast.LENGTH_LONG).show();
            return;
        }
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

    }


    private void toRegistration(final Context context) {
        if (!checkRegistration()) {
            Toast.makeText(context, "Заполните даные.", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(context);
        if (getActivity() != null)
            progressDialog.show();
        RegistrationModel model = new RegistrationModel();
        model.setUsername(loginView.getText().toString());
        model.setPassword(passwordView.getText().toString());
        model.setEmail(emailView.getText().toString());
        model.setFamily_status(state);
        model.setFloor(gender);
        model.setDob(emailView.getText().toString());
        App.getRepository().registration(
                model,
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

    }

    private String parseDate(int year, int month, int day) {
        month++;
        String monthStr = month > 9 ? String.valueOf(month) : String.format("0%s", month);
        String dayStr = day > 9 ? String.valueOf(day) : String.format("0%s", day);
        return String.format("%s-%s-%s", year, monthStr, dayStr);
    }

    private boolean checkAuthValue() {
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        return login.length() > 2 && password.length() > 3;
    }

    private boolean checkRegistration() {
        return
                checkAuthValue()
                        && gender != null
                        && state != null
                        && checkEmail()
                        && isChangeDate;

    }

    private boolean checkEmail() {
        String email = emailView.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value;
        if (position == 0) {
            value = null;
        } else
            value = parent.getItemAtPosition(position).toString();

        switch (parent.getId()) {
            case R.id.gender_spinner:
                gender = value;
                break;
            case R.id.state_spinner:
                state = value;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
