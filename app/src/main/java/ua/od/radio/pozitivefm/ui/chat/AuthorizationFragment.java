package ua.od.radio.pozitivefm.ui.chat;


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

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;


public class AuthorizationFragment extends DialogFragment implements View.OnClickListener {
    private boolean isAuth = true;
    private TextView authorizationType;
    private TextView registrationType;
    private LinearLayout registrationLayout;
    private Button enterView;
    private EditText loginView;
    private EditText passwordView;

    public AuthorizationFragment() {
        // Required empty public constructor
    }

    public static AuthorizationFragment newInstance() {
        AuthorizationFragment fragment = new AuthorizationFragment();
        return fragment;
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
        authorizationType = view.findViewById(R.id.authorization_type);
        authorizationType.setOnClickListener(this);
        registrationType = view.findViewById(R.id.registration_type);
        registrationType.setOnClickListener(this);
        registrationLayout = view.findViewById(R.id.registration_layout);
        enterView = view.findViewById(R.id.enter_view);
        enterView.setOnClickListener(this);
        updateType();

        loginView = view.findViewById(R.id.login_view);
        passwordView = view.findViewById(R.id.password_view);
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
        }
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
                            dismiss();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.cancel();
                            Toast.makeText(context, "Неверные даные.", Toast.LENGTH_LONG).show();
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
}
