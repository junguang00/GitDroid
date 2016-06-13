package feicuiedu.com.gitdroid.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.commons.ActivityUtils;
import feicuiedu.com.gitdroid.github.model.CurrentUser;
import feicuiedu.com.gitdroid.github.login.LoginActivity;
import feicuiedu.com.gitdroid.main.MainActivity;

/**
 * This is the common "splash screen" of this app, of course
 * it is the entry point.
 *
 * <p/>
 * 这是app中常见的“闪屏”页面，它当然是应用的入口页面。
 */
public class SplashActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        CurrentUser.clear();
        setContentView(R.layout.activity_splash);
    }

    /**
     * This method is invoked right after {@link #setContentView(int)},
     * you can initialize all the views here, such as binding views to this activity,
     * setting listeners for widgets or setting adapters for ListView.
     *
     * <p/>
     * 此方法在setContentView之后调用，你可以在这里做视图初始化工作。例如视图和Activity的绑定，
     * 给控件设置监听器，或者给ListView设置适配器等等。
     */
    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    /**
     * Navigation method, invoking when the buttons are clicked, binding through {@link ButterKnife}.
     *
     * <p/>
     * 导航方法，当按钮被点击后调用，通过ButterKnife绑定。
     * @param view Login button or enter button.
     */
    @OnClick({R.id.btnLogin, R.id.btnEnter})
    public void navigateToMain(View view){
        switch (view.getId()){
            case R.id.btnLogin:
                activityUtils.startActivity(LoginActivity.class);
                break;
            case R.id.btnEnter:
                activityUtils.startActivity(MainActivity.class);
                break;
        }
        finish();
    }

}
