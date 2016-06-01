package feicuiedu.com.gitdroid.github.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Repo implements Serializable{

    private String name;

    @SerializedName("full_name")
    private String fullName;

    private String description;

    @SerializedName("stargazers_count")
    private int starCount;

    @SerializedName("forks_count")
    private int forkCount;

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

    public int getForkCount() {
        return forkCount;
    }
}
