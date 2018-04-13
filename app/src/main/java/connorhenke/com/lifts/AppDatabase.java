package connorhenke.com.lifts;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import connorhenke.com.lifts.viewmodels.Lift;
import connorhenke.com.lifts.viewmodels.Set;

/**
 * Created by connor on 2/10/2018.
 */

@Database(entities = {Lift.class, Set.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract LiftDao liftDao();
    public abstract SetDao setDao();
}
