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
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.List;

import Helper.BitmapHelper;
import gun0912.tedbottompicker.TedBottomPicker;

public class Image_segmentation extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private  Button btn_hinhS,btn_threshold,btn_finding,btn_convex,btn_bounding,btn_hough ,btn_camera;
    private  ImageView img4;
    JavaCameraView javaCameraView;
    Mat mRGBA, mRGBAT;
    private  int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_image_segmentation );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Image Segmentation");
        btn_camera = (Button)findViewById(R.id.btn_camera);
        btn_hinhS = (Button)findViewById(R.id.btn_hinhS);
        img4 = (ImageView)findViewById(R.id.img4);
        btn_threshold = (Button)findViewById(R.id.btn_threshold);
        btn_finding = (Button)findViewById(R.id.btn_finding);
        btn_convex = (Button)findViewById(R.id.btn_convex);
        btn_bounding = (Button)findViewById(R.id.btn_bounding);
        btn_hough = (Button)findViewById(R.id.btn_hough);


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img4 = null;
                i = i+1;
                javaCameraView = (JavaCameraView)findViewById(R.id.cameraseg);
                javaCameraView.setVisibility(SurfaceView.VISIBLE);
                javaCameraView.setCvCameraViewListener(javaCameraView.enableView());
            }
        });

        btn_threshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img4 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(Image_segmentation.this, camerathreshold.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img4!=null){
                    Intent intent = new Intent();

                    intent.setClass(Image_segmentation.this, img_threshold.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_segmentation.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_finding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img4 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(Image_segmentation.this, camerafinding.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img4!=null){
                    Intent intent = new Intent();

                    intent.setClass(Image_segmentation.this, img_finding.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_segmentation.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_convex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img4 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(Image_segmentation.this, cameraconvex.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img4!=null){
                    Intent intent = new Intent();

                    intent.setClass(Image_segmentation.this, img_convex.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_segmentation.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_bounding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img4 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(Image_segmentation.this, camerabouding.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img4!=null){
                    Intent intent = new Intent();

                    intent.setClass(Image_segmentation.this, img_bounding.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_segmentation.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_hough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img4 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(Image_segmentation.this, camerahough.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img4!=null){
                    Intent intent = new Intent();

                    intent.setClass(Image_segmentation.this, img_hough.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Image_segmentation.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });










        btn_hinhS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                i = 0;
            }
        });
    }
    private void requestPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(Image_segmentation.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                openImagePicket();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(Image_segmentation.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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
                    img4.setImageBitmap(bitmap);
                    BitmapHelper.getInstance ().setBitmap ( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(Image_segmentation.this)
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
        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(),mRGBAT,1);
        Imgproc.resize(mRGBAT,mRGBAT,mRGBA.size());
        return mRGBAT;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}