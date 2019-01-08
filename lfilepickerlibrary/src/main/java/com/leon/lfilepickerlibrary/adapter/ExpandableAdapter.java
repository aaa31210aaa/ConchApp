package com.leon.lfilepickerlibrary.adapter;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leon.lfilepickerlibrary.R;
import com.leon.lfilepickerlibrary.model.Lv0Item;
import com.leon.lfilepickerlibrary.model.Lv1Item;
import com.leon.lfilepickerlibrary.utils.MyDialog;

import java.util.List;


public class ExpandableAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ExpandableAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    private MyDialog dialog;
    private Menu menu;
    private String lv0Title = "";

    public ExpandableAdapter(List<MultiItemEntity> data, MyDialog dialog, Menu menu) {
        super(data);
        this.dialog = dialog;
        this.menu = menu;
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
    }

    public ExpandableAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
//                switch (holder.getLayoutPosition() % 3) {
//                    case 0:
//                        holder.setImageResource(R.id.iv_head, R.drawable.head_img0);
//                        break;
//                    case 1:
//                        holder.setImageResource(R.id.iv_head, R.drawable.head_img1);
//                        break;
//                }
                final Lv0Item lv0 = (Lv0Item) item;
                lv0Title = lv0.getTitle();
                holder.setText(R.id.title, lv0.title)
                        .setImageResource(R.id.iv, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, "Level 0 item pos: " + pos);
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
                holder.setText(R.id.title, lv1.getTitle());
//                        .setImageResource(R.id.iv, lv1.isExpanded() ? R.drawable.arrow_b : R.drawable.arrow_r);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, lv1.getTitle());
                        dialog.dismiss();
                        menu.getItem(1).setTitle(lv0Title + lv1.getTitle());
//                        ShowToast.showShort(mContext,lv1.getTitle());
                    }
                });
                break;
        }
    }
}
