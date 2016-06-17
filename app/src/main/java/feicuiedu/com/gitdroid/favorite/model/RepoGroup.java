package feicuiedu.com.gitdroid.favorite.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings("unused")
@DatabaseTable(tableName = "repository_groups")
public class RepoGroup {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    private static List<RepoGroup> DEFAULT_GROUPS;

    public static List<RepoGroup> getDefaultGroups(Context context) {
        if (DEFAULT_GROUPS != null) return DEFAULT_GROUPS;

        try {
            InputStream inputStream = context.getAssets().open("repogroup.json");
            // 将流转换为字符串
            String content = IOUtils.toString(inputStream);
            // 将字符串转换为对象数组
            Gson gson = new Gson();
            DEFAULT_GROUPS = gson.fromJson(content, new TypeToken<List<RepoGroup>>() {
            }.getType());
            return DEFAULT_GROUPS;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
