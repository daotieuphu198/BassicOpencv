package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.mtp.MtpConstants;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.List;

import Helper.BitmapHelper;
import gun0912.tedbottompicker.TedBottomPicker;

public class Image_preprocessing extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private Button btn_hinhP ,btn_blur,btn_gau,btn_median,btn_bilateral,btn_histogramcu,btn_remapping,btn_histogramequa, btn_camera;
    private ImageView img1;
    private  int i =0;


     private Mat   mRGBA,mRGBAT;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preprocessing);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Image preprocessing");
        OpenCVLoader.initDebug();

        btn_blur = (Button) findViewById(R.id.btn_blur);
        btn_gau = (Button) findViewById(R.id.btn_gau);
        btn_median = (Button) findViewById(R.id.btn_median);
        btn_bilateral = (Button) findViewById(R.id.btn_bilateral);
        btn_histogramcu = (Button) findViewById(R.id.btn_histogramcu);
        btn_remapping = (Button) findViewById(R.id.btn_remapping);
        btn_histogramequa = (Button) findViewById(R.id.btn_histogramequa);
        btn_hinhP = (Button) findViewById(R.id.btn_hinhP);
        btn_camera = (Button) findViewById(R.id.btn_camera);


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 1;
                img1 = null;
                JavaCameraView javaCameraView = (JavaCameraView) findViewById(R.id.mycamera);
                javaCameraView.setVisibility(SurfaceView.VISIBLE);

                javaCameraView.setCvCameraViewListener(javaCameraView.enableView());

            }

        });


        btn_blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img1 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(Image_preprocessing.this, image_Blur.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img1!=null){
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, image_Blur1.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_preprocessing.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }

            }

        });


        btn_gau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img1 != null && i==0) {
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, image_Gau.class);
                    startActivity(intent);
                    finish();

                } else  if (i !=0 && img1==null){
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, camera_gau.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_preprocessing.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_median.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BitmapHelper.getInstance().getBitmap() != null && i==0) {
                    Intent intent = new Intent();
                    intent.setClass(Image_preprocessing.this, image_Median.class);
                    startActivity(intent);
                    finish();

                } else  if (i !=0 && BitmapHelper.getInstance().getBitmap() == null){
                    Intent intent = new Intent();
                    intent.setClass(Image_preprocessing.this, camera_median.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_preprocessing.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_bilateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img1 != null && i==0) {
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, image_bilaterral.class);
                    startActivity(intent);
                    finish();

                } else  if (img1 == null && i!=0){
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, camera_bilaterral.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_preprocessing.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_histogramcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img1 != null && i==0) {
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, image_histogramcu.class);
                    startActivity(intent);
                    finish();

                } else  if (img1 == null && i!=0){
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, camera_histogramcu.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_preprocessing.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_remapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img1 != null && i==0) {
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, image_remaping.class);
                    startActivity(intent);
                    finish();

                } else  if (img1 == null && i!=0){
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, camera_remaping.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_preprocessing.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btn_histogramequa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img1 != null && i==0) {
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, image_histogramequa.class);
                    startActivity(intent);
                    finish();

                } else  if (img1 == null && i!=0){
                    Intent intent = new Intent();

                    intent.setClass(Image_preprocessing.this, camera_histogramequa.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_preprocessing.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }


            }
        });


        btn_hinhP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                requestPermission();


            }
        });

        //tao su kien onclick khi nhan camera


    }


    private void requestPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(Image_preprocessing.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                openImagePicket();


            }
    @Override
    public void onPermissionDenied(List<String> deniedPermissions) {
        Toast.makeText(Image_preprocessing.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
    }


};
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions( Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
                }
private ImageView openImagePicket(){
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
@Override
public void onImageSelected(Uri uri) {
        Bitmap bitmap = null;

            try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            img1 = (ImageView)findViewById(R.id.img1);
            img1.setImageBitmap(bitmap);
                BitmapHelper.getInstance ().setBitmap ( bitmap );

                } catch (IOException e) {
            e.printStackTrace();
            }

         }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(Image_preprocessing.this)
        .setOnImageSelectedListener(listener)
        .create();



        tedBottomPicker.show(getSupportFragmentManager());

        return null;
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
        Core.flip(mRGBA.t(), mRGBAT, 1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
        return mRGBAT;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}