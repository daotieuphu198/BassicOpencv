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
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;

import Helper.BitmapHelper;

import static org.opencv.imgproc.Imgproc.COLOR_YUV2BGR;

public class image_histogramcu extends AppCompatActivity  {
    private ImageView imghistogramcu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_image_histogramcu );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Historam calculation");
        OpenCVLoader.initDebug ();


            imghistogramcu = (ImageView) findViewById(R.id.imghistogramcu);
            imghistogramcu.setImageBitmap(BitmapHelper.getInstance().getBitmap());
            convexBitmap();

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        HistogramBitmap(bitmap);
    }
    private void HistogramBitmap(Bitmap bitmap) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Mat rgba = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, rgba);
        Size rgbaSize = rgba.size();
        int histSize = 256;
        MatOfInt histogramSize = new MatOfInt(histSize);
        int histogramHeight = (int) rgbaSize.height;
        int binWidth = 5;

        MatOfFloat histogramRange = new MatOfFloat(0f, 256f);
        Scalar[] colorsRgb = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};


        MatOfInt[] channels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};
        Mat[] histograms = new Mat[]{new Mat(), new Mat(), new Mat()};
        Mat histMatBitmap = new Mat(rgbaSize, rgba.type());

        for (int i = 0; i < channels.length; i++) {
            Imgproc.calcHist(Collections.singletonList(rgba), channels[i], new Mat(), histograms[i], histogramSize, histogramRange);
            Core.normalize(histograms[i], histograms[i], histogramHeight, 0, Core.NORM_INF);
            for (int j = 0; j < histSize; j++) {
                Point p1 = new Point(binWidth * (j - 1), histogramHeight - Math.round(histograms[i].get(j - 1, 0)[0]));
                Point p2 = new Point(binWidth * j, histogramHeight - Math.round(histograms[i].get(j, 0)[0]));
                Imgproc.line(histMatBitmap, p1, p2, colorsRgb[i], 2, 8, 0);
            }


            Bitmap histBitmap = Bitmap.createBitmap(histMatBitmap.cols(), histMatBitmap.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(histMatBitmap, histBitmap);
            ImageView imageView = (ImageView) findViewById(R.id.imghistogramcu2);
            imageView.setImageBitmap(histBitmap);
        }


    }
}