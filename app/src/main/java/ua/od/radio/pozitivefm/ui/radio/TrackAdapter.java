package ua.od.radio.pozitivefm.ui.radio;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ua.od.radio.pozitivefm.R;
import ua.od.radio.pozitivefm.data.model.TrackModel;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder> {
    private List<TrackModel> list;
    private Calendar calendar;

    TrackAdapter() {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
    }

    public void setList(List<TrackModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrackAdapter.TrackHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_track, viewGroup, false);

        return new TrackHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.TrackHolder trackHolder, int i) {
        TrackModel model = list.get(i);
        trackHolder.timeView.setText(parseDate(model.getTs()));
        trackHolder.authorView.setText(model.getAuthor());
        trackHolder.nameView.setText(model.getTitle());
    }

    private String parseDate(long time){
        calendar.setTimeInMillis(time*1000);
        return String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class TrackHolder extends RecyclerView.ViewHolder {
        private TextView timeView, authorView, nameView;

        TrackHolder(final View view) {
            super(view);
            timeView = view.findViewById(R.id.time_view);
            authorView = view.findViewById(R.id.author_view);
            nameView = view.findViewById(R.id.name_view);

        }
    }
}
