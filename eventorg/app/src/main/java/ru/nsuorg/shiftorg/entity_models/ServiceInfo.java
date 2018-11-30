package ru.nsuorg.shiftorg.entity_models;

import java.io.Serializable;

public class ServiceInfo implements Serializable {
    private String serviceID;
    private String profession;
    private TimePeriodInfo period;
    private boolean busy;

    public ServiceInfo(String serviceID, String profession, TimePeriodInfo busyDate, boolean busy) {
        this.serviceID = serviceID;
        this.profession = profession;
        this.period = busyDate;
        this.busy = busy;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public TimePeriodInfo getPeriod() {
        return period;
    }

    public void setPeriod(TimePeriodInfo period) {
        this.period = period;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
