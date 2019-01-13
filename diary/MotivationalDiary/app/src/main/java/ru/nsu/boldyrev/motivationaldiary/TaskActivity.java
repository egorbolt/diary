package ru.nsu.boldyrev.motivationaldiary;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class TaskActivity extends AppCompatActivity {
    private EditText mEtTitle;
    private EditText mEtContent;
    private String mTaskFileName;
    private Task mLoadedTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mEtTitle = (EditText) findViewById(R.id.task_et_title);
        mEtContent = (EditText) findViewById(R.id.task_et_content);

        mTaskFileName = getIntent().getStringExtra("TASK_FILE");
        if (mTaskFileName != null && !mTaskFileName.isEmpty()) {
            mLoadedTask = Utilities.getTaskByName(this, mTaskFileName);

            if (mLoadedTask != null) {
                mEtTitle.setText(mLoadedTask.getnTitle());
                mEtContent.setText(mLoadedTask.getnContent());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_new, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_task_save:
                saveTask();
                break;

            case R.id.action_task_delete:
                deleteTask();
                break;
        }

        return true;
    }

    private void saveTask() {
        Task task;

        if (mEtTitle.getText().toString().trim().isEmpty() || mEtContent.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Can't save: emply title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mLoadedTask == null) {
            task = new Task(System.currentTimeMillis(), mEtTitle.getText().toString(), mEtContent.getText().toString());
        } else {
            task = new Task(mLoadedTask.getnDateTime(), mEtTitle.getText().toString(), mEtContent.getText().toString());
        }

        if (Utilities.saveTask(this, task)) {
            Toast.makeText(this, "Your task has been saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error while saving the task!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteTask() {
        if (mLoadedTask == null) {
            finish();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Deleting task")
                    .setMessage("You are about to delete \"" + mEtTitle.getText().toString() + "\", are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.deleteTask(getApplicationContext(), mLoadedTask.getnDateTime() + Utilities.FILE_EXTENSION);
                            Toast.makeText(getApplicationContext(), "Task \"" + mEtTitle.getText().toString() + "\" was deleted!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setCancelable(false);
            dialog.show();
        }
    }
}
