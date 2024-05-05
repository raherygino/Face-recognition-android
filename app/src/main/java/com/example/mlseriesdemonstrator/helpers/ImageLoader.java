package com.example.mlseriesdemonstrator.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {

    public static Bitmap loadImageFromAssets(Context context, String fileName) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            // Open the image file from assets folder
            inputStream = context.getAssets().open(fileName);

            // Decode the InputStream into a Bitmap
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}