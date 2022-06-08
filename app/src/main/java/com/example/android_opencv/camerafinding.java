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
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class camerafinding extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    JavaCameraView javaCameraView;
    private static  String TAG = "camerafinding";



    Mat mRGBA, mRGBAT;
    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(camerafinding.this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status)
            {
                case BaseLoaderCallback.SUCCESS:
                {
                    javaCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camerafinding);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Finding contours");
        OpenCVLoader.initDebug ();
        javaCameraView = (JavaCameraView)findViewById(R.id.camerafinding);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(camerafinding.this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height , width , CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
    mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Mat cannyEdges = new Mat();
        Mat hierarchy = new Mat();
        Core.flip(mRGBA.t(),mRGBAT,1);
//        Imgproc.resize(mRGBAT,mRGBAT,mRGBA.size());
        List<MatOfPoint> contourList = new ArrayList<MatOfPoint>();
        Imgproc.Canny(mRGBAT, cannyEdges, 80, 100);
        Imgproc.findContours(cannyEdges, contourList, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0, 0));
        hierarchy.release();
        Imgproc.drawContours(mRGBAT,contourList,-1, new Scalar(Math.random()*255, Math.random()*255, Math.random()*255));

        return mRGBAT;
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