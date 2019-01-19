package ru.nsu.boldyrev.motivationaldiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.file.NotLinkException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView nListViewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nListViewTasks = findViewById(R.id.main_listView_tasks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_new_task:
                //start TaskActivity in NewTask mode
                Intent newTaskActivity = new Intent(this, TaskActivity.class);
                startActivity(newTaskActivity);
                break;
        }

        return true;
    }

    public void onMotivateButtonClick(View view) {
        Intent intent = new Intent(this, MotivationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nListViewTasks.setAdapter(null);

        ArrayList<Task> tasks = Utilities.getAllSavedTasks(this);

        if (tasks == null || tasks.size() == 0) {
            Toast.makeText(this, "No tasks found!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            TaskAdapter ta = new TaskAdapter(this, R.layout.item_task, tasks);
            nListViewTasks.setAdapter(ta);

            nListViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = ((Task) nListViewTasks.getItemAtPosition(position)).getnDateTime() + Utilities.FILE_EXTENSION;

                    //if we have a task already, show it by passing fileName to TaskActivity by key TASK_FILE
                    Intent viewTaskIntent = new Intent(getApplicationContext(), TaskActivity.class);
                    viewTaskIntent.putExtra("TASK_FILE", fileName);
                    startActivity(viewTaskIntent);
                }
            });
        }
    }
}
