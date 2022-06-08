package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

public class border_delection extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    Button btn_hinhB, btn_laplacian, btn_sobel,btn_canny, btn_camera;
    ImageView img3;
    JavaCameraView javaCameraView;
    Mat mRGBA, mRGBAT;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_border_delection );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Border Delection");
        OpenCVLoader.initDebug();

        btn_camera = (Button)findViewById(R.id.btn_camera);
        btn_laplacian = (Button)findViewById(R.id.btn_laplacian);
        btn_sobel = (Button)findViewById(R.id.btn_sobel);
        btn_canny = (Button)findViewById(R.id.btn_canny);
        btn_hinhB = (Button)findViewById(R.id.btn_hinhB);
        img3 = (ImageView)findViewById(R.id.img3);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                javaCameraView = (JavaCameraView)findViewById(R.id.cameraVI);
                javaCameraView.setVisibility(SurfaceView.VISIBLE);
                img3 = null;
                i = 1;
                javaCameraView.setCvCameraViewListener(javaCameraView.enableView());
            }
        });





        btn_hinhB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                requestPermission();
            }
        });


        btn_laplacian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img3 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(border_delection.this, camera_laplacian.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img3 != null){
                    Intent intent = new Intent();
                    intent.setClass(border_delection.this, img_laplacian.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(border_delection.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });




        btn_sobel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img3 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(border_delection.this, camera_sobel.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img3 != null){
                    Intent intent = new Intent();
                    intent.setClass(border_delection.this, img_sobel.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(border_delection.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_canny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img3 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(border_delection.this, camera_canny.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img3 != null){
                    Intent intent = new Intent();
                    intent.setClass(border_delection.this, img_canny.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(border_delection.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void requestPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(border_delection.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                openImagePicket();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(border_delection.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions( Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    private void openImagePicket(){
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    img3.setImageBitmap(bitmap);
                    BitmapHelper.getInstance ().setBitmap ( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(border_delection.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
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
        mRGBA  = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(),mRGBAT,1);
        Imgproc.resize(mRGBAT,mRGBAT,mRGBA.size());
        return mRGBAT;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}