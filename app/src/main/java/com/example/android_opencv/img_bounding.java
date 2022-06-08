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
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Helper.BitmapHelper;

public class img_bounding extends AppCompatActivity {
    ImageView imgbounding;
    int k = 256;
    SeekBar seebarbouding;
    private Mat srcGray = new Mat();

    private static final int MAX_THRESHOLD = 255;
    private int threshold = 100;
    private Random rng = new Random (12345);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_bounding );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bouding boxes and circles");
        OpenCVLoader.initDebug ();
        seebarbouding = (SeekBar)findViewById(R.id.seebarbouding);
        imgbounding = (ImageView)findViewById ( R.id.imgbounding );
        imgbounding.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        for(int a = 0 ; a<=100; a++){
            if(k!=256){
                seebarbouding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        bounding(bitmap);
    }
    private void bounding(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
        Mat cannyOutput = new Mat();
        Imgproc.Canny(srcGray, cannyOutput, threshold, threshold * 2);
        List<MatOfPoint> contours = new ArrayList<> ();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        Point[] centers = new Point[contours.size()];
        float[][] radius = new float[contours.size()][1];
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
            centers[i] = new Point ();
            Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
        }
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
        for (MatOfPoint2f poly : contoursPoly) {
            contoursPolyList.add(new MatOfPoint(poly.toArray()));
        }
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(rng.nextInt(k), rng.nextInt(k), rng.nextInt(k));
            Imgproc.drawContours(drawing, contoursPolyList, i, color);
            Imgproc.rectangle(drawing, boundRect[i].tl(), boundRect[i].br(), color, 2);
            Imgproc.circle(drawing, centers[i], (int) radius[i][0], color, 2);
        }
        Bitmap imgBitmap = Bitmap.createBitmap ( drawing.cols (),drawing.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( drawing,imgBitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imgbounding2 );
        imageView.setImageBitmap ( imgBitmap );

    }
}