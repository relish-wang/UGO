package wang.relish.ugo.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.ButterKnife;
import wang.relish.ugo.UGOApp;
import wang.relish.ugo.R;
import wang.relish.ugo.util.BarUtil;
import wang.relish.ugo.view.WaitDialog;

/**
 * <pre>
 *     author : 王鑫
 *     e-mail : wangxin@souche.com
 *     time   : 2017/05/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public abstract class BaseAty extends AppCompatActivity implements BaseView {

    /**
     * 布局ID
     *
     * @return int
     */
    protected abstract int layoutId();

    /**
     * 初始化Toolbar
     *
     * @param savedInstanceState 数据
     * @param mToolbar           标题栏
     */
    protected abstract void initToolbar(Bundle savedInstanceState, Toolbar mToolbar);

    /**
     * 初始化所有控件
     *
     * @param savedInstanceState 数据
     */
    protected abstract void initViews(Bundle savedInstanceState);

    private static final String TAG = "BaseActivity";

    protected Toolbar mToolbar;

    private WaitDialog mWaitDialog = null;

    protected void parseIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseIntent(getIntent());

        int resId = layoutId();
        if (resId != 0) {
            if (removeParent()) {
                setContentView(resId);
                ButterKnife.bind(this);
                BarUtil.setBarTransparent(this);
            } else {
                // 在子类和Toolbar上再套一层布局
                LayoutInflater inflater = LayoutInflater.from(this);
                @SuppressLint("InflateParams") ViewGroup clRoot =
                        (ViewGroup) inflater.inflate(R.layout.activity_base, null);


                LinearLayout llRoot = (LinearLayout) clRoot.findViewById(R.id.llRoot);
                // 子类Activity的布局
                View contentView = inflater.inflate(resId, null);
                llRoot.addView(contentView, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                setContentView(clRoot);
                ButterKnife.bind(this,clRoot);
                //沉浸式状态栏
                BarUtil.setStatusBarTransparent(this);
                // 初始化Toolbar
                mToolbar = (Toolbar) clRoot.findViewById(R.id.toolbar);
                //透明状态栏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                // 初始化自定义ActionBar,下面的顺序很重要
                initToolbar(savedInstanceState, mToolbar);
                setSupportActionBar(mToolbar);
                if (isBtnBackEnable()) {
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setHomeButtonEnabled(true);
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    }
                }
            }

            //Activity入栈
            UGOApp.addActivity(this);

            //初始化所有控件
            initViews(savedInstanceState);
        } else {
            Log.e(TAG+"#onCreate", "did you forget set layoutId()?");
        }

    }

    /**
     * 返回键是否生效
     *
     * @return 默认生效，除非子类重写
     */
    protected boolean isBtnBackEnable() {
        return true;
    }

    /**
     * 是否不使用BaseActivity的根布局
     *
     * @return 默认使用，除非子类重写
     */
    protected boolean removeParent() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isBtnBackEnable()) {
                onBackPressed();//厉害了，居然要这么监听
            } else {
                ActionBar mActionBar = getSupportActionBar();
                if (mActionBar != null) {
                    mActionBar.setDisplayHomeAsUpEnabled(false);
                    mActionBar.setHomeButtonEnabled(false);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UGOApp.removeActivities(getClass().getName());
    }

    @Override
    public void showMessage(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean isShown) {
        if (isShown) {
            if (mWaitDialog == null) {
                mWaitDialog = new WaitDialog();
            }
            if (!mWaitDialog.isAdded()) {
                mWaitDialog.show(getSupportFragmentManager(), "");
            }
        } else {
            if (mWaitDialog != null) {
                mWaitDialog.dismiss();
            }
        }
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }
}