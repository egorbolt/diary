package ru.nsuorg.shiftorg.entity_models;

public class NotificationRequest {
    private String senderID;
    private String eventID;
    private String serviceID;
    private String oldServiceID;

    public NotificationRequest(String senderID, String eventID, String serviceID, String oldServiceID) {
        this.senderID = senderID;
        this.eventID = eventID;
        this.serviceID = serviceID;
        this.oldServiceID = oldServiceID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getOldServiceID() {
        return oldServiceID;
    }

    public void setOldServiceID(String oldServiceID) {
        this.oldServiceID = oldServiceID;
    }

}
