package com.example.ai.userdefinedslideshow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ai.userdefinedslideshow.view.ImageBannerFrameLayout;
import com.example.ai.userdefinedslideshow.view.ImagerBarnerViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ImageBannerFrameLayout.FrameLayoutListener
        /*implements ImagerBarnerViewGroup.ImageBannerListener*/{
    private ImageBannerFrameLayout mGroup;
    private int[] ids=new int[]{
            R.drawable.screenshot1,R.drawable.screenshot2,
            R.drawable.screenshot3
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //计算当前手机宽度的方法
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        C.WIDTH=displayMetrics.widthPixels;

        mGroup=(ImageBannerFrameLayout) findViewById(R.id.image_group);
        List<Bitmap> list=new ArrayList<Bitmap>();
        for (int i=0;i<ids.length;i++){
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),ids[i]);
            list.add(bitmap);
        }
        mGroup.addBitmaps(list);
        mGroup.setFrameLayoutListener(this);
        /**for(int i=0;i<ids.length;i++){
            ImageView iv=new ImageView(this);
            //让图片宽度充满屏幕
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(new ViewGroup.LayoutParams(width,ViewGroup.LayoutParams.WRAP_CONTENT));

            iv.setImageResource(ids[i]);
            mGroup.addView(iv);
        }
        mGroup.setListener(this);*/
    }

    @Override
    public void clickImageIndex(int pos){
        Toast.makeText(this,"第"+pos+"图片",Toast.LENGTH_SHORT).show();
    }
}
