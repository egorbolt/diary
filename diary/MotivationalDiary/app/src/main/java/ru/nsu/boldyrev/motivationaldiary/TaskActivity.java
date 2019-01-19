package ru.nsu.boldyrev.motivationaldiary;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {
    private EditText mEtTitle;
    private EditText mEtContent;
    private String mTaskFileName;
    private Task mLoadedTask;
    private ArrayList<String> mEtItemList;
    private ArrayAdapter<String> itemListAdapter;
    private EditText mEtSubtask;
    private Button mEtButton;
    private ListView mEtListView;
    private ArrayList<Boolean> mEtItemListCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task);

        mEtTitle = (EditText) findViewById(R.id.task_et_title);
        mEtContent = (EditText) findViewById(R.id.task_et_content);
        mEtListView = (ListView) findViewById(R.id.task_et_listview);
        mEtSubtask = (EditText) findViewById(R.id.task_et_subtask);
        mEtButton = (Button) findViewById(R.id.task_et_button);

        mTaskFileName = getIntent().getStringExtra("TASK_FILE");


        if (mTaskFileName != null && !mTaskFileName.isEmpty()) {
            mLoadedTask = Utilities.getTaskByName(this, mTaskFileName);

            //Если задача была уже создана, то подгружаем её
            if (mLoadedTask != null) {
                mEtTitle.setText(mLoadedTask.getnTitle());
                mEtContent.setText(mLoadedTask.getnContent());
                mEtItemList = mLoadedTask.getnSubtasks();
                mEtItemListCheckBoxes = mLoadedTask.getnCheckboxes();

                /*Проблема: если мы отметили, что подзадача выполнена (поставили галочку) и сохранили задачу,
                то когда мы её заново откроем, галочки пропадут НЕСМОТРЯ НА ТО, что состояние галочек будет сохранено
                корректно. Т.е. завершённые подзадачи сохранились, но галочки не отобразились. --Егор
                */

                /*Проблема заменилась на другую: теперь галочки отображаются в осответствии с содержимым mEtItemListCheckBoxes ,
                однако это содержимое сохраняется неверно (у меня - все "true"). --Андрей
                 */
                itemListAdapter = new ArrayAdapter<>(TaskActivity.this, android.R.layout.simple_list_item_multiple_choice, mEtItemList);
                mEtListView.setAdapter(itemListAdapter);

                for (int i = 0; i < mEtItemListCheckBoxes.size(); i++) {
                    boolean value = mEtItemListCheckBoxes.get(i);
                    mEtListView.setItemChecked(i, value);
                    itemListAdapter.notifyDataSetChanged();
                }

            }
        }

        //Если созданная задача - новая
        if (mLoadedTask == null) {
            mEtItemList = new ArrayList<>();
            mEtItemListCheckBoxes = new ArrayList<>();
            itemListAdapter = new ArrayAdapter<>(TaskActivity.this, android.R.layout.simple_list_item_multiple_choice, mEtItemList);

            mEtListView.setAdapter(itemListAdapter);
        }

        //Здесь происходит обработка выделения чекбокса сделанной подзадачи
       // itemListAdapter = new ArrayAdapter<>(TaskActivity.this, android.R.layout.simple_list_item_multiple_choice, mEtItemList);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtItemList.add(mEtSubtask.getText().toString());
                mEtSubtask.setText("");
                itemListAdapter.notifyDataSetChanged();
            }
        };

        mEtButton.setOnClickListener(buttonListener);

      //  mEtListView.setAdapter(itemListAdapter);
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

        mEtItemListCheckBoxes = new ArrayList<>();

        //Сохранение состояний подзадач (сделалана - галочка будет сохранена)
        SparseBooleanArray checked = mEtListView.getCheckedItemPositions();
        for (int i = 0; i < mEtItemList.size(); i++ ){
            mEtItemListCheckBoxes.add(i, false);
        }
        for (int i = 0; i < checked.size(); i++) {
            int key = checked.keyAt(i);
            mEtItemListCheckBoxes.remove(key);
            mEtItemListCheckBoxes.add(key, true);
        }

        if (mLoadedTask == null) {
            task = new Task(System.currentTimeMillis(), mEtTitle.getText().toString(), mEtContent.getText().toString(), mEtItemList, mEtItemListCheckBoxes);
        } else {
            task = new Task(mLoadedTask.getnDateTime(), mEtTitle.getText().toString(), mEtContent.getText().toString(), mEtItemList, mEtItemListCheckBoxes);
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
