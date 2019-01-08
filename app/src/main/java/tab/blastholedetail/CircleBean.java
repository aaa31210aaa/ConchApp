package tab.blastholedetail;

import android.support.annotation.ColorInt;

public class CircleBean {


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int x;
    private int y;

    @ColorInt
    private int color;

    public CircleBean(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
