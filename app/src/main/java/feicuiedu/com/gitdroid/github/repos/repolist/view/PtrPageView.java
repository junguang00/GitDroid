package feicuiedu.com.gitdroid.github.repos.repolist.view;


import java.util.List;

import feicuiedu.com.gitdroid.github.model.Repo;

/**
 * This interface contains both "infinite scroll" and "pull to refresh" functionality.
 *
 * <p/>
 * 本接口同时包含“无限滚动”和“下拉刷新”功能。
 */
public interface PtrPageView extends PtrView<List<Repo>>, LoadMoreView<List<Repo>>{
}
