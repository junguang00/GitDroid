package feicuiedu.com.gitdroid.github.model;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Entity class for a programming language, such as "Java" or "Ruby".
 *
 * In this class, we use <a href="http://commons.apache.org/proper/commons-io/">apache commons io</a>
 * library to simplify IO stream operation. Apache commons is a series of reusable Java libraries,
 * it is very useful and can do a lot of repetitive work for us. Another choice is to use google's
 * <a href="https://github.com/google/guava">guava</a>.
 *
 * <p/>
 * 代表编程语言的实体类，例如Java或Ruby。
 *
 * 本类中，我们用到了阿帕奇的commons io库来简化IO流操作。阿帕奇commons是一系列可重用的Java库，它是很有用的，
 * 可以帮我们做很多重复性工作。另一种选择是使用google的guava库。
 */
@SuppressWarnings("unused")
public class Language implements Serializable{

    // 用于搜索的路径
    private String path;
    // 编程语言的名称(用于在TabLayout上显示)
    private String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static List<Language> DEFAULT_LANGS;

    public static List<Language> getDefaultLanguages(Context context){
        if (DEFAULT_LANGS != null) return DEFAULT_LANGS;

        try {
            // 默认语言配置是一个json文件，路径为assets/langs.json
            InputStream inputStream = context.getAssets().open("langs.json");
            // 将流转换为字符串
            String content = IOUtils.toString(inputStream);
            // 将字符串转换为对象数组
            Gson gson = new Gson();
            DEFAULT_LANGS =  gson.fromJson(content, new TypeToken<List<Language>>(){}.getType());
            return DEFAULT_LANGS;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
