package com.example.ai.userdefinedslideshow.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AI on 2017/10/17.
 */

/**
 * 该类是实现图片轮播的核心类
 */
public class ImagerBarnerViewGroup extends ViewGroup {

    private int children;//我们ViewGroup的子视图总个数
    private int Childwidth;//子视图的宽度
    private int Childheight;//子视图的高度

    private int x;//此时的x的值代表的是第一次按下的位置的横坐标、每一次移动过程中移动之前的横坐标
    private int index=0;//代表的是我们每张图片的索引

    private Scroller scroller;

    public ImageBarnnerViewGroupListener getImageBarnnerViewGroupListener() {
        return imageBarnnerViewGroupListener;
    }

    public void setImageBarnnerViewGroupListener(ImageBarnnerViewGroupListener imageBarnnerViewGroupListener) {
        this.imageBarnnerViewGroupListener = imageBarnnerViewGroupListener;
    }

    private ImageBarnnerViewGroupListener imageBarnnerViewGroupListener;


    /**
     * 要想实现图片轮播底部圆点以及底部圆点切换功能步骤思路：
     * 1、我们需要自定义一个继承自FrameLayout的布局，利用FrameLayout布局特性（在同一个位置放置
     * 不同的view最终显示的是最后一个view），我们就可以实现底部圆点的布局。
     * 2、我们需要准备素材，就是底部的素材，我们可以利用Drawable的功能，去实现一个圆点图片的展示
     * 3、我们就需要继承FrameLayout来自定义一个类，在该类的实现过程中，我们去加载我们刚才自定义的
     * ImageBannerViewGroup核心类和我们需要实现的底部圆点的布局LinearLayout来实现
     */
    //自动轮播
    /**
     * 要想实现图片的单击事件的获取，我们采用的方法是利用一个单击变量进行判断，
     * 在用户离开屏幕的那一瞬间我们去判断变量开关来判断用户的操作是点击还是移动
     */
    private boolean isClick;//true的时候，代表的是点击事件false的时候，代表的不是点击事件
    private ImageBannerListener listener;

    public void setListener(ImageBannerListener listener){
        this.listener=listener;
    }
    public ImageBannerListener getListener(){
        return listener;
    }
    public interface ImageBannerListener{
         void clickImageIndex(int pos);//pos代表的是我们当前图片的具体索引值
    }

