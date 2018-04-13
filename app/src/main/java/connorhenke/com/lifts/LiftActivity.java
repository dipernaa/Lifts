package connorhenke.com.lifts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.design.widget.RxTextInputLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import connorhenke.com.lifts.viewmodels.Lift;
import connorhenke.com.lifts.viewmodels.Set;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LiftActivity extends AppCompatActivity {

    @Inject AppDatabase db;

    public static Intent getIntent(Context context, long liftId) {
        Intent intent = new Intent(context, LiftActivity.class);
        intent.putExtra("LIFT_ID", liftId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift);

        ((LiftApplication) getApplication()).getComponent().inject(this);

        final long liftId = getIntent().getLongExtra("LIFT_ID", 0);
        db.liftDao().getLift(liftId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Lift>() {
                    @Override
                    public void accept(Lift lift) throws Exception {
                        setTitle(lift.getName());
                    }
                });

        db.setDao().getSets(liftId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Set>>() {
                    @Override
                    public void accept(List<Set> sets) throws Exception {
                        for (Set set : sets) {
                            Log.d("SET", "" + set.getWeight());
                        }
                    }
                });

        final WeightView weightView = findViewById(R.id.lift_weight_plates);
        final TextInputLayout weight = findViewById(R.id.lift_weight_edit_text);
        RxTextView.textChanges(weight.getEditText())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .map(new Function<CharSequence, Integer>() {
                    @Override
                    public Integer apply(CharSequence charSequence) throws Exception {
                        try {
                            Integer num = Integer.parseInt(charSequence.toString());
                            return num;
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer > 0) {
                            weightView.setWeight(integer);
                        }
                    }
                });
        TextView weightDown = findViewById(R.id.lift_weight_down);
        TextView weightUp = findViewById(R.id.lift_weight_up);

        RxView.clicks(weightDown)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        try {
                            Integer num = Integer.parseInt(weight.getEditText().getText().toString()) - 5;
                            weight.getEditText().setText("" + num);
                            return num;
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                    }
                });

        RxView.clicks(weightUp)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        try {
                            Integer num = Integer.parseInt(weight.getEditText().getText().toString()) + 5;
                            weight.getEditText().setText("" + num);
                            return num;
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                    }
                });

        final TextInputLayout reps = findViewById(R.id.lift_reps_edit_text);
        final TextView repsDown = findViewById(R.id.lift_reps_down);
        final TextView repsUp = findViewById(R.id.lift_reps_up);

        RxView.clicks(repsDown)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        try {
                            Integer num = Integer.parseInt(reps.getEditText().getText().toString()) - 1;
                            reps.getEditText().setText("" + num);
                            return num;
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                    }
                });

        RxView.clicks(repsUp)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        try {
                            Integer num = Integer.parseInt(reps.getEditText().getText().toString()) + 1;
                            reps.getEditText().setText("" + num);
                            return num;
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                    }
                });

        Button log = findViewById(R.id.lift_log);
        RxView.clicks(log)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<Object, Set>() {
                    @Override
                    public Set apply(Object o) throws Exception {
                        Set set = new Set();
                        set.setLiftId(liftId);
                        set.setDate(new Date());
                        set.setWeight(Integer.parseInt(weight.getEditText().getText().toString()));
                        set.setReps(Integer.parseInt(reps.getEditText().getText().toString()));
                        return set;
                    }
                })
                .subscribe(new Consumer<Set>() {
                    @Override
                    public void accept(Set set) throws Exception {
                        db.setDao().insertAll(set);
                    }
                });

    }
}
