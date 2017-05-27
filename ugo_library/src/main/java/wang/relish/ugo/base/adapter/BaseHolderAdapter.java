package wang.relish.ugo.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器基础类
 *
 * @author 王鑫
 */
public abstract class BaseHolderAdapter<VH, T> extends BaseAdapter {

    protected List<T> mData;

    public BaseHolderAdapter(List<T> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public final Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        VH holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId(), parent, false);
            holder = createViewHolder(view);
            view.setTag(holder);
        } else {
            //noinspection unchecked
            holder = (VH) view.getTag();
        }
        setItemData(holder, mData.get(position), position);
        return view;
    }

    public void update(ArrayList<T> dataList) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(dataList);
        notifyDataSetChanged();
    }


    /**
     * ListView的Item
     *
     * @return 布局文件的ID
     */
    protected abstract int itemLayoutId();

    /**
     * 为ListView的每个Item设置显示数据，或显示样式。
     * 如：tvName.setText(userEntity.getName());
     *
     * @param holder view集合
     * @param data   需要展示的数据
     */
    protected abstract void setItemData(VH holder, T data, int position);

    /**
     * 创建ViewHolder
     *
     * @param view
     * @return ViewHolder
     */
    protected abstract VH createViewHolder(View view);

    public List<T> getmData() {
        return mData;
    }

    public void setmData(ArrayList<T> mData) {
        this.mData = mData;
    }
}
