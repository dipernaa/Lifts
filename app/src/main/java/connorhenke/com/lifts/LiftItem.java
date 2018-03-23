package connorhenke.com.lifts;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import connorhenke.com.lifts.viewmodels.Lift;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LiftItem extends Item<ViewHolder> {

    private Lift lift;
    private PublishSubject<Long> pubSub;

    public LiftItem(Lift lift) {
        this.lift = lift;
        this.pubSub = PublishSubject.create();
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView textView = viewHolder.itemView.findViewById(R.id.list_lift_name);
        textView.setText(lift.getName());

        RxView.clicks(textView)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<Object, Long>() {
                    @Override
                    public Long apply(Object o) throws Exception {
                        return lift.getLid();
                    }
                })
                .subscribe(pubSub);
    }

    public Observable<Long> getObservable() {
        return pubSub;
    }

    @Override
    public int getLayout() {
        return R.layout.list_lift;
    }
}
