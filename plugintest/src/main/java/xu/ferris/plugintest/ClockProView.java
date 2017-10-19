package xu.ferris.plugintest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**时间控件带天气
 * Created by xff on 2017/10/17.
 */

public class ClockProView extends View {
    protected String dateString;
    protected String timeString;

    protected TextPaint mTimePaint;
    protected float TimeTextWidth;
    protected float TimeTextHeight;
    protected float TimeTextSize = 0;

    protected TextPaint mDatePaint;
    protected float DateTextWidth;
    protected float DateTextHeight;
    protected float DateTextSize = 0; //时间数字的字体大小

    protected int ShadowColor=Color.GRAY;
    protected int TimeTextColor = Color.WHITE; //时间数字的颜色
    protected int DateTextColor = Color.WHITE; //时间数字的颜色
    //日期和时间padding
    protected float padding=0f;

    public ClockProView(Context context) {
        super(context);
        init();
    }

    public ClockProView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockProView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    void init(){
        mTimePaint = new TextPaint();
        mTimePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTimePaint.setTextAlign(Paint.Align.LEFT);
        mDatePaint = new TextPaint();
        mDatePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mDatePaint.setTextAlign(Paint.Align.LEFT);
    }
    public void timeChage(){
        float h=getHeight();
        if(h<=0)
            return;
        TimeTextSize=120f/200f*h;
        DateTextSize=20f/200f*h;
        setPadding((int)(5/200f*h),(int)(5/200f*h),(int)(5/200f*h),(int)(5/200f*h));
        padding=0;
        timeString=getTime();
        dateString=getDate();
        invalidateTextPaintAndMeasurementsTime(timeString);
        invalidateTextPaintAndMeasurementsDate(dateString+" "+(mTemp==null?"":mTemp));
        postInvalidate();
    }
    protected String getTime() {
        return "";
    }

