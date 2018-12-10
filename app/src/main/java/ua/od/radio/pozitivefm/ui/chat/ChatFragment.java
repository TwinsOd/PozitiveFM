package ua.od.radio.pozitivefm.ui.chat;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.callback.ResponseCallback;
import ua.od.radio.pozitivefm.data.model.ChatModel;
import ua.od.radio.pozitivefm.data.shared_preferences.SharedPreferencesManager;


public class ChatFragment extends Fragment implements View.OnClickListener {
    private final int UPDATE_TIME = 10 * 1000;
    private ChatAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout enterLayout;
    private LinearLayout messageLayout;
    private EditText inputMessage;
    private ImageView sendMessageView;
    private boolean isAuth = false;
    private TextView logOutView;
    private ProgressDialog progressDialog;
    private SharedPreferencesManager preferencesManager;
    private Handler timerHandler;

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

        //init list
        recyclerView = view.findViewById(R.id.chat_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.show();
        loadData();

        //log out
        logOutView = view.findViewById(R.id.log_out_view);
        logOutView.setOnClickListener(this);

        //bottom bar
        enterLayout = view.findViewById(R.id.enter_layout);
        view.findViewById(R.id.enter_view).setOnClickListener(this);
        messageLayout = view.findViewById(R.id.message_layout);
        inputMessage = view.findViewById(R.id.input_message);
        view.findViewById(R.id.send_message_view).setOnClickListener(this);
        preferencesManager = new SharedPreferencesManager(view.getContext());
        isAuth = preferencesManager.isAuthorization();
        updateBottomView();

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter_view:
                runAuthDialog(view.getContext());
                break;
            case R.id.send_message_view:
                sendMessage(view.getContext());
                break;
            case R.id.log_out_view:
                createDialog(view.getContext());
                break;
        }
    }

    private void loadData() {
        App.getRepository().getFullMessage(new DataCallback<List<ChatModel>>() {
            @Override
            public void onEmit(List<ChatModel> data) {
                Log.i("ChatFragment", "data.size " + data.size());
                adapter.setList(data);
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog = null;
                }
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
    }

    private void updateBottomView() {
        if (isAuth) {
            messageLayout.setVisibility(View.VISIBLE);
            logOutView.setVisibility(View.VISIBLE);
            enterLayout.setVisibility(View.GONE);
        } else {
            messageLayout.setVisibility(View.GONE);
            logOutView.setVisibility(View.GONE);
            enterLayout.setVisibility(View.VISIBLE);
        }
    }

    private void createDialog(Context context) {
        if (!isAuth) {
            return;
        }
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        String name = preferencesManager.getLogin();
        builder.setTitle("Выход из аккаунта")
                .setMessage(name + ", Вы уверены что хотите выйти?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logOut();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void logOut() {
        isAuth = false;
        preferencesManager.setAuthorization(false);
        preferencesManager.saveCookieBody("");
        updateBottomView();
    }

    private void sendMessage(final Context context) {
        if (inputMessage.getText().toString().length() > 1) {
            App.getRepository().sendMessage(inputMessage.getText().toString(), new ResponseCallback() {

                @Override
                public void isSuccessful() {
                    loadData();
//                    Toast.makeText(context, "Сообщение успешно отправлено :)", Toast.LENGTH_SHORT).show();
                    inputMessage.setText("");
                    adapter.addItem(new ChatModel(preferencesManager.getLogin(), inputMessage.getText().toString()));
                }

                @Override
                public void isFailed() {
                    Toast.makeText(context, "Что то пошло не так...", Toast.LENGTH_SHORT).show();
                }
            });

        } else
            Toast.makeText(context, "Напишите сообщение", Toast.LENGTH_SHORT).show();
    }

    private void runAuthDialog(final Context context) {
        AuthorizationFragment fragment = AuthorizationFragment.newInstance();
        fragment.setCallback(new ResponseCallback() {
            @Override
            public void isSuccessful() {
                isAuth = true;
                updateBottomView();
                preferencesManager.setAuthorization(true);
            }

            @Override
            public void isFailed() {
                Toast.makeText(context, "Произошла ошибка!", Toast.LENGTH_SHORT).show();
            }
        });
        fragment.show(getChildFragmentManager(), "authorization_fragment");
    }

    private void createTimer() {
        Log.i("ChatFragment", "createTimer");
        timerHandler = new Handler();
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timerHandler == null)
                    return;
                Log.i("ChatFragment", "run Timer");
                timerHandler.postDelayed(this, UPDATE_TIME);
                loadData();
            }
        }, UPDATE_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        createTimer();
    }
}
