package feicuiedu.com.gitdroid.github.model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AccessTokenRsp {

    @SerializedName("access_token") private String accessToken;
    @SerializedName("token_type") private String tokenType;
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
