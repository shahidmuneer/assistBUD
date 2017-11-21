package org.medcada.android.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shariq Khan on 6/2/2017.
 */

public class MedicationBean extends RealmObject {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey
    private long id;
    String medicationName,form,shape,reminderTime,repeat;
    int qty,dose, interval;
    boolean asNeeded;
    public MedicationBean(){}
    public MedicationBean(String medicationName, String form, String shape, String reminderTime, String repeat, int qty, int dose, int interval, boolean asNeeded,int color) {
        this.medicationName = medicationName;
        this.form = form;
        this.shape = shape;
        this.reminderTime = reminderTime;
        this.repeat = repeat;
        this.qty = qty;
        this.dose = dose;
        this.interval = interval;
        this.asNeeded = asNeeded;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;
    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isAsNeeded() {
        return asNeeded;
    }

    public void setAsNeeded(boolean asNeeded) {
        this.asNeeded = asNeeded;
    }

    @Override
    public String toString() {
        return "MedicationBean{" +
                "id=" + id +
                ", medicationName='" + medicationName + '\'' +
                ", form='" + form + '\'' +
                ", shape='" + shape + '\'' +
                ", reminderTime='" + reminderTime + '\'' +
                ", repeat='" + repeat + '\'' +
                ", qty=" + qty +
                ", dose=" + dose +
                ", interval=" + interval +
                ", asNeeded=" + asNeeded +
                '}';
    }
}
