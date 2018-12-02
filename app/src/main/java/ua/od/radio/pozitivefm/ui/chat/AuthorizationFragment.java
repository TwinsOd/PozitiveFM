package ua.od.radio.pozitivefm.ui.chat;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.od.radio.pozitivefm.R;


public class AuthorizationFragment extends DialogFragment implements View.OnClickListener {
    private boolean isAuth = true;
    private TextView authorizationType;
    private TextView registrationType;

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
        updateType();
        return view;
    }

    private void updateType() {
        if (isAuth) {
            authorizationType.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            registrationType.setTextColor(getResources().getColor(R.color.colorDark));
        } else {
            authorizationType.setTextColor(getResources().getColor(R.color.colorDark));
            registrationType.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authorization_type:
                isAuth = true;
                updateType();
                break;
            case R.id.registration_type:
                isAuth = false;
                updateType();
                break;
        }
    }
}
