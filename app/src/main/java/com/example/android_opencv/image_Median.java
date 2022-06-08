package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class image_Median extends AppCompatActivity  {
    private ImageView imgmedian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_image__median );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Median Blur");
        OpenCVLoader.initDebug ();


            imgmedian = (ImageView)findViewById ( R.id.imgmedian );
            imgmedian.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
            convexBitmap();






    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        MedianBlur(bitmap);
    }
    private void MedianBlur(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.medianBlur ( src,dest,3 );
        Bitmap img_Bitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( dest,img_Bitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imgmedian2 );
        imageView.setImageBitmap ( img_Bitmap );
    }


}