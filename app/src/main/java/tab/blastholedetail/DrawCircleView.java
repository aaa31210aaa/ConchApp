package tab.blastholedetail;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class DrawCircleView extends Button {
    public static float[] currentX;
    public static float[] currentY;
    //定义、并创建画笔
    Paint p = new Paint();
    /**
     * 绘制时控制文本绘制的范围
     */
    public static int[] mcolor;
    private static Context mcontext;


    public static void DrawCircle(Context context, float[] x, float[] y, int[] color) {
        mcontext = context;
        currentX = x;
        currentY = y;
        mcolor = color;
    }

    public DrawCircleView(Context context) {
        super(context);
    }

    public DrawCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  绘制小球
        for (int i = 0; i < currentX.length; i++) {
            final int a = i;
            p.setColor(mcolor[i]);
            canvas.drawCircle(currentX[i], currentY[i], 15, p);

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
