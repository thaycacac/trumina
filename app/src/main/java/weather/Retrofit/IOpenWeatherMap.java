package weather.Retrofit;


import io.reactivex.Observable;
import objects.WeatherResult;
import retrofit2.http.Query;
import retrofit2.http.GET;

public interface IOpenWeatherMap {

    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);
}
