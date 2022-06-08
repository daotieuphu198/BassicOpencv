package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
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

public class camera_histogramcu extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static String TAG = "image_Blur";

    JavaCameraView javaCameraView;
    Mat mRGBA, mRGBAT;


    final BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(camera_histogramcu.this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_histogramcu);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Historam calculation");
        OpenCVLoader.initDebug ();
        javaCameraView = (JavaCameraView)findViewById(R.id.my_cameraview);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(camera_histogramcu.this);
    }
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height, width, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(),mRGBAT,1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
        Size rgbaSize = mRGBAT.size();
        int histSize = 256;
        MatOfInt histogramSize = new MatOfInt(histSize);
        int histogramHeight = (int) rgbaSize.height;
        int binWidth = 5;

        MatOfFloat histogramRange = new MatOfFloat (0f, 256f);
        Scalar[] colorsRgb = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};


        MatOfInt[] channels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};
        Mat[] histograms = new Mat[]{new Mat(), new Mat(), new Mat()};
        Mat histMatBitmap = new Mat(rgbaSize, mRGBAT.type());

        for (int i = 0; i < channels.length; i++) {
            Imgproc.calcHist( Collections.singletonList(mRGBAT), channels[i], new Mat(), histograms[i], histogramSize, histogramRange);
            Core.normalize(histograms[i], histograms[i], histogramHeight, 0, Core.NORM_INF);
            for (int j = 0; j < histSize; j++) {
                Point p1 = new Point(binWidth * (j - 1), histogramHeight - Math.round(histograms[i].get(j - 1, 0)[0]));
                Point p2 = new Point(binWidth * j, histogramHeight - Math.round(histograms[i].get(j, 0)[0]));
                Imgproc.line(histMatBitmap, p1, p2, colorsRgb[i], 2, 8, 0);
            }

        }



        return histMatBitmap;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.d(TAG , "da chay dc");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);

        }else
        {
            Log.d(TAG,"ko chay dc");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this, baseLoaderCallback);
        }
    }
}