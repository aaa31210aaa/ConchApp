package tab.blastholedetail;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.conchapp.R;

import java.util.List;


public class TableView extends View {

    public int getRow() {
        return mRow;
    }

    public void setRow(int mRow) {
        this.mRow = mRow;
        invalidate();
    }

    public int getColumn() {
        return mColumn;
    }

    public void setColumn(int mColumn) {
        this.mColumn = mColumn;
        invalidate();
    }

    public float getCircleRadius() {
        return mCircleRadius;
    }

    public void setCircleRadius(float mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
        invalidate();
    }

    public List<CircleBean> getCircleBeanList() {
        return mCircleBeanList;
    }

    public void setmCircleBeanList(List<CircleBean> mCircleBeanList) {
        this.mCircleBeanList = mCircleBeanList;
        invalidate();
    }

    //横向行数
    private int mRow;

    //纵向列数
    private int mColumn;

    //圆圈半径
    private float mCircleRadius;

    //需要显示的圆
    private List<CircleBean> mCircleBeanList;


    private Paint mPaint;


    private int measuredWidth;
    private int measuredHeight;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init();
    }

    private void getAttrs(Context context, @Nullable AttributeSet attrs) {
        //获取自定义的属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        mRow = typedArray.getInt(R.styleable.TableView_row, 5);
        mColumn = typedArray.getInt(R.styleable.TableView_column, 5);
        mCircleRadius = typedArray.getDimension(R.styleable.TableView_circle_radius, 10);
        typedArray.recycle();
    }

    private void init() {
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        //去抖
        mPaint.setDither(true);
        mPaint.setStrokeWidth(1);
        //实心
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                //声明任意的大小
                width = this.getLayoutParams().width;
                break;
            case MeasureSpec.EXACTLY:
                //父容器指定大小
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                //父容器没有对我有任何限制
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = this.getLayoutParams().height;
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(width, height);
        measuredWidth = this.getMeasuredWidth();
        measuredHeight = this.getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCircleBeanList == null || mCircleBeanList.size() == 0) {
            return;
        }

        float xDiff = measuredWidth / mRow;
        float yDiff = measuredHeight / mColumn;

        for (CircleBean circleBean : mCircleBeanList) {
            if (circleBean.getX() < 0 || circleBean.getY() < 0 || circleBean.getX() > mRow || circleBean.getY() > mColumn) {
                continue;
            }

            mPaint.setColor(circleBean.getColor());

            //画圆
            canvas.drawCircle(
                    xDiff / 2 + circleBean.getX() * xDiff,
                    yDiff / 2 + circleBean.getY() * yDiff,
                    mCircleRadius,
                    mPaint
            );
        }
    }
}
