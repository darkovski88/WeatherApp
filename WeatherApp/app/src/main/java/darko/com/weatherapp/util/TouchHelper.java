package darko.com.weatherapp.util;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import darko.com.weatherapp.adapters.CityAdapter;

/**
 * Created by Petkovski on 01.02.2016.
 */
public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private CityAdapter cityAdapter;

    public TouchHelper(CityAdapter cityAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.cityAdapter = cityAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO: Not implemented here
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        cityAdapter.remove(viewHolder.getAdapterPosition());
    }
}