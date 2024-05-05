package com.example.mlseriesdemonstrator;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.helpers.ImageLoader;

import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FaceRecognitionActivity extends AppCompatActivity {

    private static final String TAG = "FaceRecognitionActivity";
    private Interpreter interpreter;
    private int inputSize = 250; // Example input size, adjust according to your model
    private int outputVectorSize = 250; // Example output vector size, adjust according to your model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Load the TensorFlow Lite model
        try {
            interpreter = new Interpreter(loadModelFile("mobile_face_net.tflite"));
        } catch (IOException e) {
            Log.e(TAG, "Error loading TensorFlow Lite model.", e);
            Toast.makeText(this, "Error loading TensorFlow Lite model.", Toast.LENGTH_LONG).show();
        }

        // Example: Recognize a face from a sample image
        recognizeFaceFromSampleImage();

        /*Bitmap bitmap = ImageLoader.loadImageFromAssets(FaceRecognitionActivity.this, "2723.jpg");
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        ImageView img = findViewById(R.id.preview);
        img.setImageBitmap(resizedBitmap);*/
    }

    // Load the TensorFlow Lite model file
    private MappedByteBuffer loadModelFile(String modelFilename) throws IOException {
        AssetManager assetManager = getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Preprocess the input image
    private float[] preprocessImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        float[] inputFeatures = new float[inputSize * inputSize * 3]; // Assuming 3 channels for RGB image
        int pixel;
        int idx = 0;
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                pixel = resizedBitmap.getPixel(j, i); // Note: (x, y) coordinates are flipped
                inputFeatures[idx++] = (Color.red(pixel) - 127.5f) / 127.5f;
                inputFeatures[idx++] = (Color.green(pixel) - 127.5f) / 127.5f;
                inputFeatures[idx++] = (Color.blue(pixel) - 127.5f) / 127.5f;
            }
        }
        return inputFeatures;
    }

    // Example: Recognize a face from a sample image
    private void recognizeFaceFromSampleImage() {
        try {
            // Load a sample image from assets
            Bitmap sampleBitmap = BitmapFactory.decodeStream(getAssets().open("2723.jpg"));

            // Preprocess the sample image
            float[] inputFeatures = preprocessImage(sampleBitmap);

            // Run inference
            float[] outputFeatures = new float[outputVectorSize];
            interpreter.run(inputFeatures, outputFeatures);

            Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();

            // Example: Use the output features for face recognition
            // Compare with known face embeddings to recognize the face

        } catch (IOException e) {
            Log.e(TAG, "Error loading sample image.", e);
            Toast.makeText(this, "Error loading sample image.", Toast.LENGTH_LONG).show();
        }
    }
}
