package connorhenke.com.lifts;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import connorhenke.com.lifts.viewmodels.Set;
import io.reactivex.Flowable;

@Dao
public interface SetDao {

    @Query("SELECT * FROM `set` WHERE lift_id = :lid ORDER BY date DESC")
    Flowable<List<Set>> getSets(long lid);

    @Query("SELECT * FROM `set` WHERE lift_id = :lid AND `date` > :start AND `date` < :end")
    Flowable<List<Set>> getSetsByDay(long lid, Date start, Date end);

    @Update
    void updateSet(Set set);

    @Insert
    void insertAll(Set... sets);
}

