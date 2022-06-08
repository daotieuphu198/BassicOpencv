package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Helper.BitmapHelper;

public class img_finding extends AppCompatActivity {
    ImageView imgfinding;
    int k = 255;
    SeekBar seebarfinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_finding );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Finding contours");
        OpenCVLoader.initDebug ();
        imgfinding = (ImageView)findViewById ( R.id.imgfinding );
        imgfinding.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        seebarfinding = (SeekBar)findViewById(R.id.seebarfindning);
        for (int a = 0 ; a<=100; a++){
            if (k!=255){
                seebarfinding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        k = progress;
                        convexBitmap();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }else {
                convexBitmap();
            }
            k++;
        }

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        finding(bitmap);
    }
    private void finding(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dts = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );

        Mat grayMat = new Mat();
        Mat cannyEdges = new Mat();
        Mat hierarchy = new Mat();
        Utils.bitmapToMat ( bitmap,src );
        List<MatOfPoint> contour = new ArrayList<MatOfPoint> ();


        Imgproc.Canny(src, cannyEdges, 80, 100);
        Imgproc.findContours(cannyEdges, contour, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0, 0));
        hierarchy.release();
        Imgproc.drawContours(dts,contour,-1, new Scalar(Math.random()*k, Math.random()*k, Math.random()*k));

        Bitmap imgBitmap = Bitmap.createBitmap ( dts.cols (),dts.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap(dts,imgBitmap);
        ImageView imageView = (ImageView)findViewById ( R.id.imgfinding2);

        imageView.setImageBitmap ( imgBitmap );
    }
}