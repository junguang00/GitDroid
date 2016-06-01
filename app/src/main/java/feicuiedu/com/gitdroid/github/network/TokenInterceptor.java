package feicuiedu.com.gitdroid.github.network;


import java.io.IOException;

import feicuiedu.com.gitdroid.github.model.CurrentUser;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class TokenInterceptor implements Interceptor{

    @Override public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();

        if (CurrentUser.hasAccessToken()) {
            builder.header(GitHubApi.AUTH_HEADER, GitHubApi.AUTH_TOKEN + CurrentUser.getAccessToken());
        }

        Response response = chain.proceed(builder.build());

        if (response.isSuccessful()) {
            return response;
        }

        if (response.code() == 401 || response.code() == 403) {
            throw new IOException("unauthorized! Limit is 10 times per minute.");
        } else {
            throw new IOException("response code = " + response.code());
        }
    }
}
