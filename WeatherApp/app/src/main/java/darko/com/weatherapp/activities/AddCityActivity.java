package darko.com.weatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import darko.com.weatherapp.R;
import darko.com.weatherapp.db.DBHelper;
import darko.com.weatherapp.model.Response;
import darko.com.weatherapp.server.ServerEndpoints;
import darko.com.weatherapp.util.Constants;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by Petkovski on 01.02.2016.
 */
public class AddCityActivity extends BaseActivity {
    private final String TAG = AddCityActivity.class.getSimpleName();

    private TextInputLayout cityInputLayout;
    private EditText cityEditText;
    private Button addCityBtn;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        init();
    }

    private void init() {
        db = new DBHelper(this);
        //init views;
        cityInputLayout = (TextInputLayout) findViewById(R.id.input_layout_city);
        cityEditText = (EditText) findViewById(R.id.input_city);
        cityEditText.addTextChangedListener(new MyTextWatcher(cityInputLayout));
        addCityBtn = (Button) findViewById(R.id.add_city_btn);

        addCityBtn.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
        }


    }

    /**
     * City validation code
     */
    private boolean validateCity() {
        final String cityName = cityEditText.getText().toString();
        if (cityName.isEmpty()) {
            cityInputLayout.setError(getString(R.string.enter_city));
            requestFocus(cityEditText);
            return false;
        } else {
            cityInputLayout.setErrorEnabled(false);
            getCityFromServer(cityName);
        }

        return true;
    }

    private void getCityFromServer(String cityName) {
        showLoadingDialog(getString(R.string.loading_));
        myRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.SERVER_URL)
                .build();
        serverEndpoints = myRestAdapter.create(ServerEndpoints.class);
        serverEndpoints.getWheater(cityName, getString(R.string.weather_apikey), "metric", new Callback<Response>() {
            @Override
            public void success(Response response, retrofit.client.Response response2) {
                dismissProgressDialog();
                if (response.getCod() == 200) {
                    final Integer CITY_ID = response.getId();
                    if (!db.checkIfCityAdded(CITY_ID)) {
                        Log.d(TAG, "success " + response.getMain().getTemp());
                        Intent data = new Intent();
                        data.putExtra(Constants.CITY_ID, CITY_ID);
                        setResult(Constants._CITY_ADDED, data);
                        finish();
                    } else {
                        showToast(cityEditText.getText().toString() + getString(R.string.already_added));
                    }
                } else {
                    showSnackBar(addCityBtn, getString(R.string.error_getting_city));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "error " + error.getUrl() + " | " + error.getMessage());
                dismissProgressDialog();
                showSnackBar(addCityBtn, getString(R.string.error_getting_city));
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.add_city_btn:
                validateCity();
                break;
        }
    }
}
