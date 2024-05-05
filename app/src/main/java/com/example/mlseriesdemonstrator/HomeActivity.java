package com.example.mlseriesdemonstrator;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.helpers.ImageLoader;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private Interpreter faceNetModelInterpreter;
    private ImageProcessor faceNetImageProcessor;
    private static final int FACENET_INPUT_IMAGE_SIZE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        faceNetImageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(FACENET_INPUT_IMAGE_SIZE, FACENET_INPUT_IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(0f, 255f))
                .build();
        try {
            faceNetModelInterpreter = new Interpreter(FileUtil.loadMappedFile(this, "mobile_face_net.tflite"), new Interpreter.Options());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = ImageLoader.loadImageFromAssets(this, "2723.jpg");
        TensorImage tensorImage = TensorImage.fromBitmap(bitmap);
        ByteBuffer faceNetByteBuffer = faceNetImageProcessor.process(tensorImage).getBuffer();
        float[][] faceOutputArray = new float[1][192];
        //Toast.makeText(activity, ""+Arrays.deepToString(faceOutputArray), Toast.LENGTH_SHORT).show();
        faceNetModelInterpreter.run(faceNetByteBuffer, faceOutputArray);

        Toast.makeText(this, ""+Arrays.deepToString(faceOutputArray), Toast.LENGTH_SHORT).show();

    }
}