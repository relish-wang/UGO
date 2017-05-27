package wang.relish.ugo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
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
public abstract class BaseFgm extends Fragment implements BaseView {

    protected abstract int layoutId();

    protected abstract void initViews(View v);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        ButterKnife.bind(this,view);
        initViews(view);
        return view;
    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(CharSequence str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    WaitDialog dialog;

    @Override
    public void showLoading(boolean isShown) {
        if (isShown) {
            if (dialog == null) {
                dialog = new WaitDialog();
            }
            if (!dialog.isAdded()) {
                dialog.show(getActivity().getSupportFragmentManager(), "show");
            }
        } else {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }
}
