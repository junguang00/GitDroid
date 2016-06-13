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

/**
 * This Java interface represents all the RESTful server interface from GitHub we used in this application.
 *
 * <p/>
 * 这个Java接口代表本应用中用到的所有GitHub服务器RESTful接口。
 */
public interface GitHubApi {

    String ENDPOINT = "https://api.github.com/";

    String CALL_BACK = "gitdog";

    String AUTH_HEADER = "Authorization";
    String AUTH_TOKEN = " token ";

    String CLIENT_ID = "c360b01e5ef05890248b";
    String CLIENT_SECRET = "56cc538bdde4960008198819d50b5c3281f91e24";

    // 授权时申请的可访问域
    String INITIAL_SCOPE = "user,public_repo,repo";

    // WebView加载此路径来显示GitHub的登录页面
    String AUTH_URL = "https://www.github.com/login/oauth/authorize?client_id=" +
            CLIENT_ID + "&" + "scope=" + INITIAL_SCOPE;

    /**
     * @param query 查询参数
     * @param pageId 查询页数，从1开始
     * @return 查询结果
     */
    @GET("search/repositories")
    Call<RepoResult> searchRepos(@Query("q") String query, @Query("page") int pageId);

    /**
     * 获取OAuth 2.0协议的AccessToken
     * @param client @see {@link #CLIENT_ID}
     * @param clientSecret @see {@link #CLIENT_SECRET}
     * @param code 授权码
     * @return 授权结果
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("https://github.com/login/oauth/access_token")
    Call<AccessTokenRsp> getOAuthToken(@Field("client_id") String client,
                                       @Field("client_secret") String clientSecret,
                                       @Field("code") String code);

    /**
     * @return 获取用户信息
     */
    @GET("user")
    Call<User> getUserInfo();

    /**
     * 获取一个Markdown内容对应的HTMl页面
     * @param body 请求体，内容来自{@link RepoContent#content}
     * @return Call对象
     */
    @Headers({
            "Content-Type: text/plain"
    })
    @POST("markdown/raw")
    Call<ResponseBody> markdown(@Body RequestBody body);

    /**
     * @param owner 仓库的拥有者
     * @param repo 仓库的名称
     * @return 仓库的Readme页面的内容，{@link RepoContent#content}将是Markdown格式。
     */
    @GET("repos/{owner}/{repo}/readme")
    Call<RepoContent> getReadme(@Path("owner") String owner, @Path("repo") String repo);
}
