package ru.nsuorg.shiftorg.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import ru.nsuorg.shiftorg.UserPageActivity;
import ru.nsuorg.shiftorg.entity_models.ServiceInfo;
import ru.nsuorg.shiftorg.entity_models.UserInfo;;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantInfoViewHolder> {
    private List<UserInfo> participantInfoList = new ArrayList<>();
    private List<ServiceInfo> serviceInfoList = new ArrayList<>();
    private Context context;

    public void setItems(Collection<UserInfo> participantInfos, Collection<ServiceInfo> serviceInfoList) {
        participantInfoList.addAll(participantInfos);
        this.serviceInfoList.addAll(serviceInfoList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        participantInfoList.clear();
        serviceInfoList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParticipantInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participants_list, parent, false);
        return new ParticipantInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantInfoViewHolder holder, int position) {
        holder.bind(participantInfoList.get(position), serviceInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return participantInfoList.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    class ParticipantInfoViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView profession;
        private TextView startDate;
        private TextView endDate;

        void bind(UserInfo participantInfo, ServiceInfo serviceInfo) {
            String name = participantInfo.getFirstName() + " " + participantInfo.getSecondName();
            userName.setText(name);
            profession.setText(participantInfo.getProfession());
            String userID = participantInfo.getUserID();

            startDate.setText(serviceInfo.getPeriod().getStart());
            endDate.setText(serviceInfo.getPeriod().getEnd());

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserPageActivity.start(context, participantInfo);
                }
            });

        }

        ParticipantInfoViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            profession = itemView.findViewById(R.id.profession);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
        }
    }
}
