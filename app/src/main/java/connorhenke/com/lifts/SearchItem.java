package connorhenke.com.lifts;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.jakewharton.rxbinding2.support.design.widget.RxTextInputLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SearchItem extends Item<ViewHolder> {

    private PublishSubject<CharSequence> pubSub;

    public SearchItem() {
        pubSub = PublishSubject.create();
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        EditText editText = viewHolder.itemView.findViewById(R.id.list_search_edit_text);
        RxTextView.textChanges(editText)
                .observeOn(Schedulers.computation())
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(pubSub);
    }

    @Override
    public int getLayout() {
        return R.layout.list_search;
    }

    public Observable<CharSequence> getObservable() {
        return pubSub;
    }
}
