package feicuiedu.com.gitdroid.favorite.model;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import feicuiedu.com.gitdroid.github.model.Repo;

public class RepoConverter {

    private RepoConverter(){}

    public static @NonNull LocalRepo convert(@NonNull Repo repo) {
        LocalRepo localRepo = new LocalRepo();
        localRepo.setAvatar(repo.getOwner().getAvatar());
        localRepo.setDescription(repo.getDescription());
        localRepo.setFullName(repo.getFullName());
        localRepo.setId(repo.getId());
        localRepo.setName(repo.getName());
        localRepo.setStarCount(repo.getStarCount());
        localRepo.setForkCount(repo.getForkCount());
        return localRepo;
    }

    public static @NonNull List<LocalRepo> convertAll(@NonNull List<Repo> repos) {
        ArrayList<LocalRepo> localRepos = new ArrayList<>();
        for (Repo repo : repos) {
            localRepos.add(convert(repo));
        }
        return localRepos;
    }
}
