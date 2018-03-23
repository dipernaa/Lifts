package connorhenke.com.lifts.viewmodels;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Lift {

    @PrimaryKey(autoGenerate = true)
    private long lid;
    private String name;

    public Lift(String name) {
        this.name = name;
    }

    public long getLid() {
        return lid;
    }

    public void setLid(long lid) {
        this.lid = lid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
