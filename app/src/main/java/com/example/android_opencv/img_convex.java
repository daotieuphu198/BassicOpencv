package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Helper.BitmapHelper;

public class img_convex extends AppCompatActivity {
    ImageView imgconvex;
    int l = 256;
    SeekBar seebarconvex;
    private Mat srcGray = new Mat();

    private static final int MAX_THRESHOLD = 255;
    private int threshold = 100;
    private Random rng = new Random(12345);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_convex );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Convex Hull");
        OpenCVLoader.initDebug ();
        seebarconvex = (SeekBar)findViewById(R.id.seebarconvex);
        imgconvex = (ImageView)findViewById ( R.id.imgconvex );
        imgconvex.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
       for(int a = 0 ; a <= 100 ; a++){
           if(l!=256){
               seebarconvex.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                   @Override
                   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                       l = progress;
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
           l++;
       }
    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        convex(bitmap);
    }
    private void convex(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (),CvType.CV_8UC4);
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(srcGray, srcGray, new Size(3, 3));
        Mat cannyOutput = new Mat();
        Imgproc.Canny(srcGray, cannyOutput, threshold, threshold * 2);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        List<MatOfPoint> hullList = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(contour, hull);
            Point[] contourArray = contour.toArray();
            Point[] hullPoints = new Point[hull.rows()];
            List<Integer> hullContourIdxList = hull.toList();
            for (int i = 0; i < hullContourIdxList.size(); i++) {
                hullPoints[i] = contourArray[hullContourIdxList.get(i)];
            }
            hullList.add(new MatOfPoint(hullPoints));
        }
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(rng.nextInt(l), rng.nextInt(l), rng.nextInt(l));
            Imgproc.drawContours(drawing, contours, i, color);
            Imgproc.drawContours(drawing, hullList, i, color );
        }

        Bitmap imgbitmap = Bitmap.createBitmap ( drawing.cols (),drawing.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( drawing,imgbitmap );
        ImageView imageView = (ImageView)findViewById(R.id.imgconvex2);
        imageView.setImageBitmap(imgbitmap);

    }
}