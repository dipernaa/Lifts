package connorhenke.com.lifts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerViewAdapter;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.xwray.groupie.Group;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import connorhenke.com.lifts.viewmodels.Lift;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject AppDatabase db;

    private RecyclerView recyclerView;
    private List<Lift> lifts;
    private Section liftGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((LiftApplication) getApplication()).getComponent().inject(this);

        recyclerView = findViewById(R.id.main_recycler_view);
        final GroupAdapter adapter = new GroupAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new FadeInUpAnimator(new DecelerateInterpolator()));
        recyclerView.setAdapter(adapter);
        SearchItem searchItem = new SearchItem();
        searchItem.getObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<CharSequence, List<Lift>>() {
                    @Override
                    public List<Lift> apply(@NonNull CharSequence charSequence) throws Exception {
                        List<Lift> filtered = new ArrayList<Lift>();
                        for(Lift lift : lifts) {
                            if (lift.getName().toLowerCase().contains(charSequence)) {
                                filtered.add(lift);
                            }
                        }
                        return filtered;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Lift>>() {
                    @Override
                    public void accept(@NonNull List<Lift> filtered) throws Exception {
                        adapter.remove(liftGroup);
                        liftGroup = new Section();
                        for (Lift lift : filtered) {
                            LiftItem item = new LiftItem(lift);
                            item.getObservable()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Object>() {
                                        @Override
                                        public void accept(@NonNull Object o) throws Exception {
                                            startActivity(new Intent(MainActivity.this, LiftActivity.class));
                                        }
                                    });
                            liftGroup.add(item);
                        }
                        adapter.add(liftGroup);
                    }
                });
        adapter.add(searchItem);

        lifts = new ArrayList<>();
        liftGroup = new Section();

        db.liftDao().getAllLifts()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<Lift>, List<LiftItem>>() {
                    @Override
                    public List<LiftItem> apply(List<Lift> lifts) throws Exception {
                        List<LiftItem> items = new ArrayList<>(lifts.size());
                        for (Lift lift : lifts) {
                            LiftItem item = new LiftItem(lift);
                            item.getObservable()
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(@NonNull Long o) throws Exception {
                                            startActivity(LiftActivity.getIntent(MainActivity.this, o));
                                        }
                                    });
                            items.add(item);
                        }
                        return items;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LiftItem>>() {
                    @Override
                    public void accept(List<LiftItem> liftItems) throws Exception {
                        liftGroup.addAll(liftItems);
                    }
                });
        adapter.add(liftGroup);
    }
}
