package feicuiedu.com.gitdroid.github.model;


public class CurrentUser {

    private CurrentUser(){}

    private static String accessToken;

    private static User user;

    public static void setAccessToken(String accessToken) {
        CurrentUser.accessToken = accessToken;
    }

    public static void setUser(User user) {
        CurrentUser.user = user;
    }

    public static void clear(){
        accessToken = null;
        user = null;
    }

    public static User getUser(){
        return user;
    }

    public static String getAccessToken(){
        return accessToken;
    }

    public static boolean isEmpty(){
        return accessToken == null || user == null;
    }

    public static boolean hasAccessToken(){
        return accessToken != null;
    }
}
