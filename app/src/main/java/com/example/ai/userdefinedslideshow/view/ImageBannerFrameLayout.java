package com.example.ai.userdefinedslideshow.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ai.userdefinedslideshow.C;
import com.example.ai.userdefinedslideshow.R;

import java.text.AttributedCharacterIterator;
import java.util.List;

/**
 * Created by AI on 2017/10/19.
 */

public class ImageBannerFrameLayout extends FrameLayout
        implements ImagerBarnerViewGroup.ImageBannerListener, ImagerBarnerViewGroup.ImageBarnnerViewGroupListener{

    private ImagerBarnerViewGroup imagerBarnerViewGroup;
    private LinearLayout linearLayout;

    public FrameLayoutListener getFrameLayoutListener() {
        return frameLayoutListener;
    }

    public void setFrameLayoutListener(FrameLayoutListener frameLayoutListener) {
        this.frameLayoutListener = frameLayoutListener;
    }

    private FrameLayoutListener frameLayoutListener;


    @Override
    public void clickImageIndex(int pos){
        frameLayoutListener.clickImageIndex(pos);
    }

    @Override
    public void selectImage(int index){
        int count=linearLayout.getChildCount();
        for(int i=0;i<count;i++){
            ImageView iv=(ImageView)linearLayout.getChildAt(i);
            if(i==index){
                iv.setImageResource(R.drawable.dot_select);
            }else{
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }

    public interface FrameLayoutListener{
        void clickImageIndex(int pos);
    }

    public ImageBannerFrameLayout(Context context){
        super(context);
        initImageBannerViewGroup();
        initDotLinearlayout();
    }
    public ImageBannerFrameLayout(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        initImageBannerViewGroup();
        initDotLinearlayout();
    }

    public ImageBannerFrameLayout(Context context,
                                  AttributeSet attributeSet,int defStyleAttr){
        super(context,attributeSet,defStyleAttr);
        initImageBannerViewGroup();
        initDotLinearlayout();
    }

    public void addBitmaps(List<Bitmap> list){
        for(int i=0;i<list.size();i++){
            Bitmap bitmap=list.get(i);
            addBitmapToImageBarnnerViewGroup(bitmap);
            addDotToLinearlayout();
        }
    }
    private void addDotToLinearlayout(){
        ImageView iv=new ImageView(getContext());
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        iv.setLayoutParams(lp);
        iv.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(iv);
    }
    private void addBitmapToImageBarnnerViewGroup(Bitmap bitmap){
        ImageView iv=new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new ViewGroup.LayoutParams(C.WIDTH,ViewGroup.LayoutParams.WRAP_CONTENT));
        iv.setImageBitmap(bitmap);
        imagerBarnerViewGroup.addView(iv);
    }

    /**
     * 初始化我们自定义的图片轮播功能核心类
     */
    private void initImageBannerViewGroup(){
        imagerBarnerViewGroup=new ImagerBarnerViewGroup(getContext());
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        imagerBarnerViewGroup.setLayoutParams(lp);
        addView(imagerBarnerViewGroup);
        imagerBarnerViewGroup.setListener(this);
        imagerBarnerViewGroup.setImageBarnnerViewGroupListener(this);
    }

    /**
     * 初始化我们自定义的底部圆点布局
     */
    private void initDotLinearlayout(){
        linearLayout=new LinearLayout(getContext());
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,40);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.RED);
        addView(linearLayout);

        FrameLayout.LayoutParams layoutParams=(LayoutParams)linearLayout.getLayoutParams();
        layoutParams.gravity=Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);
        //设置透明度
        //这里有一个知识点，就是在3.0(api==>11)以后，我们是用的是setAlpha(),
        // 在3.0之前,使用的是setAlpha,但是调用者不同
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            //Build.VERSION.SDK_INT是系统的版本，Build.VERSION_CODES.GINGERBREAD是版本号。
             linearLayout.setAlpha(0.5f);
        }else{
            linearLayout.getBackground().setAlpha(100);
        }
    }


}
