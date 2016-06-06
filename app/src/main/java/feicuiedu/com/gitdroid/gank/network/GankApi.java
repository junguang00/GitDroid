package feicuiedu.com.gitdroid.gank.network;


import feicuiedu.com.gitdroid.gank.model.GankResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankApi {

    String ENDPOINT = "http://gank.io/api/";

    @GET("day/{year}/{month}/{day}")
    Call<GankResult> getDailyData(@Path("year") int year, @Path("month") int month, @Path("day") int day);
}
