package com.example.android_opencv;

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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class camera_morphology extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    JavaCameraView javaCameraView;
    Mat mRGBA, mRGBAT;
    private static String TAG = "morphologycal";
    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(camera_morphology.this) {
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
        setContentView(R.layout.activity_camera_morphology);
        javaCameraView= (JavaCameraView)findViewById(R.id.cameraV2);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(camera_morphology.this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height,width, CvType.CV_8UC4);
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
        Imgproc.cvtColor ( mRGBAT,mRGBAT,Imgproc.COLOR_RGB2GRAY );
        Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
        Imgproc.morphologyEx(mRGBAT, mRGBAT, Imgproc.MORPH_CLOSE, se);
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