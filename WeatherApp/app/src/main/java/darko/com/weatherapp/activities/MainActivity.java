package darko.com.weatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import darko.com.weatherapp.R;
import darko.com.weatherapp.adapters.CityAdapter;
import darko.com.weatherapp.db.DBHelper;
import darko.com.weatherapp.model.MultipleCities;
import darko.com.weatherapp.model.Response;
import darko.com.weatherapp.server.ServerEndpoints;
import darko.com.weatherapp.util.Constants;
import darko.com.weatherapp.util.TouchHelper;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView emptyView;
    private SwipeRefreshLayout refreshLayout;

    private ArrayList<Integer> cityIds;
    private List<Response> cities;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        db = new DBHelper(this);

        cities = new ArrayList<>();
        cityIds = db.getCityIds();

        emptyView = (TextView) findViewById(R.id.empty_view);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.main_recycleview);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //refresh view
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListWithCities();
            }
        });
        //recycle view init
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //rest api
        myRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.SERVER_URL)
                .build();
        serverEndpoints = myRestAdapter.create(ServerEndpoints.class);
        //adding toolbar
        setSupportActionBar(toolbar);
        //adding click listeners
        fab.setOnClickListener(this);

        updateListWithCities();

    }

    private void checkForEmpty() {
        if (mAdapter.getItemCount() == 0) {
            showEmptyView();
        } else {
            emptyView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyView(){
        emptyView.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants._CITY_ADDED) {

            Integer cityID = data.getIntExtra(Constants.CITY_ID, -1);
            Log.d(TAG, "city id " + cityID);
            if (cityID > 0) {
                cityIds.add(cityID);
                db.addCity(cityID, cityIds.size());
                updateListWithCities();
            }
        }
    }

    private void updateListWithCities() {
        String s = "";
        if (cityIds.size() > 0) {
            if (cityIds.size() > 0) {
                for (int i : cityIds) {
                    s += i + ",";
                }
                s = s.substring(0, s.length() - 1);
            } else {
                s = cityIds.get(0).toString();
            }
        }
        if (cityIds.size() > 0) {
            showLoadingDialog(getString(R.string.loading_));
            serverEndpoints.getWeatherForMultipleCities(s, getString(R.string.weather_apikey), "metric", new Callback<MultipleCities>() {
                @Override
                public void success(MultipleCities multipleCities, retrofit.client.Response response) {
                    Log.d(TAG, "success " + multipleCities.getCities().size());
                    cities = multipleCities.getCities();
                    mAdapter = new CityAdapter(cities, MainActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    ItemTouchHelper.Callback callback = new TouchHelper((CityAdapter) mAdapter);
                    ItemTouchHelper helper = new ItemTouchHelper(callback);
                    helper.attachToRecyclerView(recyclerView);
                    stopRefresh();
                    dismissProgressDialog();
                    checkForEmpty();
                    Log.d(TAG, "list :" + mAdapter.getItemCount() + " | ");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "error " + error.getUrl() + " | " + error.getMessage());
                    stopRefresh();
                    mAdapter = new CityAdapter(cities, MainActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    checkForEmpty();
                    dismissProgressDialog();
                    showSnackBar(refreshLayout, getString(R.string.error_mai));
                }
            });
        } else {
            showEmptyView();
        }
    }

    private void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(this, AddCityActivity.class), Constants._ADD_CITY);
                break;
        }
    }

    public void removeItem(final int position) {
        final Response response = cities.get(position);
        showSnackBar(refreshLayout, getString(R.string.city_removed), getString(R.string.undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cities.add(position, response);
                db.addCity(response.getId(), position);
                cityIds = db.getCityIds();
                mAdapter.notifyDataSetChanged();
                checkForEmpty();
            }
        });
        cities.remove(position);
        db.removeCityFromDb(cityIds.get(position));
        cityIds = db.getCityIds();
        mAdapter.notifyDataSetChanged();
        checkForEmpty();

    }

}
