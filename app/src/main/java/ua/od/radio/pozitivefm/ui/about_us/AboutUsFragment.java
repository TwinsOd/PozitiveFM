package ua.od.radio.pozitivefm.ui.about_us;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ua.od.radio.pozitivefm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements View.OnClickListener {


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        view.findViewById(R.id.youtube_icon).setOnClickListener(this);
        view.findViewById(R.id.facebook_icon).setOnClickListener(this);
        view.findViewById(R.id.good_game_icon).setOnClickListener(this);
        view.findViewById(R.id.instagram_icon).setOnClickListener(this);
        view.findViewById(R.id.twitch_icon).setOnClickListener(this);
        view.findViewById(R.id.twitter_icon).setOnClickListener(this);

        view.findViewById(R.id.address_view).setOnClickListener(this);
        view.findViewById(R.id.telephone_vew).setOnClickListener(this);
        view.findViewById(R.id.mob_view).setOnClickListener(this);
        view.findViewById(R.id.email_view).setOnClickListener(this);
        view.findViewById(R.id.web_view).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.youtube_icon:
                openLink("https://www.youtube.com/watch?v=KRIUFyo9nYg");
                break;
            case R.id.facebook_icon:
                openLink("https://www.facebook.com/pozitiv.fm.odessa/");
                break;
            case R.id.good_game_icon:
                openLink("https://goodgame.ru/channel/PozitivFM");
                break;
            case R.id.instagram_icon:
                openLink("https://www.instagram.com/pozitiv.fm/");
                break;
            case R.id.twitch_icon:
                openLink("https://www.twitch.tv/pozitivfm");
                break;
            case R.id.twitter_icon:
                openLink("https://twitter.com/fm_pozitiv");
                break;
            case R.id.address_view:
                showAddress();
                break;
            case R.id.telephone_vew:
                callPhone("+38(048)799 08 29");
                break;
            case R.id.mob_view:
                callPhone("+38(067)628 07 77");
                break;
            case R.id.email_view:
                sendEmail();
                break;
            case R.id.web_view:
                openLink("http://pozitiv.fm/");
                break;
        }
    }

    private void openLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void callPhone(String number) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    999);
        }
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"pozitivfm.chief@gmail.com"});
//        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddress() {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("geo:46.4585832,30.7383589");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }
}
