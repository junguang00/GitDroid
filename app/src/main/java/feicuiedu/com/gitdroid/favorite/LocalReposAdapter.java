package feicuiedu.com.gitdroid.favorite;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.favorite.model.LocalRepo;
import feicuiedu.com.gitdroid.github.network.AvatarLoadOptions;

/**
 * This is just a very simple ListView's adapter, nothing new here.
 */
class LocalReposAdapter extends BaseAdapter {


    private final ArrayList<LocalRepo> data;
    private final DisplayImageOptions options;

    public LocalReposAdapter(Context context) {
        data = new ArrayList<>();
        // 图片加载选项
        options = AvatarLoadOptions.build(context);
    }

    @Override public int getCount() {
        return data.size();
    }

    @Override public LocalRepo getItem(int position) {
        return data.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.layout_item_repo, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        LocalRepo repo = data.get(position);

        ImageLoader.getInstance().displayImage(repo.getAvatar(), viewHolder.ivIcon, options);
        viewHolder.tvRepoName.setText(repo.getFullName());
        viewHolder.tvRepoInfo.setText(repo.getDescription());
        viewHolder.tvRepoStars.setText(String.format("stars : %d", repo.getStarCount()));

        return convertView;
    }
    public void setData(@Nullable Collection<LocalRepo> repos) {
        data.clear();
        if (repos != null)
        data.addAll(repos);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.ivIcon) ImageView ivIcon;
        @Bind(R.id.tvRepoName) TextView tvRepoName;
        @Bind(R.id.tvRepoInfo) TextView tvRepoInfo;
        @Bind(R.id.tvRepoStars) TextView tvRepoStars;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
