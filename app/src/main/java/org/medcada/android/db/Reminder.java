package org.medcada.android.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reminder extends RealmObject {
    @PrimaryKey
    private long id;

    private Date alarmTime;
    private boolean oneTime;
    private boolean active;
    private String label;
    private String doctor;
    private String location;
    private String note;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    private String metric;

    public String getSms() {
        return sms;
    }


    public void setSms(String sms) {
        this.sms = sms;
    }

    private String sms;

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    boolean isReminder;

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }



    public Reminder() {
    }

        public Reminder(long id, Date alarmTime, String label, String metric, boolean oneTime, boolean active) {
        this.id = id;
        this.label = label;
        this.alarmTime = alarmTime;
        this.metric = metric;
        this.oneTime = oneTime;
        this.active = active;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", alarmTime=" + alarmTime +
                ", oneTime=" + oneTime +
                ", active=" + active +
                ", label='" + label + '\'' +
                ", doctor='" + doctor + '\'' +
                ", location='" + location + '\'' +
                ", note='" + note + '\'' +
                ", title='" + title + '\'' +
                ", metric='" + metric + '\'' +
                ", sms='" + sms + '\'' +
                ", isReminder=" + isReminder +
                '}';
    }
}
