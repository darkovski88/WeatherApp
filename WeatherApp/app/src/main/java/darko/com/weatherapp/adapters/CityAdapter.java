package darko.com.weatherapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import darko.com.weatherapp.R;
import darko.com.weatherapp.activities.CityDetailsActivity;
import darko.com.weatherapp.activities.MainActivity;
import darko.com.weatherapp.model.Response;
import darko.com.weatherapp.util.Constants;

/**
 * Created by Petkovski on 01.02.2016.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private List<Response> data;
    private Activity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CityAdapter(List<Response> myDataset, Activity activity) {
        data = myDataset;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((TextView) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(data.get(position).getName() + ", " + data.get(position).getMain().getTemp().intValue() + (char) 0x00B0);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CityDetailsActivity.class);
                intent.putExtra(Constants.INTENT_CITY, data.get(position));
                activity.startActivity(intent);
            }
        });
    }

    public void remove(final int position) {
        ((MainActivity) activity).removeItem(position);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}
