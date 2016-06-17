package feicuiedu.com.gitdroid.favorite.local;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import feicuiedu.com.gitdroid.commons.LogUtils;
import feicuiedu.com.gitdroid.favorite.model.LocalRepo;
import feicuiedu.com.gitdroid.favorite.model.RepoGroup;

public class DbHelper extends OrmLiteSqliteOpenHelper{

    private static final String TABLE_NAME = "repo_favorites.db";

    private static final int VERSION = 1;

    private static DbHelper sInstance;

    public static synchronized DbHelper getInstance(Context context){
        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private final Context context;

    private DbHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION);
        this.context = context;
    }

    @Override public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            TableUtils.createTableIfNotExists(connectionSource, LocalRepo.class);
            TableUtils.createTableIfNotExists(connectionSource, RepoGroup.class);
            RepoGroupDao repoGroupDao = new RepoGroupDao(this);
            repoGroupDao.createOrUpdateAll(RepoGroup.getDefaultGroups(context));
            LocalRepoDao localRepoDao = new LocalRepoDao(this);
            localRepoDao.createOrUpdateAll(LocalRepo.getDefaultLocalRepos(context));
        } catch (SQLException e) {
            LogUtils.e("DbHelper onCreate", e);
        }

    }

    @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

        try {
            TableUtils.dropTable(connectionSource, LocalRepo.class, true);
            TableUtils.dropTable(connectionSource, RepoGroup.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            LogUtils.e("DbHelper onUpgrade", e);
        }

    }

}
