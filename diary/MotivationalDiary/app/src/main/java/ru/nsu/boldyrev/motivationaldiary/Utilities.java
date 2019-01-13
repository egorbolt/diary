package ru.nsu.boldyrev.motivationaldiary;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Utilities {
    public static final String FILE_EXTENSION = ".bin";

    public static boolean saveTask(Context context, Task task) {
        String fileName = String.valueOf(task.getnDateTime()) + FILE_EXTENSION;

        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(task);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArrayList<Task> getAllSavedTasks(Context context) {
        ArrayList<Task> tasks = new ArrayList<>();

        File filesDir = context.getFilesDir();
        ArrayList<String> taskFiles  = new ArrayList<>();

        for (String file: filesDir.list()) {
            if (file.endsWith(FILE_EXTENSION)) {
                taskFiles.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        for (int i = 0; i < taskFiles.size(); i++) {
            try {
                fis = context.openFileInput(taskFiles.get(i));
                ois = new ObjectInputStream(fis);

                tasks.add((Task)ois.readObject());

                fis.close();
                ois.close();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        return tasks;
    }

    public static Task getTaskByName(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        Task task;

        if (file.exists()){
            FileInputStream fis;
            ObjectInputStream ois;

            try {
                fis = context.openFileInput(fileName);
                ois = new ObjectInputStream(fis);

                task = (Task) ois.readObject();

                fis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            return task;
        }
        return null;
    }

    public static void deleteTask(Context context, String fileName) {
        File dir = context.getFilesDir();
        File file = new File(dir, fileName);

        if (file.exists()) {
            file.delete();
        }
    }
}
