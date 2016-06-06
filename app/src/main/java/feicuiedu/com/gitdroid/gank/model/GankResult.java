package feicuiedu.com.gitdroid.gank.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class GankResult {

    private List<String> category;

    private boolean error;

    private Results results;

    public List<String> getCategory() {
        return category;
    }

    public Results getResults() {
        return results;
    }

    public boolean isError() {
        return error;
    }

    public static class Results {

        @SerializedName("Android")
        private List<GankItem> androidItems;

        public List<GankItem> getAndroidItems() {
            return androidItems;
        }
    }
}
