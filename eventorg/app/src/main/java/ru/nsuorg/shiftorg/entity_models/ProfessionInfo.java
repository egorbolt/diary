package ru.nsuorg.shiftorg.entity_models;

import java.io.Serializable;

public class ProfessionInfo implements Serializable {
    private String profession;
    private String userName;
    private String startDate;
    private String endDate;

    public ProfessionInfo (String profession, String userName, String startDate, String endDate) {
        this.profession = profession;
        this.userName = userName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEndDate(String endDate) {

        this.endDate = endDate;
    }
}
