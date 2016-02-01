package darko.com.weatherapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import darko.com.weatherapp.R;
import darko.com.weatherapp.model.Response;
import darko.com.weatherapp.util.Constants;

/**
 * Created by Petkovski on 01.02.2016.
 */
public class CityDetailsActivity extends BaseActivity {
    private final String TAG = CityDetailsActivity.class.getSimpleName();

    private TextView cityName, currentTemp, description, condition;
    private ImageView icon;

    private Response cityObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_details_activity);

        init();
    }

    private void init() {
        cityName = (TextView) findViewById(R.id.details_city_name);
        currentTemp = (TextView) findViewById(R.id.details_temp_now);
        description = (TextView) findViewById(R.id.details_description);
        condition = (TextView) findViewById(R.id.details_conditions);
        icon = (ImageView) findViewById(R.id.details_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cityObject = (Response) getIntent().getSerializableExtra(Constants.INTENT_CITY);

        if (cityObject == null) {
            finish();
            showToast(getString(R.string.error_getting_city));
        } else {
            //Sample data display
            getSupportActionBar().setTitle(cityObject.getName());
            cityName.setText(cityObject.getName() + ", " + cityObject.getSys().getCountry());
            currentTemp.setText(getString(R.string.right_now) + cityObject.getMain().getTemp().intValue() + (char) 0x00B0);
            Picasso.with(this).load(Constants.ICON_URL + cityObject.getWeather().get(0).getIcon() + ".png").fit().into(icon);
            condition.setText(cityObject.getWeather().get(0).getDescription());
            description.setText("Min:" + cityObject.getMain().getTempMin().intValue() + (char) 0x00B0 + "\nMax:" +
                    cityObject.getMain().getTempMax().intValue() + (char) 0x00B0 +
                    "\nWind speed:" + cityObject.getWind().getSpeed() + "km/h");

        }

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
}
