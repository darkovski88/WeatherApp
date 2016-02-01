package darko.com.weatherapp.server;

import java.util.ArrayList;

import darko.com.weatherapp.model.MultipleCities;
import darko.com.weatherapp.model.Response;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Created by Petkovski on 01.02.2016.
 */
public interface ServerEndpoints {
    @GET("/weather")
    void getWheater(@Query("q") String cityName, @Query("appid") String appID, @Query("units") String units, Callback<Response> callback);

    @GET("/group")
    void getWeatherForMultipleCities(@Query("id") String ids,@Query("appid") String appID,  @Query("units") String units, Callback<MultipleCities> callback);

}
