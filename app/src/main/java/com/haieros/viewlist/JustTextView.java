package com.haieros.viewlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;


/**
 * Created by Kang on 2018/7/10.
 */
public class JustTextView extends TextView {
    String mText;
    private Paint mTextPaint;
    private float mMeasureTextLength;
    private int mEnglishLength;
    private float mChineseLength;
    private float mTextHeight;
    private boolean setHeight = false;
    private int start;
    private int end;
    private int mMarginLeft;
    private int mMarginRight;
    private int mMarginTop;

    public JustTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        String namespace = "http://schemas.android.com/apk/res/android";
        int textSize = attrs.getAttributeIntValue(namespace, "textSize", 15);//字体大小
        int textColor = attrs.getAttributeIntValue(namespace, "textColor", Color.parseColor("#8c9fae"));//字体颜色
        //float lineSpacingExtra = attrs.getAttributeFloatValue(namespace, "lineSpacingExtra", 0.0f);//行间距
        int lineSpacingExtraId = attrs.getAttributeResourceValue(namespace, "lineSpacingExtra", 0);
        float lineSpacingExtra = context.getResources().getDimension(lineSpacingExtraId);
//        int paddingLeft = attrs.getAttributeIntValue(namespace, "paddingLeft", 0);
//        int paddingRight = attrs.getAttributeIntValue(namespace, "paddingRight", 0);
        mMarginLeft = attrs.getAttributeIntValue(namespace, "marginLeft", 0);
        mMarginRight = attrs.getAttributeIntValue(namespace, "marginRight", 0);
        mMarginTop = attrs.getAttributeIntValue(namespace, "marginTop", 0);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JustTextView, 0, 0);
        int space = a.getInt(R.styleable.JustTextView_space, 0);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(sp2px(context, textSize));
        mTextPaint.setAntiAlias(true);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        //行高 + 行间距
        mTextHeight = metrics.descent - metrics.ascent + lineSpacingExtra;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //  Log.e("kang", "Class---JustTextView---Method---onLayout---:");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //    Log.e("kang", "Class---JustTextView---Method---onSizeChanged---:");
        super.onSizeChanged(w, h, oldw, oldh);
        int height = getHeight();
//        Log.e("kang", "Class---JustTextView---Method---onSizeChanged---height:" + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.e("kang","Class---JustTextView---Method---onDraw---:");
        super.onDraw(canvas);
        //Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        //mTextHeight = (int) (Math.ceil(metrics.descent - metrics.ascent)) + 5;
        int length = mText.length();
        //Layout layout = getLayout();
        //int lineCount = layout.getLineCount();
        //Log.e("kang", "Class---JustTextView---Method---onDraw---lineCount:" + lineCount);
        int measuredWidth = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        //view宽度
        int textNeedWidth = measuredWidth - paddingLeft - paddingRight;
        //  Log.e("kang","Class---JustTextView---Method---onDraw---:"+textNeedWidth+"="+measuredWidth+"-"+paddingLeft+"-"+paddingRight);
        //文字大于一行长度
        if (mMeasureTextLength >= textNeedWidth) {
            //所需行数
            int lines = calculateLines(textNeedWidth-mEnglishLength);
//            Log.e("kang", "Class---JustTextView---Method---onDraw---:lines:" + lines);

            //平均每行 字数
            int count = length / lines;
//            Log.e("kang", "Class---JustTextView---Method---onDraw---:count:" + count);
            for (int i = 0; i < lines; i++) {
//                Log.e("kang", "Class---JustTextView---Method---onDraw---:line-----------:" + i);
//                //计算实际 每行字符 个数
                int realCount = calculateCount(start, 1, textNeedWidth);
//                Log.e("kang", "Class---JustTextView---Method---onDraw---:realcount:" + realCount);
//                //结束位置
                end = start + realCount;
//                Log.e("kang", "Class---JustTextView---Method---onDraw---:start:" + start + ",end:" + end);
                String s = mText.substring(start, end);
                //       Log.e("kang", "Class---JustTextView---Method---onDraw---:result:" + s + ",start:" + start + ",end:" + end);
//                Log.e("kang", "Class---JustTextView---Method---onDraw---:mTextHeight:" + mTextHeight);
                //  Log.e("kang","Class---JustTextView---Method---onDraw---:"+(i + 1) * mTextHeight);
                canvas.drawText(s, 0, (i + 1) * mTextHeight, mTextPaint);
//                canvas.drawText(mText,start,end,0, (i + 1) * mTextHeight, mTextPaint);
                start += realCount;
                if (i == lines - 1) {
                    //绘制完成 start 指向 0
                    start = 0;
                }
            }

        } else {
            //文字 小于一行长度
            canvas.drawText(mText, 0, mTextHeight, mTextPaint);
        }
    }

    private int calculateLines(int textNeedWidth) {
        float lines = mMeasureTextLength / textNeedWidth;
        int ceil = (int) Math.ceil(lines);
//        Log.e("kang", "Class---JustTextView---Method---calculateLines---:ceil" + ceil + ",lines:" + lines);
        return ceil;
    }

    private int calculateCount(int start, int count, int textNeedWidth) {
//        Log.e("kang", "Class---JustTextView---Method---calculateCount---:start+count:==" + (start + count));
//        Log.e("kang", "Class---JustTextView---Method---calculateCount---:" + mText.length());
        int newCount = count;

        if (start + count >= mText.length()) {
            return mText.length() - start;
        }
        //行的长度
        float lineLength = mTextPaint.measureText(mText.substring(start, start + newCount));

        //小于 view宽度
        while (lineLength < textNeedWidth && textNeedWidth - lineLength > mChineseLength) {
            newCount += 1;
            if (start + newCount >= mText.length()) {
                return mText.length() - start;
            }
            lineLength = mTextPaint.measureText(mText.substring(start, start + newCount));
        }
        return newCount;
    }

    public void setText1(String text) {
        this.start = 0;
        this.end = 0;
        this.mText = text;
        this.mMeasureTextLength = mTextPaint.measureText(mText);
        // Log.e("kang","Class---JustTextView---Method---setText1---mMeasureTextLength:"+mMeasureTextLength+",length:"+mText.length());
        mEnglishLength = (int) mTextPaint.measureText("a");
        mChineseLength = mTextPaint.measureText("我");
        //Log.e("kang", "Class---JustTextView---Method---setText1---:" + text);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, width);
        //Log.e("kang", "Class---JustTextView---Method---onMeasure---:width:" + width + ",height:" + height);
        setMeasuredDimension(width, height);
    }

    private int measureHeight(int heightMeasureSpec, int width) {
        int result = 100;
        int lines = calculateLines(width-mEnglishLength);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightSpecMode) {
            case MeasureSpec.EXACTLY:
                result = heightSpecSize;
                break;
            case MeasureSpec.AT_MOST://wrap_content
            case MeasureSpec.UNSPECIFIED:
                result = (int) Math.ceil(lines * mTextHeight + (mTextHeight / 4));
                break;
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 100;
        //通过MeasureSpec解析获取mode与size
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthSpecMode) {
            case MeasureSpec.EXACTLY: //match_parent
                result = widthSpecSize;
                break;
            case MeasureSpec.AT_MOST: //未设置
            case MeasureSpec.UNSPECIFIED:
                result = 100;
                break;
        }
        return result;
    }

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    public int getLength(String input, int charLength, int doubleCharLength) {
        int length = 0;
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] < '\177') {
                Log.e("kang", "Class---JustTextView---Method---ToSBC---:" + c[i]);
                length += charLength;
            } else {
                length += doubleCharLength;
            }
        }
        return length;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal   dp value
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal   sp value
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

}
