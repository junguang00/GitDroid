package feicuiedu.com.gitdroid.github.network;


import feicuiedu.com.gitdroid.github.model.AccessTokenRsp;
import feicuiedu.com.gitdroid.github.model.RepoContent;
import feicuiedu.com.gitdroid.github.model.RepoResult;
import feicuiedu.com.gitdroid.github.model.User;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApi {

    String ENDPOINT = "https://api.github.com/";

    String CALL_BACK = "gitdog";

    String AUTH_HEADER = "Authorization";
    String AUTH_TOKEN = " token ";

    String CLIENT_ID = "c360b01e5ef05890248b";
    String CLIENT_SECRET = "56cc538bdde4960008198819d50b5c3281f91e24";

    String INITIAL_SCOPE = "user,public_repo,repo";

    String AUTH_URL = "https://www.github.com/login/oauth/authorize?client_id=" +
            CLIENT_ID + "&" + "scope=" + INITIAL_SCOPE;

    @GET("search/repositories")
    Call<RepoResult> searchRepos(@Query("q") String query, @Query("page") int pageId);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("https://github.com/login/oauth/access_token")
    Call<AccessTokenRsp> getOAuthToken(@Field("client_id") String client,
                                       @Field("client_secret") String clientSecret,
                                       @Field("code") String code);

    @GET("user")
    Call<User> getUserInfo();

    @Headers({
            "Content-Type: text/plain"
    })
    @POST("markdown/raw")
    Call<ResponseBody> markdown(@Body RequestBody body);

    @GET("repos/{owner}/{repo}/readme")
    Call<RepoContent> getReadme(@Path("owner") String owner, @Path("repo") String repo);
}
