package ru.nsu.boldyrev.motivationaldiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, int resource, ArrayList<Task> tasks) {
        super(context, resource, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_task, null);
        }

        Task task = getItem(position);
        if (task != null) {
            TextView title = (TextView) convertView.findViewById(R.id.list_task_title);
            TextView date = (TextView) convertView.findViewById(R.id.list_task_date);
            TextView content = (TextView) convertView.findViewById(R.id.list_task_content);

            title.setText(task.getnTitle());
            date.setText(task.getDateTimeFormatted(getContext()));
            if (task.getnContent().length() > 50) {
                content.setText(task.getnContent().substring(0, 50));
            } else {
                content.setText(task.getnContent());
            }
        }

        return convertView;
    }
}
