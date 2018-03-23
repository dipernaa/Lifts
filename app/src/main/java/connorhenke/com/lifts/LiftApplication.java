package connorhenke.com.lifts;

import android.app.Application;
import connorhenke.com.lifts.viewmodels.Lift;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class LiftApplication extends Application {

    @Inject
    AppDatabase db;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .mainModule(new MainModule(this))
                .build();
        applicationComponent.inject(this);

        db.liftDao().getLiftCount()
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (aLong.longValue() <= 0) {
                            db.liftDao().insertAll(new Lift("Bench Press"), new Lift("Overhead Press"), new Lift("Deadlift"), new Lift("Sumo Deadlift"), new Lift("Back Squat"), new Lift("Front Squat"));
                        }
                    }
                });
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
