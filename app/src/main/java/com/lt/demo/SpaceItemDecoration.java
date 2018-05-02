package com.lt.demo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


/**
 * Created by lt on 2018/4/9.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

    private static final java.lang.String TAG = "SpaceItemDecoration";
    private int mSpanCount;
    private int mSpace;
    private Paint mPaint;
    private int mMaxSpanGroupIndex;

    /**
     * 获取Item的偏移量
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 获取位置
        int position = parent.getChildAdapterPosition(view);
        view.setTag(position);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            mSpanCount =  gridLayoutManager.getSpanCount();
            mMaxSpanGroupIndex = spanSizeLookup.getSpanGroupIndex(parent.getAdapter().getItemCount() - 1, mSpanCount);
            int spanSize = spanSizeLookup.getSpanSize(position);
            int spanIndex = spanSizeLookup.getSpanIndex(position, mSpanCount);
            int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, mSpanCount);
            Log.d(TAG, "getItemOffsets: spanIndex:" + spanIndex);
            if (spanSize <mSpanCount && spanIndex != 0) {
                // 左边需要偏移
                outRect.left = mSpace;
            }
            if(spanGroupIndex != 0) {
                outRect.top = mSpace;
            }
        }else if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if(position != 0) {
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    outRect.left = mSpace;
                } else {
                    outRect.top = mSpace;
                }
            }
        }
    }

    public SpaceItemDecoration(int space, int spaceColor) {
        this.mSpace = space;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(spaceColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 绘制分割线
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager) {
            drawSpace(c, parent);
        }else if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if(linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL){
                // 画竖直分割线
                drawVertical(c,parent);
            }else{
                // 画横向分割线
                drawHorizontal(c,parent);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        // 画竖直分割线
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int top,bottom,left,right;
        for(int i=0;i<parent.getChildCount();i++){
            View child = parent.getChildAt(i);
            int position = (int) child.getTag();
            // 判断是否位于边缘
            if(position == linearLayoutManager.getItemCount()-1) continue;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom()+layoutParams.bottomMargin;
            bottom = top + mSpace;
            left = child.getLeft() - layoutParams.leftMargin;
            right = child.getRight() + layoutParams.rightMargin;
            c.drawRect(left,top,right,bottom,mPaint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        // 画竖直分割线
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int top,bottom,left,right;
        for(int i=0;i<parent.getChildCount();i++){
            View child = parent.getChildAt(i);
            int position = (int) child.getTag();
            // 判断是否位于边缘
            if(position == 0) continue;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getTop()-layoutParams.topMargin;
            bottom = child.getBottom()+layoutParams.bottomMargin;
            left = child.getLeft() - layoutParams.leftMargin - mSpace;
            right = left + mSpace;
            c.drawRect(left,top,right,bottom,mPaint);
        }
    }

    /**
     * 绘制分割线
     * @param canvas
     * @param parent
     */
    private void drawSpace(Canvas canvas, RecyclerView parent) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = gridLayoutManager.getSpanCount();
        GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        int childCount = parent.getChildCount();
        int top,bottom,left,right;
        for(int i=0;i<childCount;i++){
            // 绘制思路，以绘制bottom和left为主，top和right不绘制，需要判断出当前的item是否位于边缘，位于边缘的item不绘制bottom和left，你懂得
            View child = parent.getChildAt(i);
            int position = (int) child.getTag();
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount);
            int spanSize = spanSizeLookup.getSpanSize(position);
            int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
            // 画bottom分割线,如果没到达底部，绘制bottom
            if(spanGroupIndex < mMaxSpanGroupIndex) {
                top = child.getBottom() + layoutParams.bottomMargin;
                bottom = top + mSpace;
                left = child.getLeft() - layoutParams.leftMargin; // 不需要外边缘
                right = child.getRight() + layoutParams.rightMargin + mSpace;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
            // 画left分割线
           if (spanSize != mSpanCount && spanIndex!=0) {
                // 左边需要分割线，开始绘制
               top = child.getTop() - layoutParams.topMargin;
               bottom = child.getBottom() + layoutParams.bottomMargin;
               left = child.getLeft() - layoutParams.leftMargin - mSpace;
               right = left + mSpace;
               canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
