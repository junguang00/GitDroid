package feicuiedu.com.gitdroid.favorite.local;


import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import feicuiedu.com.gitdroid.favorite.model.LocalRepo;

public class LocalRepoDao {

    private final Dao<LocalRepo, Long> dao;

    public LocalRepoDao(DbHelper dbHelper){
        try {
            dao = dbHelper.getDao(LocalRepo.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createOrUpdate(LocalRepo localRepo){
        try {
            dao.createOrUpdate(localRepo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createOrUpdateAll(List<LocalRepo> localRepos){
        for (LocalRepo localRepo : localRepos) {
            createOrUpdate(localRepo);
        }
    }

    public List<LocalRepo> queryForAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocalRepo> queryForGroupId(int groupId){
        try {
            return dao.queryForEq(LocalRepo.COLUMN_GROUP_ID, groupId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocalRepo> queryForNoGroup(){
        try {
            return dao.queryBuilder().where().isNull(LocalRepo.COLUMN_GROUP_ID).query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(LocalRepo localRepo){
        try {
            dao.delete(localRepo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
