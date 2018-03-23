package connorhenke.com.lifts.viewmodels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.icu.math.BigDecimal;

import java.util.*;

@Entity
public class Set {

    public Set() {}

    @PrimaryKey(autoGenerate = true)
    private int sid;

    @ColumnInfo(name = "lift_id")
    private int liftId;

    private double weight;
    private int reps;
    private Date date;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getLiftId() {
        return liftId;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

