package feicuiedu.com.gitdroid.github.entity;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Repo {

    private String name;

    @SerializedName("full_name")
    private String fullName;

    private String description;

    @SerializedName("stargazers_count")
    private int starCount;

    private User owner;

    public String getFullName() {
        return fullName;
    }

    public User getOwner() {
        return owner;
    }

    public int getStarCount() {
        return starCount;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
