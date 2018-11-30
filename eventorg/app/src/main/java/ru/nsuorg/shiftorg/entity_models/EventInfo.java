package ru.nsuorg.shiftorg.entity_models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EventInfo implements Serializable {
    private String eventID;
    private String eventName;
    private TimePeriodInfo period;
    private String orgId;
    private UserInfo organizer;
    private List<UserInfo> participantsInfo;
    private List<String> participantsId;
    private List<ServiceInfo> requiredProfessions;
    private String info;
    private Map<String, String> performers;

    public EventInfo(String eventID,
                     String eventName,
                     TimePeriodInfo date,
                     String orgID,
                     UserInfo organizer,
                     List<UserInfo> participantsInfo,
                     List<String> participantsId,
                     List<ServiceInfo> requiredProfessions,
                     String info,
                     Map<String, String> performers) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.period = date;
        this.orgId = orgID;
        this.organizer = organizer;
        this.participantsInfo = participantsInfo;
        this.participantsId = participantsId;
        this.requiredProfessions = requiredProfessions;
        this.info = info;
        this.performers = performers;
    }


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public UserInfo getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserInfo organizer) {
        this.organizer = organizer;
    }

    public List<UserInfo> getParticipantsInfo() {
        return participantsInfo;
    }

    public void setParticipantsInfo(List<UserInfo> participantsInfo) {
        this.participantsInfo = participantsInfo;
    }

    public List<ServiceInfo> getRequiredProfessions() {
        return requiredProfessions;
    }

    public void setRequiredProfessions(List<ServiceInfo> requiredProfessions) {
        this.requiredProfessions = requiredProfessions;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public TimePeriodInfo getPeriod() {
        return period;
    }

    public void setPeriod(TimePeriodInfo period) {
        this.period = period;
    }

    public List<String> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(List<String> participantsId) {
        this.participantsId = participantsId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Map<String, String> getPerformers() {
        return performers;
    }

    public void setPerformers(Map<String, String> performers) {
        this.performers = performers;
    }

}
