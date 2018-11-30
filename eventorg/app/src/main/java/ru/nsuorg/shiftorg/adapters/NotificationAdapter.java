package ru.nsuorg.shiftorg.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.UserInfo;
import ru.nsuorg.shiftorg.entity_models.NotificationInfo;
import ru.nsuorg.shiftorg.entity_models.ServiceInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

import static android.content.Context.MODE_PRIVATE;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationInfo> items = new ArrayList<>();
    private LoadingView loadingView;
    private Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_notification_org, parent, false);
                return new OrgViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_notification_participant, parent, false);
                return new PartViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((OrgViewHolder) holder).bind(items.get(position));
                break;
            case 1:
                ((PartViewHolder) holder).bind(items.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        String status = items.get(position).getType();
        return (status.equals("JOB_SUGGESTION") || status.equals("CHANGED_TIME")) ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(Collection<NotificationInfo> tweets) {
        items.addAll(tweets);
        notifyDataSetChanged();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public LoadingView getLoadingView() {
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
    }

    class OrgViewHolder extends RecyclerView.ViewHolder {

        TextView tvBody;
        Button btnDelete;

        void bind(final NotificationInfo info) {
            String text = OrgViewText(info);
            tvBody.setText(Html.fromHtml(text));
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getContext().getSharedPreferences(getContext().getString(R.string.pref_key), MODE_PRIVATE);
                    String userID = pref.getString("userID", "0");
                    loadingView.startAnimation();
                    EventorApp.getRetrofitProvider(getContext()).getNotificationApi().deleteNotification(info.getNotificationID(),userID).enqueue(new Callback<ResponseWrapper>() {
                        @Override
                        public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                            loadingView.stopAnimation();
                            ResponseWrapper wrapper = response.body();
                            if (wrapper!= null) {
                                if (wrapper.getStatus().equals("OK")) {
                                    items.remove(info);
                                    notifyDataSetChanged();
                                    return;
                                }
                            }
                            Toast.makeText(getContext(),"Could not remove notification",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                            loadingView.stopAnimation();
                            Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        private String OrgViewText(final NotificationInfo info){
            String status = info.getType();
            UserInfo pageInfo = info.getSender();
            ServiceInfo serviceInfo = info.getService();
            EventInfo eventInfo = info.getEvent();
            String text = "";
            if(status.equals("JOB_CONFIRM")){
                text = "<b>" + pageInfo.getFirstName() + " " + pageInfo.getSecondName() + "</b> будет участвовать <b>" + serviceInfo.getProfession()
                        + "</b> в вашем событии <b>" + eventInfo.getEventName() + "</b>";
            }
            if(status.equals("CHANGED_NAME")){
                text = "<b>" + pageInfo.getFirstName() + " " + pageInfo.getSecondName() + "</b> изменил(а) название события на <b>" + eventInfo.getEventName() + "</b>";
            }
            if(status.equals( "TIME_REJECT")){
                text = "<b>" + pageInfo.getFirstName() + " " + pageInfo.getSecondName() + "</b> отказался(ась) участвовать <b>" + serviceInfo.getProfession()
                        + "</b> в вашем событии <b>" + eventInfo.getEventName() + "</b>";
            }
            return text;
        }

        private OrgViewHolder(View itemView) {
            super(itemView);
            tvBody = itemView.findViewById(R.id.tvNotificationBody);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    class PartViewHolder extends RecyclerView.ViewHolder {

        TextView tvBody;
        Button btnAccept;
        Button btnReject;

        void bind(final NotificationInfo info) {

            SharedPreferences prefs = getContext().getSharedPreferences(getContext().getString(R.string.pref_key),Context.MODE_PRIVATE);
            final String userID = prefs.getString("userID", "0");
            String text = PartViewText(info);
            tvBody.setText(Html.fromHtml(text));
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingView.startAnimation();
                    EventorApp.getRetrofitProvider(getContext()).getNotificationAcceptanceApi().sendResponse(info.getNotificationID(),
                            "ACCEPTED", userID).enqueue(new Callback<ResponseWrapper>() {
                        @Override
                        public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                            loadingView.stopAnimation();
                            ResponseWrapper responseWrapper = response.body();
                            if (responseWrapper != null) {
                                switch (responseWrapper.getStatus()) {
                                    case "OK":
                                        toasts(info, "You accepted this job");
                                        break;
                                    case "JOB_BOOKED":
                                        toasts(info, "This job is already occupied");
                                        break;
                                    case "DATE_BUSY":
                                        toasts(info, "You are busy at this time");
                                        break;
                                    case "BAD_REQUEST":
                                    default:
                                        Toast.makeText(context, "Unexpected response", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                            loadingView.stopAnimation();
                            Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingView.startAnimation();
                    SharedPreferences prefs = getContext().getSharedPreferences(getContext().getString(R.string.pref_key),Context.MODE_PRIVATE);
                    final String userID = prefs.getString("userID", "0");
                    EventorApp.getRetrofitProvider(getContext()).getNotificationAcceptanceApi().sendResponse(info.getNotificationID(),
                            "REJECTED", userID).enqueue(new Callback<ResponseWrapper>() {
                        @Override
                        public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                            loadingView.stopAnimation();
                            ResponseWrapper responseWrapper = response.body();
                            if (responseWrapper != null) {
                                switch (responseWrapper.getStatus()) {
                                    case "OK":
                                        toasts(info, "You rejected this job");
                                        break;
                                    case "BAD_REQUEST":
                                    default:
                                        Toast.makeText(context, "Unexpected response", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                            loadingView.stopAnimation();
                            Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }



        private PartViewHolder(View itemView) {
            super(itemView);
            tvBody = itemView.findViewById(R.id.tvBody);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }

        private String PartViewText(final NotificationInfo info){
            String status = info.getType();
            UserInfo pageInfo = info.getSender();
            ServiceInfo serviceInfo = info.getService();
            ServiceInfo serviceInfoOld = info.getOldService();
            EventInfo eventInfo = info.getEvent();
            String text = "";
            if(status.equals("JOB_SUGGESTION")){
                text = "<b>" + pageInfo.getFirstName() + " " + pageInfo.getSecondName() + "</b> приглашает вас, как <b>" + serviceInfo.getProfession()
                        + "</b> на событие <b>" + eventInfo.getEventName() + "</b>";
            }
            if(status.equals("CHANGED_TIME")){
                text = "<b>" + pageInfo.getFirstName() + " " + pageInfo.getSecondName() + "</b> изменил(а) время события <b>" + eventInfo.getEventName() + " c " + serviceInfoOld.getPeriod() + " на "
                        + serviceInfo.getPeriod() + ". Подтвердите или отклоните своё участие." + "</b>";
            }
            return text;
        }
    }

    private void toasts(NotificationInfo info, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        items.remove(info);
        notifyDataSetChanged();
    }
}
