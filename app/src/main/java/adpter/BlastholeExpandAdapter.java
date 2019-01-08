package adpter;

import android.support.v4.app.Fragment;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.administrator.conchapp.R;
import com.leon.lfilepickerlibrary.adapter.ExpandableAdapter;
import com.leon.lfilepickerlibrary.model.Lv0Item;
import com.leon.lfilepickerlibrary.model.Lv1Item;

import java.util.List;

public class BlastholeExpandAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ExpandableAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    private String lv0Title = "";
    private Fragment fragment;
    private Lv1Item bean;

    public BlastholeExpandAdapter(Fragment fragment, List<MultiItemEntity> data, Lv1Item bean) {
        super(data);
        this.fragment = fragment;
        this.bean = bean;
        addItemType(TYPE_LEVEL_0, R.layout.blasthole_lv0_item);
        addItemType(TYPE_LEVEL_1, R.layout.blasthole_lv1_item);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Lv0Item lv0 = (Lv0Item) item;
                lv0Title = lv0.getTitle();
                holder.setText(R.id.title, lv0.title)
                        .setImageResource(R.id.iv, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Lv1Item lv1 = (Lv1Item) item;
                holder.setText(R.id.title1, "作业地点：" + lv1.getProjectname());
                holder.setText(R.id.title2, "炮孔数：" + lv1.getPks());
                holder.setText(R.id.title3, "分组情况：" + lv1.getSffz());
                holder.setText(R.id.title4, "上传时间：" + lv1.getUploadtime());
//                        .setImageResource(R.id.iv, lv1.isExpanded() ? R.drawable.arrow_b : R.drawable.arrow_r);
                break;
        }
    }
}
