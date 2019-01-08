package utils;


import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import com.example.administrator.conchapp.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;

public class SwipeMenu  {
    private static SwipeMenuCreator swipeMenuCreator;

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    public static SwipeMenuCreator CreateSwipeMenu(Context context) {
        swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(com.yanzhenjie.recyclerview.swipe.SwipeMenu swipeLeftMenu, com.yanzhenjie.recyclerview.swipe.SwipeMenu swipeRightMenu, int viewType) {
                int width = context.getResources().getDimensionPixelSize(R.dimen.dimen_70);
                // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
                // 2. 指定具体的高，比如80;
                // 3. WRAP_CONTENT，自身高度，不推荐;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;

                // 添加左侧的，如果不添加，则左侧不会出现菜单。
//                {
//                    SwipeMenuItem addItem = new SwipeMenuItem(context)
//                            .setBackground(R.drawable.selector_red)
//                            .setImage(R.drawable.ic_action_add)
//                            .setWidth(width)
//                            .setHeight(height);
//                    swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。
//
//                    SwipeMenuItem closeItem = new SwipeMenuItem(context)
//                            .setBackground(R.drawable.selector_red)
//                            .setImage(R.drawable.ic_action_add)
//                            .setWidth(width)
//                            .setHeight(height);
//                    swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
//                }

                // 添加右侧的，如果不添加，则右侧不会出现菜单。
                {
                    SwipeMenuItem addItem = new SwipeMenuItem(context)
                            .setBackground(R.drawable.selector_red)
                            .setImage(R.drawable.modify)
                            .setText("")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(addItem);// 添加菜单到右侧。

                    SwipeMenuItem deleteItem = new SwipeMenuItem(context)
                            .setBackground(R.drawable.selector_gray)
                            .setImage(R.drawable.delete)
                            .setText("")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(deleteItem); // 添加菜单到右侧。
                }
            }
        };
        return swipeMenuCreator;
    }
}
