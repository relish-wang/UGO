package wang.relish.ugo.base;

/**
 * <pre>
 *     author : 王鑫
 *     e-mail : wangxin@souche.com
 *     time   : 2017/05/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface BaseView {

    void showMessage(int resId);

    void showMessage(CharSequence str);

    void showLoading(boolean isShown);
}
