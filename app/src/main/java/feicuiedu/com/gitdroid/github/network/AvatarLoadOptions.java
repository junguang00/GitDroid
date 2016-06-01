package feicuiedu.com.gitdroid.github.network;


import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import feicuiedu.com.gitdroid.R;

public class AvatarLoadOptions {

    private AvatarLoadOptions(){
    }

    private static DisplayImageOptions options;

    public static DisplayImageOptions build(Context context){
        if (options == null){
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_avatar)
                    .showImageOnLoading(R.drawable.ic_avatar)
                    .showImageOnFail(R.drawable.ic_avatar)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .resetViewBeforeLoading(true)
                    .displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelSize(R.dimen.dp_6)))
                    .build();
        }

        return options;
    }
}
