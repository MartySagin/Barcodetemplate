package com.vsb.kru13.barcodetemplate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BarcodeView barcodeView;
    private EditText inputBarcode;
    private Button buttonDrawBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputBarcode = findViewById(R.id.inputBarcode);
        barcodeView = findViewById(R.id.barcodeView);

        Button buttonGenerateBarcode = findViewById(R.id.buttonGenerateBarcode);
        Button buttonDrawBarcode = findViewById(R.id.buttonDrawBarcode);

        // Náhodné generování čárového kódu
        buttonGenerateBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomBarcode();
            }
        });

        // Vykreslení čárového kódu zadaného uživatelem
        buttonDrawBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBarcode();
            }
        });
    }

    // Validace vstupu (musí být 12 číslic)
    private boolean isValidBarcode(String barcode) {
        return !TextUtils.isEmpty(barcode) && barcode.length() == 12 && TextUtils.isDigitsOnly(barcode);
    }

    // Generování náhodného čárového kódu
    private void generateRandomBarcode() {
        Random random = new Random();
        StringBuilder barcode = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            barcode.append(random.nextInt(10)); // Náhodné číslo od 0 do 9
        }
        inputBarcode.setText(barcode.toString());
    }

    // Vykreslení čárového kódu na základě vstupu
    private void drawBarcode() {
        String barcodeText = inputBarcode.getText().toString();

        if (barcodeText.length() == 12) {
            int[] barcodeArray = new int[12];
            for (int i = 0; i < 12; i++) {
                barcodeArray[i] = Character.getNumericValue(barcodeText.charAt(i));
            }
            barcodeView.setCode(barcodeArray);
            barcodeView.invalidate(); // Překreslí čárový kód s novým kódem
        } else {
            inputBarcode.setError("Čárový kód musí mít 12 číslic.");
        }
    }
}
