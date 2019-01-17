package ru.nsu.boldyrev.motivationaldiary;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Task implements Serializable {
    private long nDateTime; //дата
    private String nTitle; //название
    private String nContent; //главна цель
    private ArrayList<String> nSubtasks; //подзадачи
    private ArrayList<Boolean> nCheckboxes; /*здесь сохраняются те чекбоксы,
                                             которые мы отметили*/


    public Task(long nDateTime, String nTitle, String nContent, ArrayList<String> nSubtasks, ArrayList<Boolean> nCheckboxes) {
        this.nDateTime = nDateTime;
        this.nTitle = nTitle;
        this.nContent = nContent;
        this.nSubtasks = nSubtasks;
        this.nCheckboxes = nCheckboxes;
    }

    public void setnDateTime(long nDateTime) {
        this.nDateTime = nDateTime;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public void setnContent(String nContent) {
        this.nContent = nContent;
    }

    public long getnDateTime() {
        return nDateTime;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnContent() {
        return nContent;
    }

    public ArrayList<String> getnSubtasks() { return nSubtasks; }

    public ArrayList<Boolean> getnCheckboxes() { return nCheckboxes; }

    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(new Date(nDateTime));
    }
}

