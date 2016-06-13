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

    private final GankCache gankCache;

    private final Callback<GankResult> gankCallback = new Callback<GankResult>() {
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

        if (gankCache.isEmpty(date)) { // 如果当天没有干货数据
            getView().showEmpty();
        } else if (gankItems != null) { // 如果当天的干货数据已经缓存
            getView().hideEmpty();
            getView().setData(gankItems);
        } else {
            // 从服务器获取每日干货数据
            getDailyGanks(date);
        }

    }

    private void getDailyGanks(Date date){
        if (gankCall != null) gankCall.cancel();

        // 从Date实例中获取年、月、日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 访问服务器接口
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
