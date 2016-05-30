package feicuiedu.com.gitdroid.github.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class RepoResult {

    @SerializedName("items")
    private List<Repo> repoList;

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("incomplete_results")
    private boolean incompleteResults;

    public List<Repo> getRepoList() {
        return repoList;
    }

    public boolean getIncompleteResults() {
        return incompleteResults;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
