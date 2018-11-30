package ru.nsuorg.shiftorg.entity_models;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable{

    private String userID;
    private String profession;
    private String firstName;
    private String secondName;
    private String login;
    private String email;
    private String password;
    private String phone;
    private List<TimePeriodInfo> busyDates;

    public UserInfo(String usrID, String profession, String firstName, String secondName, String login,
            String email,
            String password,
            String phone, List<TimePeriodInfo> busyData) {
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userID = usrID;
        this.profession = profession;
        this.firstName = firstName;
        this.secondName = secondName;
        this.busyDates = busyData;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String usreID) {
        this.userID = userID;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public List<TimePeriodInfo> getBusyData() {
        return busyDates;
    }

    public void setBusyData(List<TimePeriodInfo> busyData) {
        this.busyDates = busyData;
    }
}
