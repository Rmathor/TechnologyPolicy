package com.mathor.technologypolicy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author: mathor
 * Date : on 2017/11/13 17:12
 * 自定义圆角imageview
 */
public class RoundCornersImageView extends ImageView {
    private float radiusX;
    private float radiusY;

    public RoundCornersImageView(Context context) {
        this(context, null);

    }

    public RoundCornersImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornersImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * @param rx x方向弧度
     * @param ry y方向弧度
     */
    public void setRadius(float rx, float ry) {
        this.radiusX = rx;
        this.radiusY = ry;
    }

    private void init() {
        radiusX = 25;
        radiusY = 25;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        RectF rectF = new RectF(rect);
        path.addRoundRect(rectF, radiusX, radiusY, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);//Op.REPLACE这个范围内的都将显示，超出的部分覆盖
        super.onDraw(canvas);
    }
}