    protected String getDate() {
        return "";
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        timeChage();
    }
    float timeBaseY=0,dateBaseY;
    protected void invalidateTextPaintAndMeasurementsTime(String mTime) {
        timeString=mTime;
        mTimePaint.setTextSize(TimeTextSize);
        mTimePaint.setColor(TimeTextColor);
        mTimePaint.setFakeBoldText(true);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        mTimePaint.setShadowLayer(5, 3, 3, ShadowColor);
        TimeTextWidth = mTimePaint.measureText(timeString);
        Paint.FontMetrics fontMetrics = mTimePaint.getFontMetrics();
        TimeTextHeight =fontMetrics.descent- fontMetrics.ascent;
        timeBaseY=Math.abs(fontMetrics.ascent);
    }
    protected void invalidateTextPaintAndMeasurementsDate(String mDate) {
        dateString=mDate;
        mDatePaint.setTextSize(DateTextSize);
        mDatePaint.setColor(DateTextColor);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        mDatePaint.setShadowLayer(5, 3, 3, ShadowColor);
        DateTextWidth = mDatePaint.measureText(dateString);
        Paint.FontMetrics fontMetrics = mDatePaint.getFontMetrics();
        DateTextHeight = fontMetrics.descent-fontMetrics.ascent;
        dateBaseY=Math.abs(fontMetrics.ascent);
    }
    @Override
    public void draw(Canvas canvas) {
        float paddingLeft = getPaddingLeft();
        float paddingTop = getPaddingTop();
        float paddingRight = getPaddingRight();
        float paddingBottom = getPaddingBottom();
        float contentWidth = getWidth() - paddingLeft - paddingRight;
        float contentHeight = getHeight() - paddingTop - paddingBottom;
        float centetPaddingTop=(contentHeight-TimeTextHeight-padding-DateTextHeight)/2;
        float centetPaddingLeftTime=(contentWidth-TimeTextWidth)/2;
        float centetPaddingLeftData=(contentWidth-DateTextWidth)/2;

        canvas.drawText(timeString,
                paddingLeft + centetPaddingLeftTime,
                paddingTop+centetPaddingTop+timeBaseY,
                mTimePaint);
        float offsetPaddingIcon=DateTextHeight*0.1f;
        float offsetDatePadding=mIcon==null?0:-(DateTextHeight+offsetPaddingIcon)/2;
        canvas.drawText(dateString,
                paddingLeft + centetPaddingLeftData+offsetDatePadding,
                paddingTop+centetPaddingTop+padding+TimeTextHeight+dateBaseY,
                mDatePaint);

        if(mIcon!=null){
            int left=(int)(paddingLeft + centetPaddingLeftData+DateTextWidth+offsetPaddingIcon+offsetDatePadding);
            int top=(int)(paddingTop+centetPaddingTop+padding+TimeTextHeight);
            int right=left+(int)DateTextHeight;
            int bottom=top+(int)DateTextHeight;
            mIcon.setBounds(left,top,right,bottom);
            mIcon.draw(canvas);
        }
    }
    String mTemp;
    Drawable mIcon;
    public void update(String mTemp,Drawable mIcon) {
        this.mTemp=mTemp;
        this.mIcon=mIcon;
        timeChage();
    }
    //点击在时间
    public boolean isClickOnTime(int x,int y){
        float paddingLeft = getPaddingLeft();
        float paddingTop = getPaddingTop();
        float paddingRight = getPaddingRight();
        float paddingBottom = getPaddingBottom();
        float contentWidth = getWidth() - paddingLeft - paddingRight;
        float contentHeight = getHeight() - paddingTop - paddingBottom;
        float centetPaddingTop=(contentHeight-TimeTextHeight-padding-DateTextHeight)/2;
        float centetPaddingLeftTime=(contentWidth-TimeTextWidth)/2;


        int left=(int)(paddingLeft + centetPaddingLeftTime);
        int top=(int)(paddingTop+centetPaddingTop);
        int right=left+(int)TimeTextWidth;
        int bottom=top+(int)TimeTextHeight;
        Rect rect=new Rect(left,top,right,bottom);
        if(rect.contains(x,y))
            return true;
        return false;
    }
    //点击在日期
    public boolean isClickOnDate(int x,int y){
        float paddingLeft = getPaddingLeft();
        float paddingTop = getPaddingTop();
        float paddingRight = getPaddingRight();
        float paddingBottom = getPaddingBottom();
        float contentWidth = getWidth() - paddingLeft - paddingRight;
        float contentHeight = getHeight() - paddingTop - paddingBottom;
        float centetPaddingTop=(contentHeight-TimeTextHeight-padding-DateTextHeight)/2;

        float centetPaddingLeftData=(contentWidth-DateTextWidth)/2;
        float offsetPaddingIcon=DateTextHeight*0.1f;
        float offsetDatePadding=mIcon==null?0:-(DateTextHeight+offsetPaddingIcon)/2;

        int left=(int)(paddingLeft + centetPaddingLeftData+offsetDatePadding);
        int top=(int)(paddingTop+centetPaddingTop+padding+TimeTextHeight);
        int right= (int) (left+DateTextWidth+DateTextHeight+offsetPaddingIcon);
        int bottom=top+(int)DateTextHeight;
        Rect rect=new Rect(left,top,right,bottom);
        if(rect.contains(x,y))
            return true;
        return false;
    }




    @Override
    public boolean performClick() {
        if(isClickOnDate(x,y)){
            iClockListem.clickDate();
        }else if(isClickOnTime(x,y)){
            iClockListem.clickTime();
        }        return super.performClick();
    }

    int x=0,y=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            x= (int) event.getX();
            y= (int) event.getY();
        }
        return super.onTouchEvent(event);
    }
    protected  IClockListem iClockListem;
    public void setClockListem(IClockListem iClockListem){
        this.iClockListem=iClockListem;
    }
    public interface IClockListem{
        void clickTime();
        void clickDate();
    }
}
