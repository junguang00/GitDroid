package feicuiedu.com.gitdroid.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.commons.ActivityUtils;
import feicuiedu.com.gitdroid.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_splash);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnLogin, R.id.btnEnter})
    public void navigateToMain(View view){
        switch (view.getId()){
            case R.id.btnLogin:
                activityUtils.showToast("navigateToMain: login!");
                break;
            case R.id.btnEnter:
                activityUtils.startActivity(MainActivity.class);
                break;
        }

    }

}
