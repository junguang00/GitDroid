package feicuiedu.com.gitdroid.github;


import feicuiedu.com.gitdroid.github.entity.RepoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GitHubApi {

    String ENDPOINT = "https://api.github.com/";

    @GET("search/repositories")
    Call<RepoResult> searchRepos(@Query("q") String query, @Query("page") int pageId);
}
