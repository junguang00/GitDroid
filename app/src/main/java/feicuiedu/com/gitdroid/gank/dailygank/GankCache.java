package feicuiedu.com.gitdroid.gank.dailygank;


import android.util.LruCache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import feicuiedu.com.gitdroid.gank.model.GankItem;

/**
 * 一个简单的内存缓存实现，用来缓存得到的干货数据。
 */
public class GankCache {

    private static GankCache sInstance;

    public static GankCache getInstance(){
        if (sInstance == null) {
            sInstance = new GankCache();
        }

        return sInstance;
    }

    // 用来存储某天没有干货数据
    private final HashMap<String, Boolean> emptyMap;

    // 用来存储某天的干货数据
    private final LruCache<String, List<GankItem>>  lruCache;

    // 日期转换，用来将Date转换为字符串，作为HashMap和LruCache的Key
    private final SimpleDateFormat simpleDateFormat;

    private GankCache(){
        emptyMap = new HashMap<>();

        lruCache = new LruCache<>(100);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }

    public void setEmpty(Date date, boolean isEmpty){
        emptyMap.put(getKey(date), isEmpty);
    }

    public void setGankData(Date date, List<GankItem> gankItems){
        if (gankItems == null || gankItems.isEmpty()) return;
        lruCache.put(getKey(date), gankItems);
    }

    public boolean isEmpty(Date date){
        Boolean value = emptyMap.get(getKey(date));
        return value == null? false : value;
    }

    public List<GankItem> getGankData(Date date){
        return lruCache.get(getKey(date));
    }

    private String getKey(Date date){
        return simpleDateFormat.format(date);
    }
}
