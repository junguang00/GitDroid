package feicuiedu.com.gitdroid.gank.network;


import feicuiedu.com.gitdroid.gank.model.GankResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 接口来自代码家的干货集中营：http://gank.io/api。
 */
interface GankApi {

    String ENDPOINT = "http://gank.io/api/";

    @GET("day/{year}/{month}/{day}")
    Call<GankResult> getDailyData(@Path("year") int year, @Path("month") int month, @Path("day") int day);
}
