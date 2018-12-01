package ua.od.radio.pozitivefm.data.task;

import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ua.od.radio.pozitivefm.data.callback.DataCallback;
import ua.od.radio.pozitivefm.data.model.ChatModel;

import static ua.od.radio.pozitivefm.data.net.RestModule.BASE_POSITIV_URL;

public class FullChatTask implements Runnable {
    private DataCallback<List<ChatModel>> callback;
    private Handler uiHandler;

    public FullChatTask(DataCallback<List<ChatModel>> callback, Handler uiHandler) {
        this.callback = callback;
        this.uiHandler = uiHandler;
    }

    @Override
    public void run() {
        final Element body;
        List<ChatModel> list = new ArrayList<>();
        try {
            body = Jsoup.connect(BASE_POSITIV_URL).post().body();
            if (body == null)
                return;

            Elements bodyElements = body.select("tbody");
            if (bodyElements == null || bodyElements.size() == 0)
                return;

            Elements columnElement = bodyElements.get(0).select("tr");
            for (Element column : columnElement) {
                Elements valueElement = column.select("td");
                int i = 0;
                Log.i("FullChatTask", "-----------------------------");
                ChatModel model = new ChatModel();
                for (Element value : valueElement) {
                    Log.i("FullChatTask", "i = " + i + ", value = " + value.html());
                    if (i == 1)
                        model.setNick(value.text());
                    else if (i == 2)
                        model.setMessage(value.html());
                    i++;
                }
                list.add(model);

            }
            uiHandler.post(new CallbackToUI(list));

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private class CallbackToUI implements Runnable {

        private final List<ChatModel> list;

        CallbackToUI(List<ChatModel> list) {
            this.list = list;
        }

        @Override
        public void run() {
            Log.i("FullChatTask", "run: postModels callback to ui");
            callback.onEmit(list);
            callback.onCompleted();
        }
    }
}
