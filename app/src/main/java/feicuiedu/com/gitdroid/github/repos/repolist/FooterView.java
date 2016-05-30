package feicuiedu.com.gitdroid.github.repos.repolist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;

public class FooterView extends FrameLayout {

    private static final int STATE_LOADING = 0;
    private static final int STATE_COMPLETE = 1;
    private static final int STATE_ERROR = 2;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.tv_complete)
    TextView tvComplete;

    @Bind(R.id.tv_error)
    TextView tvError;

    private int state = STATE_LOADING;

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.content_load_footer, this, true);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
    }

    public void showLoading() {
        state = STATE_LOADING;
        progressBar.setVisibility(VISIBLE);
        tvComplete.setVisibility(GONE);
        tvError.setVisibility(GONE);
    }

    public void showError(String error) {
        state = STATE_ERROR;
        progressBar.setVisibility(GONE);
        tvComplete.setVisibility(GONE);
        tvError.setVisibility(VISIBLE);
    }

    public void showComplete() {
        state = STATE_COMPLETE;
        progressBar.setVisibility(GONE);
        tvComplete.setVisibility(VISIBLE);
        tvError.setVisibility(GONE);
    }

    public boolean isLoading(){
        return state == STATE_LOADING;
    }

    public boolean isComplete(){
        return state == STATE_COMPLETE;
    }

    public void setErrorClickListener(OnClickListener onClickListener){
        tvError.setOnClickListener(onClickListener);
    }
}
