package feicuiedu.com.gitdroid.favorite;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.favorite.local.DbHelper;
import feicuiedu.com.gitdroid.favorite.local.LocalRepoDao;
import feicuiedu.com.gitdroid.favorite.local.RepoGroupDao;
import feicuiedu.com.gitdroid.favorite.model.LocalRepo;
import feicuiedu.com.gitdroid.favorite.model.RepoGroup;

public class FavoriteFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    @Bind(R.id.tvGroupType) TextView tvGroupType;
    @Bind(R.id.listView) ListView listView;

    private LocalReposAdapter localReposAdapter;
    private LocalRepoDao localRepoDao;
    private RepoGroupDao repoGroupDao;
    private LocalRepo selectedRepo;

    private int groupId;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localRepoDao = new LocalRepoDao(DbHelper.getInstance(getContext()));
        repoGroupDao = new RepoGroupDao(DbHelper.getInstance(getContext()));
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        localReposAdapter = new LocalReposAdapter(getContext());
        listView.setAdapter(localReposAdapter);

        groupId = R.id.repo_group_all;
        localReposAdapter.setData(localRepoDao.queryForAll());

        registerForContextMenu(listView);
    }

    @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
            selectedRepo = localReposAdapter.getItem(adapterContextMenuInfo.position);

            MenuInflater menuInflater = getActivity().getMenuInflater();
            menuInflater.inflate(R.menu.menu_context_favorite, menu);

            SubMenu subMenu = menu.findItem(R.id.sub_menu_move).getSubMenu();
            List<RepoGroup> groups = repoGroupDao.queryForAll();
            for (RepoGroup repoGroup : groups) {
                subMenu.add(R.id.menu_group_move, repoGroup.getId(), Menu.NONE, repoGroup.getName());
            }
        }
    }

    @Override public boolean onContextItemSelected(MenuItem item) {

        int groupId = item.getGroupId();
        int id = item.getItemId();
        if (id == R.id.delete){
            localRepoDao.delete(selectedRepo);
            resetData();
            return true;
        } else if (groupId == R.id.menu_group_move){
            if (id == R.id.repo_group_no) {
                selectedRepo.setRepoGroup(null);
            } else {
                selectedRepo.setRepoGroup(repoGroupDao.queryForId(id));
            }
            localRepoDao.createOrUpdate(selectedRepo);
            resetData();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btnFilter)
    public void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu_popup_repo_groups);

        Menu menu = popupMenu.getMenu();
        List<RepoGroup> groups = repoGroupDao.queryForAll();

        for (RepoGroup repoGroup : groups) {
            menu.add(Menu.NONE, repoGroup.getId(), Menu.NONE, repoGroup.getName());
        }

        popupMenu.show();
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
        tvGroupType.setText(item.getTitle());

        groupId = item.getItemId();
        resetData();
        return true;
    }

    private void resetData(){
        if (groupId == R.id.repo_group_all) {
            localReposAdapter.setData(localRepoDao.queryForAll());
        } else if (groupId == R.id.repo_group_no) {
            localReposAdapter.setData(localRepoDao.queryForNoGroup());
        } else {
            localReposAdapter.setData(localRepoDao.queryForGroupId(groupId));
        }
    }
}
