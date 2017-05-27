package wang.relish.ugo.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import wang.relish.ugo.R;

/**
 * <pre>
 *     author : 王鑫
 *     e-mail : wangxin@souche.com
 *     time   : 2017/05/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public abstract class BaseDialog<T> extends DialogFragment {

    protected abstract int layoutId();

    protected abstract void initViews();

    protected abstract T generateT();

    protected void parseArguments(Bundle arguments) {

    }

    TextView tv_cancel;
    TextView tv_complete;

    protected OnCompletedListener<T> mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_base, container, false);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_complete = (TextView) view.findViewById(R.id.tv_complete);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onComplete(generateT());
                }
            }
        });
        ButterKnife.bind(this, view);
        LinearLayout ll_child_root = (LinearLayout) view.findViewById(R.id.ll_child_root);
        View child_layout = inflater.inflate(layoutId(), container, false);
        ll_child_root.addView(child_layout);
        initViews();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(layoutId());
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        Bundle bundle = getArguments();
        parseArguments(bundle);
        return dialog;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        Dialog dialog = getDialog();
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            window.setAttributes(lp);
            return super.show(transaction, tag);
        } else {
            return -1;//不显示Dialog
        }
    }

    public void setListener(OnCompletedListener<T> listener) {
        this.mListener = listener;
    }

    interface OnCompletedListener<T> {
        void onComplete(T t);
    }

}
