package ru.nsuorg.shiftorg.adapters;

import android.content.Context;
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

import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.UserPageActivity;
import ru.nsuorg.shiftorg.entity_models.UserInfo;;

public class EditParticipantsAdapter extends RecyclerView.Adapter<EditParticipantsAdapter.ParticipantInfoViewHolder> {
    private List<UserInfo> participantInfoList = new ArrayList<>();
    Context context;

    public void setItems(Collection<UserInfo> items) {
        participantInfoList.addAll(items);
        notifyDataSetChanged();
    }

    public void clearItems() {
        participantInfoList.clear();
        notifyDataSetChanged();
    }

    public List<UserInfo> getItems() {
        return participantInfoList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ParticipantInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_edit_participants, parent, false);
        return new ParticipantInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantInfoViewHolder holder, int position) {
        holder.bind(participantInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return participantInfoList.size();
    }

    public class ParticipantInfoViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView profession;
        private TextView startDate;
        private TextView endDate;

        void bind(UserInfo participantInfo) {
            String name = participantInfo.getFirstName() + " " + participantInfo.getSecondName();
            userName.setText(name);
            profession.setText(participantInfo.getProfession());
            //startDate.setHint(participantInfo.getStartDate);
            //endDate.setHint(participantInfo.getEndDate());
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
