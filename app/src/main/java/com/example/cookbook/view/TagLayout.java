package com.example.cookbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TagLayout extends ViewGroup {
    //记录每一行有多高
    List<Integer> lineHeights = new ArrayList<>();
    List<List<View>> views = new ArrayList<>();

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        views.clear();
        lineHeights.clear();
        //1.计算
        //该行有多少列数据
        List<View> lineViews = new ArrayList<>();
        int width = getMeasuredWidth();//容器自己的宽度
        int lineWidth = 0;
        int lineHeight = 0;//这一行的最大高度
        int childCount = getChildCount();
        for (int j = 0; j < childCount; j++) {
            View child = getChildAt(j);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeigh = child.getMeasuredHeight();
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                //超出,换行
                lineHeights.add(lineHeight);
                views.add(lineViews);
                lineWidth = 0;
                lineHeight = 0;
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeigh + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        lineHeights.add(lineHeight);
        views.add(lineViews);
        int left = 0;
        int top = 0;
        //2.摆放
        int size = views.size();
        for (int i = 0; i < size; i++) {
            lineViews = views.get(i);
            lineHeight = lineHeights.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                //遍历这一行的所有child
                View child = lineViews.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;//width=所有行里面最宽的一行
        int height = 0;//height=所有行的高度相加
        //一行的宽度=一行当中的所有view的宽度的和
        int lineWidth = 0;
        int lineHeight = 0;

        //1.测量所有子控件的大小
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //子控件真实占用的宽和高度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //当一行放不下的时候需要换行
            if (lineWidth + childWidth > sizeWidth) {
                //换行
                width = Math.max(lineWidth, width);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {//累加
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //最后一步
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }

        //2.测量并定义自身的大小
        int measuredWidth = (modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width;
        int measuredHeight = (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height;
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
