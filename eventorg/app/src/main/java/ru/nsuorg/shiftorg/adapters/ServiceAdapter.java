package ru.nsuorg.shiftorg.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.EventorApp;
import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.entity_models.NotificationRequest;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.ServiceInfo;

import static android.content.Context.MODE_PRIVATE;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<ServiceInfo> needInfoList = new ArrayList<>();
    private Context context;
    private String eventID;

    public void setItems(Collection<ServiceInfo> needInfos) {
        needInfoList.addAll(needInfos);
        notifyDataSetChanged();
    }

    public void clearItems() {
        needInfoList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.need_list, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(needInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return needInfoList.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView profession;
        private TextView startDate;
        private TextView endDate;
        private Button btnSearch;

        void bind(final ServiceInfo needInfo) {
            profession.setText(needInfo.getProfession());
            startDate.setText(needInfo.getPeriod().getStart());
            endDate.setText(needInfo.getPeriod().getEnd());
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.pref_key), MODE_PRIVATE);
                    String userID = pref.getString("userID", "0");
                    EventorApp.getRetrofitProvider(context).getServiceSearchApi().sendInvitations(new NotificationRequest(userID,eventID,needInfo.getServiceID())).enqueue(new Callback<ResponseWrapper>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseWrapper> call, @NonNull Response<ResponseWrapper> response) {
                            ResponseWrapper wrapper = response.body();
                            if (wrapper != null) {
                                if (wrapper.getStatus().equals("OK")) {
                                    Toast.makeText(context, "Invitations send", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseWrapper> call, @NonNull Throwable t) {
                            Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        ServiceViewHolder(View itemView) {
            super(itemView);
            profession = itemView.findViewById(R.id.profession);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            btnSearch = itemView.findViewById(R.id.search);
        }
    }
}
