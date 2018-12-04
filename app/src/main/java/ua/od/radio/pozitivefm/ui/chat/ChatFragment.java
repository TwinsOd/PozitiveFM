package ua.od.radio.pozitivefm.ui.chat;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.model.ChatModel;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;


public class ChatFragment extends Fragment implements View.OnClickListener {

    private ChatAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout enterLayout;
    private LinearLayout messageLayout;
    private EditText inputMessage;
    private ImageView sendMessageView;
    private boolean isAuth = false;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.chat_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.show();
        App.getRepository().getFullMessage(new DataCallback<List<ChatModel>>() {
            @Override
            public void onEmit(List<ChatModel> data) {
                Log.i("ChatFragment", "data.size " + data.size());
                adapter.setList(data);
                progressDialog.cancel();
            }

            @Override
            public void onCompleted() {
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onError(Throwable throwable) {
                progressDialog.cancel();
            }
        });
        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        enterLayout = view.findViewById(R.id.enter_layout);
        view.findViewById(R.id.enter_view).setOnClickListener(this);
        messageLayout = view.findViewById(R.id.message_layout);
        inputMessage = view.findViewById(R.id.input_message);
        view.findViewById(R.id.send_message_view).setOnClickListener(this);

        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(view.getContext());
        isAuth = preferencesManager.isAuthorization();
        updateBottomView();
        return view;
    }

    private void updateBottomView() {
        if (isAuth) {
            messageLayout.setVisibility(View.VISIBLE);
            enterLayout.setVisibility(View.GONE);
        } else {
            enterLayout.setVisibility(View.VISIBLE);
            messageLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter_view:
                runAuthDialog();
                break;
            case R.id.send_message_view:
                sendMessage(view.getContext());

                break;
        }
    }

    private void sendMessage(final Context context) {
        Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
        if (inputMessage.toString().length() > 1) {
            App.getRepository().sendMessage(inputMessage.toString(), new ResponseCallback() {

                @Override
                public void isSuccessful() {

                }

                @Override
                public void isFailed() {

                }
            });

        } else
            Toast.makeText(context, "Напишите сообщение", Toast.LENGTH_SHORT).show();
    }

    private void runAuthDialog() {
        AuthorizationFragment fragment = AuthorizationFragment.newInstance();
        fragment.setCallback(new ResponseCallback() {
            @Override
            public void isSuccessful() {
                isAuth = true;
                updateBottomView();
                if (getActivity() != null) {
                    SharedPreferencesManager preferencesManager = new SharedPreferencesManager(getActivity());
                    preferencesManager.setAuthorization(true);
                }
            }

            @Override
            public void isFailed() {

            }
        });
        fragment.show(getChildFragmentManager(), "authorization_fragment");
    }


}
