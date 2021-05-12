package com.github.blizz2night.imagepicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.BitmapCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_PICK_IMAGE = 101;

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
    }

    public void handleActionPick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: "+requestCode);
        if (requestCode == REQUEST_PICK_IMAGE) {
            Uri imageUri = data.getData();
            float scale = 1;
            try (InputStream ins = getContentResolver().openInputStream(imageUri)){
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(ins, null, opt);
                Log.i(TAG, "onActivityResult: " + opt.outWidth + "x" + opt.outHeight);
                final int width = image.getWidth();
                scale = opt.outWidth / (float) width;

            } catch (IOException e) {
                e.printStackTrace();
            }

            try (InputStream ins = getContentResolver().openInputStream(imageUri)){
                final int width = image.getWidth();
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = (int) scale;
                Log.i(TAG, "onActivityResult: "+scale);
                Log.i(TAG, "onActivityResult: " + opt.inSampleSize);
                opt.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(ins, null, opt);
                Log.i(TAG, "onActivityResult: "+bitmap.getWidth());
                image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}