package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.TodoBean;

public class TodoAdapter extends BaseQuickAdapter<TodoBean, BaseViewHolder> {


    public TodoAdapter(@LayoutRes int layoutResId, @Nullable List<TodoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodoBean item) {
        helper.setText(R.id.todo_name, "待办名称:" + item.getMattername());
        helper.setText(R.id.todo_content, "待办内容:" + item.getMemo());
        helper.setText(R.id.todo_addtime, "待办时间:" + item.getAddtime());
    }
}
