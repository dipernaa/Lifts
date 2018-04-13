package connorhenke.com.lifts;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import connorhenke.com.lifts.viewmodels.Set;
import io.reactivex.Flowable;

@Dao
public interface SetDao {

    @Query("SELECT * FROM `set` WHERE lift_id = :lid ORDER BY date DESC")
    Flowable<List<Set>> getSets(long lid);

    @Insert
    void insertAll(Set... sets);
}