    private boolean isAuto=true;//默认情况下开启自动轮播
    private Timer timer=new Timer();
    private TimerTask task;
    private Handler autoHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0://此时我们需要图片的自动轮播
                    if(++index>=children){//说明此时是最后一张图片，我们需要从第一张图片开始重新滑动
                        index=0;
                    }
                    scrollTo(Childwidth*index,0);
                    imageBarnnerViewGroupListener.selectImage(index);
                    break;

            }
        }

    };

    private void startAuto(){
        isAuto=true;
    }

    private void stopAuto(){
        isAuto=false;
    }
    /**
     *我们采用Timer，TimerTask，Handler三者相结合的方式来实现自动轮播
     * 我们会抽出两个方法来控制，是否启动自动轮播，我们称之为startAuto(),stopAuto();
     * 那么我们在两个方法的控制过程中，我们实际上希望控制的是自动开启轮播图的开关
     * 那么我们就需要一个变量参数来作为我们自动轮播图的开关，我们称之为isAuto boolean true代表开启 false代表关闭
     */

    public ImagerBarnerViewGroup(Context context){
        super(context);
        initObj();
    }

    /**
     * AttributeSet是自定义控件这个控件节点的属性集合
     * @param context
     * @param attributeSet
     */
    public ImagerBarnerViewGroup(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        initObj();
    }

    public ImagerBarnerViewGroup(Context context,AttributeSet attributeSet,int defStyle){
        super(context,attributeSet,defStyle);
        initObj();
    }
    private void initObj(){
        scroller=new Scroller(getContext());
        task=new TimerTask() {
            @Override
            public void run() {
                   if(isAuto){//开起了轮播
                       autoHandler.sendEmptyMessage(0);
                   }
            }
        };
        timer.schedule(task,100,3000);
    }

    /**
     * 通过invalidate操纵，此方法通过draw方法调用
     */
    @Override
    public void computeScroll(){//computeScroll的作用是计算ViewGroup如何滑动。而computeScroll是通过draw来调用的。
        super.computeScroll();
        if(scroller.computeScrollOffset()){//返回值为true，滚动尚未完成，false，滚动完成
            scrollTo(scroller.getCurrX(),0);//mScroller.getCurrX()的值却是一直在变化的
            //mScroller.getCurrX() //获取mScroller当前水平滚动的位置
            //mScroller.getCurrY() //获取mScroller当前竖直滚动的位置
            invalidate();//重绘动作
        }
    }
    /**
     * 我们在自定义VIewGroup中，我们必须要实现的方法有：测量-》布局-》绘制
     * 那对于我们来说就是：onMeasure()
     * 我们对于绘制来说，因为我们是自定义的ViewGroup容器，针对于容器的绘制，
     * 其实就是容器内的子控件的绘制过程，那么我们只需要调用系统自带的绘制即可
     * 也就是说，对于ViewGroup绘制过程我们不需要再重写该方法
     * 调用系统自带的即可。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        /**
         * 由于我们要实现的是一个ViewGroup的容器，
         * 那么我们就应该知道该容器中的所有子视图
         * 我们想要测量我们的ViewGroup的高度和宽度，
         * 那么我们就必须先要去测量子视图的宽度和高度之和
         * 才知道我们的ViewGroup的高度和宽度是多少
         */
        //1.求出子视图的个数
        children=getChildCount();//我们就可以知道子视图的个数
        if(children==0){
            setMeasuredDimension(0,0);
        }else{
            //2.测量子视图的高度和宽度
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //此时我们以第一个子视图为基准，
            // 也就是说我们的ViewGroup的高度就是我们第一个子视图的高度，宽度就是我们
            //第一个子视图的宽度*子视图的个数
            View view=getChildAt(0);//children不为0，第一个子视图绝对存在
            //3.根据子视图的高度和宽度，来求出该ViewGroup的高度和宽度
            Childwidth=view.getMeasuredWidth();
            Childheight=view.getMeasuredHeight();
            int width=(view.getMeasuredWidth())*children;//所有子视图的宽度总和
            setMeasuredDimension(width,Childheight);//赋值给ViewGroup高宽
        }

    }
    /**
     * 事件的传递过程中的调用方法：我们我们需要调用容器的拦截方法onInterceptTouchEvent
     *针对于该方法我们可以理解为 如果说 该方法的返回值为true的时候，
     * 那么我们自定义的ViewGroup
     * 容器就会处理此次拦截事件，如果说返回值为false的时候，
     * 那么我们自定义的ViewGroup容器不会接受
     * 此次事件的处理过程，将会继续向下传递该事件，针对于我们自定义的ViewGroup 我们
     * 当然希望ViewGroup容器处理接受事件，那么我们的返回值就是true
     * 如果返回值为true的话，真正处理该事件的方法，是onTouchEvent方法
     */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        return true;
    }

    /**
     * 用两种方式来实现轮播图的手动 轮播
     * 1.利用 scrollTo、scrollBy 完成轮播图的手动轮播
     * 2、利用Scroller对象完成轮播图的手动轮播
     *
     * 第一：我们在 滑动屏幕图片的过程中，其实就是我们自定义ViewGroup的子视图的移动过程，
     * 那么我们只需要知道滑动之前的横坐标和滑动之后的横坐标，此时我们就可以求出我们此过程中
     * 移动的距离，我们再利用scrollBy方法实现图片的滑动，所以，我们需要两个值：
     * 移动之前和移动之后的横坐标
     *
     * 第二：在我们第一次按下的那一瞬间，此时的移动之前和移动之后的值是相等的，
     * 也就是我们此时按下那一瞬间的那一个点的横坐标的值
     *
     * 第三：我们在不断滑动的过程中，是会不断地调用我们ACTION_MOVE方法，那么此时我们就应该
     * 将移动之前的值和移动之后的进行保存，以便我们能够算出滑动的距离
     *
     * 第四：在我们抬起的那一瞬间，我们需要计算出我们此时将要滑动到哪张图片的位置上
     * 我们此时就需要求得出将要滑动到的那张图片的索引值
     * (我们当前ViewGroup的滑动位置+我们每张图片的宽度/2) / 我们每一张图片的宽度值
     * 此时我们就可以利用scrollTo方法，滑动到该图片的位置上
     *
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //收拾探测 GestureDetector
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN://表示的是用户按下的一瞬间
                stopAuto();
                if(!scroller.isFinished()){
                    scroller.abortAnimation();//终止图片滑动，防止混乱滑动
                }
                isClick=true;
                x=(int)event.getX();//getX()和getY()是触摸点相对于view本身的坐标
                Log.d("ACTION_DOWN:", "onTouchEvent: 按下的横坐标"+x);
                break;
            case MotionEvent.ACTION_MOVE://表示的是用户按下之后 在屏幕上移动的过程，会不断调用
                int moveX=(int)event.getX();
                int distance=moveX-x;
                Log.d("ACTION_MOVE:", "onTouchEvent: 移动的距离"+distance);
                scrollBy(-distance,0);
                x=moveX;
                isClick=false;
                break;
            case MotionEvent.ACTION_UP://表示的是用户抬起的一瞬间
                int scrollX=getScrollX();//getScrollX() 就是当前view的左上角相对于ViewGroup的左上角的X轴偏移量。
                Log.d("ACTION_UP:", "onTouchEvent:scrollX="+scrollX);
                index=(scrollX+Childwidth/2)/Childwidth;
                Log.d("ACTION_UP", "onTouchEvent:index="+index);
                if(index<0){//说明了此时已经滑动到了左边第一张图片
                    index=0;
                }else if(index>children-1){//说明了此时已经滑动到了最右最后一张图片
                    index=children-1;
                }

                if(isClick){//代表点击事件
                    listener.clickImageIndex(index);
                }else{

                    //scrollTo(index*Childwidth,0);
                    int dx=index*Childwidth-scrollX;
                    scroller.startScroll(scrollX,0,dx,0);
                    postInvalidate();
                    imageBarnnerViewGroupListener.selectImage(index);
                }
                startAuto();
                break;
            default:
                break;
        }
        return true;//返回true的目的是告诉 我们的ViewGroup容器的父View 我们已经处理好了该事件
    }

    /**
     * 继承ViewGroup必须要实现布局onLayout方法
     * b代表的是当我们的ViewGroup布局位置发生改变的为true，没有发生改变为false
     */
    @Override
    protected void onLayout(boolean change,int l,int t,int r,int b){
         if(change){
             int leftMargin=0;
             for(int i=0;i<children;i++){
                 View view=getChildAt(i);
                 view.layout(leftMargin,t,leftMargin+Childwidth,Childheight);
                 leftMargin+=Childwidth;
             }
         }
    }

    public interface ImageBarnnerViewGroupListener{
        void selectImage(int index);
    }
}
