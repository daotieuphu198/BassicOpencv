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
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.BORDER_CONSTANT;
import static org.opencv.imgproc.Imgproc.INTER_LINEAR;

public class camera_remaping extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static String TAG ="camera_remaping";
    Mat mRGBA, mRGBAT, mapX,mapY;
    int ind = 0;
    JavaCameraView javaCameraView;
    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(camera_remaping.this) {
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
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("REMAPING");
        OpenCVLoader.initDebug ();
        setContentView(R.layout.activity_camera_remaping);
        javaCameraView = (JavaCameraView)findViewById(R.id.mycameraView1);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(camera_remaping.this);
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

        mapX = new Mat(mRGBA.size(), CvType.CV_32F);
        mapY = new Mat(mRGBA.size(), CvType.CV_32F);
        float buffX[] = new float[(int) (mapX.total() * mapX.channels())];
        mapX.get(0, 0, buffX);

        float buffY[] = new float[(int) (mapY.total() * mapY.channels())];
        mapY.get(0, 0, buffY);
        ind =(ind+1)%4;
        for (int i = 0; i < mapX.rows(); i++) {
            for (int j = 0; j < mapX.cols(); j++) {
                switch (ind) {
                    case 0:
                        if( j > mapX.cols()*0.25 && j < mapX.cols()*0.75 && i > mapX.rows()*0.25 && i < mapX.rows()*0.75 ) {
                            buffX[i*mapX.cols() + j] = 2*( j - mapX.cols()*0.25f ) + 0.5f;
                            buffY[i*mapY.cols() + j] = 2*( i - mapX.rows()*0.25f ) + 0.5f;
                        } else {
                            buffX[i*mapX.cols() + j] = 0;
                            buffY[i*mapY.cols() + j] = 0;
                        }
                        break;
                    case 1:
                        buffX[i*mapX.cols() + j] = j;
                        buffY[i*mapY.cols() + j] = mapY.rows() - i;
                        break;
                    case 2:
                        buffX[i*mapX.cols() + j] = mapY.cols() - j;
                        buffY[i*mapY.cols() + j] = i;
                        break;
                    case 3:
                        buffX[i*mapX.cols() + j] = mapY.cols() - j;
                        buffY[i*mapY.cols() + j] = mapY.rows() - i;
                        break;
                    default:
                        break;
                }
            }
            mapX.put(0, 0, buffX);
            mapY.put(0, 0, buffY);
            ind = 3;
        }

        Imgproc.remap ( mRGBAT,mRGBAT,mapX,mapY,INTER_LINEAR,BORDER_CONSTANT,new Scalar( 0,0,0 ) );
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