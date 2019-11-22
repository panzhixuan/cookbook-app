package com.example.cookbook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.example.cookbook.R;

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private int mBorderThickness = 0;
    private Context mContext;
    private int defaultColor = 0xFFFFFFFF;
    // 濡傛灉鍙湁鍏朵腑涓�涓湁鍊硷紝鍒欏彧鐢讳竴涓渾褰㈣竟妗�
    private int mBorderOutsideColor = 0;
    private int mBorderInsideColor = 0;
    // 鎺т欢榛樿闀裤�佸
    private int defaultWidth = 0;
    private int defaultHeight = 0;

    public CircleImageView(Context context) {

        super(context);
        mContext = context;
    }



    public CircleImageView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }
    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.circleImageView);
        mBorderThickness = a.getDimensionPixelSize(R.styleable.circleImageView_border_thickness, 0);
        mBorderOutsideColor = a.getColor(R.styleable.circleImageView_border_outside_color,defaultColor);
        mBorderInsideColor = a.getColor(R.styleable.circleImageView_border_inside_color, defaultColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable() ;
        if(drawable == null)
        {
            return;
        }
        if(getWidth() == 0|| getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if(drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if(b==null)
            return;
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        if(defaultWidth == 0) {
            defaultWidth = getWidth();
        }
        if(defaultHeight == 0){
            defaultHeight = getHeight();
        }
        int radius = 0;
        if(mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2- 2* mBorderThickness;
            drawCircleBorder(canvas, radius + mBorderThickness / 2,mBorderInsideColor);
            drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
        } else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2- mBorderThickness;
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
        } else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 瀹氫箟鐢讳竴涓竟妗�
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2- mBorderThickness;
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);
        } else{
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;
        }

        Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);

        canvas.drawBitmap(roundBitmap, defaultWidth / 2- radius, defaultHeight / 2- radius, null);
    }




    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;

        if(bmpHeight > bmpWidth) {

            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
        } else if (bmpHeight < bmpWidth) {
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,squareHeight);
        } else{
            squareBitmap = bmp;
        }
        if(squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,diameter, true);
        } else{
            scaledSrcBmp = squareBitmap;
        }

        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth()/ 2,scaledSrcBmp.getHeight() / 2,  scaledSrcBmp.getWidth()/ 2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;

        return output;
    }

    private void drawCircleBorder(Canvas canvas, int radius, int color) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
    }
}

