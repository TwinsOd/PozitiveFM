package ua.od.radio.pozitivefm.ui.chat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ua.od.radio.pozitivefm.App;
import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.ChatModel;


public class ChatFragment extends Fragment {

    private ChatAdapter adapter;

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
        RecyclerView recyclerView = view.findViewById(R.id.chat_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter();
        recyclerView.setAdapter(adapter);
        App.getRepository().getFullMessage(new DataCallback<List<ChatModel>>() {
            @Override
            public void onEmit(List<ChatModel> data) {
                Log.i("ChatFragment", "data.size " + data.size());
                adapter.setList(data);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        return view;
    }
}
