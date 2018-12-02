package ua.od.radio.pozitivefm.ui.chat;

import android.app.Fragment;
import android.app.ProgressDialog;
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

import java.util.List;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.ChatModel;


public class ChatFragment extends Fragment implements View.OnClickListener {

    private ChatAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout enterLayout;
    private LinearLayout messageLayout;
    private EditText inputMessage;
    private ImageView sendMessageView;

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

        enterLayout.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter_view:
                AuthorizationFragment fragment = AuthorizationFragment.newInstance();
                fragment.show(getChildFragmentManager(), "authorization_fragment");
                break;
            case R.id.send_message_view:

                break;
        }
    }
}
