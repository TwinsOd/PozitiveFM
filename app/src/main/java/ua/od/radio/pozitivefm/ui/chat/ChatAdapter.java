package ua.od.radio.pozitivefm.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.custom.URLImageParser;
import ua.od.radio.pozitivefm.data.model.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private List<ChatModel> list;
    private Context context;

    ChatAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    public void setList(List<ChatModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
        return new ChatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatHolder chatHolder, int i) {
        ChatModel model = list.get(i);
        chatHolder.nickView.setText(model.getNick());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            chatHolder.messageView.setText(Html.fromHtml(model.getMessage(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
        URLImageParser urlImageParser = new URLImageParser(chatHolder.messageView, context);
        chatHolder.messageView.setText(Html.fromHtml(model.getMessage(), urlImageParser, null));
//        }
//        chatHolder.messageView.setText(model.getMessage());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ChatHolder extends RecyclerView.ViewHolder {
        private TextView messageView, nickView;
//        private LinearLayout messageContainer;

        ChatHolder(final View view) {
            super(view);
            nickView = view.findViewById(R.id.nick_view);
            messageView = view.findViewById(R.id.message_view);
//            messageContainer = view.findViewById(R.id.message_container);
        }
    }


}
