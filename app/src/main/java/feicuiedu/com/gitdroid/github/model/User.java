package feicuiedu.com.gitdroid.github.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class User implements Serializable{

    // 登录所用的账号
    private String login;

    // 用户名
    private String name;

    private int id;

    // 用户头像路径路径
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

    public String getName() {
        return name;
    }
}
