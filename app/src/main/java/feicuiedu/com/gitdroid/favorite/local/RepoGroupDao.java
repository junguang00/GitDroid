package feicuiedu.com.gitdroid.favorite.local;


import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import feicuiedu.com.gitdroid.favorite.model.RepoGroup;

public class RepoGroupDao {

    private final Dao<RepoGroup, Long> dao;

    public RepoGroupDao(DbHelper dbHelper) {
        try {
            dao = dbHelper.getDao(RepoGroup.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RepoGroup> queryForAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RepoGroup queryForId(long id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createOrUpdate(RepoGroup repoGroup){
        try {
            dao.createOrUpdate(repoGroup);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createOrUpdateAll(List<RepoGroup> repoGroups){
        for (RepoGroup repoGroup : repoGroups) {
            createOrUpdate(repoGroup);
        }
    }
}
