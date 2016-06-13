package feicuiedu.com.gitdroid.github.network;


import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import feicuiedu.com.gitdroid.R;

// 用户头像的加载选项
public class AvatarLoadOptions {

    private AvatarLoadOptions(){
    }

    private static DisplayImageOptions options;

    public static DisplayImageOptions build(Context context){
        if (options == null){
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_avatar) // 如果是空uri显示什么
                    .showImageOnLoading(R.drawable.ic_avatar) // 加载中显示什么
                    .showImageOnFail(R.drawable.ic_avatar) // 加载失败显示什么
                    .cacheInMemory(true) // 开启内存缓存
                    .cacheOnDisk(true) // 开启硬盘缓存
                    .resetViewBeforeLoading(true) // 加载前重置ImageView
                    .displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelSize(R.dimen.dp_6))) // 显示圆角图片
                    .build();
        }

        return options;
    }
}
