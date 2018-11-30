package ru.nsuorg.shiftorg.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.nsuorg.shiftorg.EventFullActivity;
import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<EventInfo> items = new ArrayList<>();
    private LoadingView loadingView;
    private Context context;

    public void setItems(Collection<EventInfo> info) {
        items.addAll(info);
        notifyDataSetChanged();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_one_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public LoadingView getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(LoadingView loadingView) {
        this.loadingView = loadingView;
    }

    // TODO: 10.07.18
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameEvent;
        private TextView startDate;

        void bind(final EventInfo info) {
            nameEvent.setText(info.getEventName());
            startDate.setText(info.getPeriod().getStart());
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAAAAG", "clickeddede");
                    Intent intent = new Intent(context, EventFullActivity.class);
                    intent.putExtra("event", info);
                    context.startActivity(intent);
                }
            });
        }


        ViewHolder(View itemView) {
            super(itemView);
            nameEvent = itemView.findViewById(R.id.nameEvent);
            startDate = itemView.findViewById(R.id.startDate);
        }
    }
}