package feicuiedu.com.gitdroid.github.entity;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class User {

    private String login;
    private int id;

    @SerializedName("avatar_url")
    private String avatar;

    public String getLogin() {
        return login;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getId() {
        return id;
    }
}
