package com.qiang.qiangguide.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.util.DensityUtil;

/**
 * Created by Qiang on 2016/7/30.
 * 城市选择右侧字母
 */
public class SideBar extends View {

    private static final int fontSize=12;//sp

    // 26个字母
    public static String[]b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };
    private int	choose	= -1;																																			// 选中
    private Paint paint	= new Paint();
    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*重写onDraw方法*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取焦点改变背景颜色
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;//获取每个字母的高度

        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.GRAY);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            paint.setTextSize(DensityUtil.sp2px(getContext(), fontSize));
            //选中状态
            if (i == choose) {
                paint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
                paint.setFakeBoldText(true);
            }
            //x坐标等于中间-字符宽度的一半
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        //点击Y坐标所占高度的比例 *b 数据的长度就等于点击b中的个数
        final int c = (int)(y/getHeight()*b.length);

        switch(action){
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.WHITE);
                choose=-1;
                invalidate();
                if(mTextDialog!=null){
                    mTextDialog.setVisibility(View.GONE);
                }
                break;

            default:
                setBackgroundResource(R.drawable.side_bar_background);
                if(oldChoose !=c){
                    if(c>=0 && c< b.length){
                        if(mTextDialog!=null){
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(VISIBLE);
                        }
                        choose=c;
                        invalidate();
                        if(listener!=null){
                            listener.onTouchingLetterChanged(b[c]);
                        }
                    }
                }
                break;
        }
        return true;
    }

    // 触摸事件
    private OnTouchingLetterChangedListener	onTouchingLetterChangedListener;

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     *
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}
