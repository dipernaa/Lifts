package connorhenke.com.lifts;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import connorhenke.com.lifts.viewmodels.Set;

public class SetItem extends Item<ViewHolder> {

    private Set set;

    public SetItem(Set set) {
        this.set = set;
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView reps = viewHolder.itemView.findViewById(R.id.list_set_reps);
        TextView weight = viewHolder.itemView.findViewById(R.id.list_set_weight);

        reps.setText("" + set.getReps() + " reps");
        weight.setText(set.getWeight() + " weight");
    }

    @Override
    public int getLayout() {
        return R.layout.list_set;
    }
}
