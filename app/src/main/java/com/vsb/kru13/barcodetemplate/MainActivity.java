package com.vsb.kru13.barcodetemplate;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BarcodeView barcodeView;
    private EditText inputBarcode;
    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputBarcode = findViewById(R.id.inputBarcode);
        barcodeView = findViewById(R.id.barcodeView);

        Button buttonGenerateBarcode = findViewById(R.id.buttonGenerateBarcode);
        Button buttonDrawBarcode = findViewById(R.id.buttonDrawBarcode);
        Button saveToFileButton = findViewById(R.id.buttonSaveToFile);

        buttonGenerateBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomBarcode();
            }
        });

        buttonDrawBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBarcode();
            }
        });

        saveToFileButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveBarcodeToMediaStore();
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        saveBarcodeToMediaStore();
                    }
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                }
            }
        });
    }

    private boolean isValidBarcode(String barcode) {
        return !TextUtils.isEmpty(barcode) && barcode.length() == 12 && TextUtils.isDigitsOnly(barcode);
    }

    private void generateRandomBarcode() {
        Random random = new Random();
        StringBuilder barcode = new StringBuilder();

        for (int i = 0; i < 11; i++) {
            barcode.append(random.nextInt(10));
        }
        inputBarcode.setText(barcode.toString());
    }

    private void drawBarcode() {
        String barcodeText = inputBarcode.getText().toString();

        if (barcodeText.length() == 11) {
            int[] barcodeArray = new int[11];
            for (int i = 0; i < 11; i++) {
                barcodeArray[i] = Character.getNumericValue(barcodeText.charAt(i));
            }

            barcodeView.setCodeWithCheckDigit(barcodeArray);
            barcodeView.invalidate();
        } else {
            inputBarcode.setError("Zadejte přesně 11 číslic pro automatický výpočet kontrolní číslice.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void saveBarcodeToMediaStore() {
        barcodeView.setDrawingCacheEnabled(true);
        Bitmap barcodeBitmap = Bitmap.createBitmap(barcodeView.getDrawingCache());
        barcodeView.setDrawingCacheEnabled(false);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "barcode.png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Barcodes");

        try (OutputStream out = getContentResolver().openOutputStream(
                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values))) {
            if (out != null) {
                barcodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                Toast.makeText(this, "Čárový kód uložen do složky Obrázky/Barcodes", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Chyba při ukládání: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveBarcodeToMediaStore();
                }
            } else {
                Toast.makeText(this, "Oprávnění pro uložení nebylo uděleno.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
