package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import Helper.BitmapHelper;

public class image_histogramequa extends AppCompatActivity {
    ImageView imghistogramequa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_image_histogramequa );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Historam equalization");
        OpenCVLoader.initDebug ();
        imghistogramequa = (ImageView)findViewById ( R.id.imghistogramequa );
        imghistogramequa.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        convexBitmap();
    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        histogramequa(bitmap);
    }
    private void histogramequa(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.cvtColor ( src,src,Imgproc.COLOR_BGR2YCrCb );
        List<Mat> channels = new ArrayList<> ();
        Core.split ( src,channels );
        Imgproc.equalizeHist ( channels.get ( 0),channels.get ( 0) );
        Core.merge ( channels ,src);
        Imgproc.cvtColor ( src,dest,Imgproc.COLOR_BGR2YCrCb );
        Bitmap img_Bitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap(dest,img_Bitmap);
        ImageView imageView = (ImageView)findViewById ( R.id.imghistogramequa2 );
        imageView.setImageBitmap ( img_Bitmap );


    }
}