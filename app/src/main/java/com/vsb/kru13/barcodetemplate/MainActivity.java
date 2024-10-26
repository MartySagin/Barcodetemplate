package com.vsb.kru13.barcodetemplate;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BarcodeView barcodeView;
    private EditText inputBarcode;
    private Button buttonDrawBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeView = findViewById(R.id.barcodeView);
        inputBarcode = findViewById(R.id.inputBarcode);
        buttonDrawBarcode = findViewById(R.id.buttonDrawBarcode);

        buttonDrawBarcode.setOnClickListener(view -> {
            String barcodeInput = inputBarcode.getText().toString();

            // Kontrola, zda je vstup 12 číslic
            if (isValidBarcode(barcodeInput)) {
                int[] barcodeNumbers = new int[12];

                // Převod vstupu na pole int
                for (int i = 0; i < 12; i++) {
                    barcodeNumbers[i] = Character.getNumericValue(barcodeInput.charAt(i));
                }

                // Nastavení kódu v BarcodeView a překreslení
                barcodeView.setCode(barcodeNumbers);
                barcodeView.invalidate(); // Překreslí BarcodeView
            } else {
                Toast.makeText(MainActivity.this, "Prosím zadejte 12 číslic.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validace vstupu (musí být 12 číslic)
    private boolean isValidBarcode(String barcode) {
        return !TextUtils.isEmpty(barcode) && barcode.length() == 12 && TextUtils.isDigitsOnly(barcode);
    }
}
