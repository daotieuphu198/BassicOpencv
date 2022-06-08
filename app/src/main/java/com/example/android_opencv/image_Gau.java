package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class image_Gau extends AppCompatActivity  {
    ImageView imggau;
    SeekBar seekBarGau, seekBarGau2;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_image__gau );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gaussian Blur");
        OpenCVLoader.initDebug ();
//        seekBarGau2 = (SeekBar)findViewById(R.id.seebargau2);
        seekBarGau = (SeekBar)findViewById(R.id.seebargau);
        imggau  = (ImageView)findViewById ( R.id.imggau );
        imggau.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );

        for (int b = 0; b<=100;b++ ){


            if (k!=0) {
                seekBarGau.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int proprogress, boolean fromUser) {
                        Log.d("aaaa", "gia tri"+ proprogress);

                        k = proprogress;
                        convexBitmap();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Log.d("Start", "oke");
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Log.d("end", "end");
                    }
                });
            } else {
                convexBitmap();

            }
            k=k+1;

        }



    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        GaussianBlur(bitmap);
    }
    private void GaussianBlur(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.GaussianBlur ( src,dest,new Size (21,21), k);
        Bitmap img_Bitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( dest,img_Bitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imggau2 );
        imageView.setImageBitmap ( img_Bitmap );

    }




}