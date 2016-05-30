package feicuiedu.com.gitdroid.github;


import feicuiedu.com.gitdroid.github.entity.RepoResult;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gitHubApi = retrofit.create(GitHubApi.class);
    }

    @Override public Call<RepoResult> searchRepos(String query, int pageId) {
        return gitHubApi.searchRepos(query, pageId);
    }
}
