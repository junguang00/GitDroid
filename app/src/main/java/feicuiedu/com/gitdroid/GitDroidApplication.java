package feicuiedu.com.gitdroid;


import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * This is the {@link Application} class for this app.
 * Do some initializing work in {@link #onCreate()} method.
 *
 * <p/>
 * 此类是此app的Application类，在onCreate方法中做一些初始化的工作。
 */
public class GitDroidApplication extends Application{

    /**
     * Initialize <a href="https://github.com/nostra13/Android-Universal-Image-Loader">Android-Universal-Image-Loader</a>
     */
    @Override public void onCreate() {
        super.onCreate();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)  // 打开内存缓存
                .cacheOnDisk(true) // 打开硬盘缓存
                .resetViewBeforeLoading(true) // 在ImageView加载前清除它上面之前的图片
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(4 * 1024 * 1024) // 设置内存缓存的大小 (4M)
                .defaultDisplayImageOptions(options) // 设置默认的显示选项
                .build();
        ImageLoader.getInstance().init(config); // 初始化ImageLoader
    }
}
