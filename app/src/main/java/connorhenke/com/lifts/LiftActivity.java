package connorhenke.com.lifts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.jakewharton.rxbinding2.support.design.widget.RxTextInputLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import connorhenke.com.lifts.viewmodels.Lift;
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

        db.liftDao().getLift(getIntent().getLongExtra("LIFT_ID", 0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Lift>() {
                    @Override
                    public void accept(Lift lift) throws Exception {
                        setTitle(lift.getName());
                    }
                });

        final WeightView weightView = findViewById(R.id.lift_weight_plates);
        TextInputLayout weight = findViewById(R.id.lift_weight_edit_text);
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
    }
}
