package feicuiedu.com.gitdroid.gank.dailygank;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import feicuiedu.com.gitdroid.gank.model.GankItem;
import feicuiedu.com.gitdroid.gank.model.GankResult;
import feicuiedu.com.gitdroid.gank.network.GankClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GankPresenter extends MvpNullObjectBasePresenter<GankView>{

    private Date date;

    private GankCache gankCache;

    private Callback<GankResult> gankCallback = new Callback<GankResult>() {
        @Override public void onResponse(Call<GankResult> call, Response<GankResult> response) {
            GankResult gankResult = response.body();


            if (gankResult == null
                    || gankResult.isError()
                    || gankResult.getResults() == null
                    || gankResult.getResults().getAndroidItems() == null) {

                gankCache.setEmpty(date, true);
                getView().showEmpty();
                return;
            }

            List<GankItem> items = gankResult.getResults().getAndroidItems();

            if (items.isEmpty()) {
                gankCache.setEmpty(date, true);
                getView().showEmpty();
                return;
            }

            gankCache.setEmpty(date, false);
            getView().hideEmpty();
            gankCache.setGankData(date, items);
            getView().setData(items);

        }

        @Override public void onFailure(Call<GankResult> call, Throwable t) {
            getView().showMessage("Error:" + t.getMessage());
            gankCache.setEmpty(date, false);
            getView().showEmpty();
        }
    };

    private Call<GankResult> gankCall;

    public GankPresenter() {
        gankCache = GankCache.getInstance();
    }

    public void init(Date date){
        this.date = date;

        List<GankItem> gankItems = gankCache.getGankData(date);

        if (gankCache.isEmpty(date)) {
            getView().showEmpty();
        } else if (gankItems != null) {
            getView().hideEmpty();
            getView().setData(gankItems);
        } else {
            getDailyGanks(date);
        }

    }

    private void getDailyGanks(Date date){
        if (gankCall != null) gankCall.cancel();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        gankCall = GankClient.getInstance().getDailyData(year, month, day);

        gankCall.enqueue(gankCallback);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (!retainInstance) {
            if (gankCall != null) gankCall.cancel();
        }
    }
}
