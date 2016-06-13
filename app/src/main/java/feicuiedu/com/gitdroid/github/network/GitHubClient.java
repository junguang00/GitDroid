package feicuiedu.com.gitdroid.github.network;


import feicuiedu.com.gitdroid.commons.LoggingInterceptor;
import feicuiedu.com.gitdroid.github.model.AccessTokenRsp;
import feicuiedu.com.gitdroid.github.model.RepoContent;
import feicuiedu.com.gitdroid.github.model.RepoResult;
import feicuiedu.com.gitdroid.github.model.User;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple wrapper class for {@link GitHubApi}'s implementation.
 */
public final class GitHubClient implements GitHubApi{

    private static GitHubClient sClient;

    public static GitHubClient getInstance(){
        if (sClient == null) {
            sClient = new GitHubClient();
        }

        return sClient;
    }

    private final GitHubApi gitHubApi;

    private GitHubClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())  // 添加拦截器，处理AccessToken
                .addInterceptor(new LoggingInterceptor()) // 添加拦截器，打印请求和响应的日志
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT) // 设置url根节点
                .client(okHttpClient) // 设置OkHttpClient
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .build();

        // 由Retrofit来负责GitHubApi接口的实现
        gitHubApi = retrofit.create(GitHubApi.class);
    }

    @Override public Call<RepoResult> searchRepos(String query, int pageId) {
        return gitHubApi.searchRepos(query, pageId);
    }

    @Override public Call<AccessTokenRsp> getOAuthToken(String client, String clientSecret, String code) {
        return gitHubApi.getOAuthToken(client, clientSecret, code);
    }

    @Override public Call<RepoContent> getReadme(String owner, String repo) {
        return gitHubApi.getReadme(owner, repo);
    }

    @Override public Call<User> getUserInfo() {
        return gitHubApi.getUserInfo();
    }

    @Override public Call<ResponseBody> markdown(RequestBody body) {
        return gitHubApi.markdown(body);
    }


}
