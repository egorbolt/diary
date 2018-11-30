package ru.nsuorg.shiftorg.entity_models;

public class NotificationInfo {
    private String type;
    private String notificationID;
    private UserInfo sender;
    private EventInfo event;
    private ServiceInfo service;
    private ServiceInfo oldService;


    public NotificationInfo(String type, String notificationID, UserInfo sender, EventInfo event, ServiceInfo service) {
        this.type = type;
        this.notificationID = notificationID;
        this.sender = sender;
        this.event = event;
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public EventInfo getEvent() {
        return event;
    }

    public void setEvent(EventInfo event) {
        this.event = event;
    }

    public ServiceInfo getService() {
        return service;
    }

    public void setService(ServiceInfo service) {
        this.service = service;
    }


    public ServiceInfo getOldService() {
        return oldService;
    }

    public void setOldService(ServiceInfo oldService) {
        this.oldService = oldService;
    }
}
