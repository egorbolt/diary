package ru.nsuorg.shiftorg.adapters;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.ProfessionInfo;
import ru.nsuorg.shiftorg.entity_models.ServiceInfo;

public class EditNeedAdapter extends RecyclerView.Adapter<EditNeedAdapter.ViewHolder>  {
    private List<ServiceInfo> items = new ArrayList<>();
    /*private LoadingView loadingView;
    private Context context;
*/
    public void setItems(Collection<ServiceInfo> info) {
        items.addAll(info);
        notifyDataSetChanged();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public List<ServiceInfo> getItems() {
        return items;
    }

    @NonNull
    @Override
    public EditNeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_edit_need, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditNeedAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /*public LoadingView getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(LoadingView loadingView) {
        this.loadingView = loadingView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Spinner profession;
        private TextView startDate;
        private TextView endDate;

        void bind(final ServiceInfo info) {
           //profession.setSelection(setIte);
            //startDate.setHint(info.getPeriod().getStart());
            //endDate.setHint(info.getPeriod().getEnd());
        }

        ViewHolder(View itemView) {
            super(itemView);
            profession = itemView.findViewById(R.id.profession);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);

        }

        public TextView getStartDate() {
            return startDate;
        }

        public void setStartDate(TextView startDate) {
            this.startDate = startDate;
        }

        public TextView getEndDate() {
            return endDate;
        }

        public void setEndDate(TextView endDate) {
            this.endDate = endDate;
        }
    }

}
