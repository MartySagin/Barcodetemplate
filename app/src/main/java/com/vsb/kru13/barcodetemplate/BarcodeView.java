package com.vsb.kru13.barcodetemplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BarcodeView extends View {

    static final int[] L = {0x0D, 0x19, 0x13, 0x3D, 0x23, 0x31, 0x2F, 0x3B, 0x37, 0x0B};
    static final int[] R = {0x72, 0x66, 0x6C, 0x42, 0x5C, 0x5E, 0x50, 0x44, 0x48, 0x74};

    static int BARCODE_WIDTH = 600;
    static int BARCODE_HEIGHT = 200;
    static final int BARCODE_LINE_WIDTH = 5;

    int[] code = new int[12];

    public BarcodeView(Context context) {
        super(context);
        setDefaults();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDefaults();
    }

    void setDefaults() {
        int[] copyFrom = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2};
        System.arraycopy(copyFrom, 0, code, 0, copyFrom.length);
    }

    public void setCodeWithCheckDigit(int[] codeWithoutCheckDigit) {
        System.arraycopy(codeWithoutCheckDigit, 0, code, 0, 11);
        code[11] = calculateCheckDigit(codeWithoutCheckDigit);
    }

    private int calculateCheckDigit(int[] codeWithoutCheckDigit) {
        int oddSum = 0, evenSum = 0;
        for (int i = 0; i < codeWithoutCheckDigit.length; i++) {
            if (i % 2 == 0) {
                oddSum += codeWithoutCheckDigit[i];
            } else {
                evenSum += codeWithoutCheckDigit[i];
            }
        }
        int total = (oddSum * 3) + evenSum;
        return (10 - (total % 10)) % 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);

        Paint blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);

        // Dynamická velikost podle aktuální velikosti View
        BARCODE_WIDTH = getWidth();
        BARCODE_HEIGHT = getHeight() / 2;

        // Vypočítá pozici pro vykreslení čárového kódu uprostřed
        int xStart = (getWidth() - BARCODE_WIDTH) / 2;
        int yStart = (getHeight() - BARCODE_HEIGHT) / 2;

        // Vykreslení pozadí čárového kódu
        canvas.drawRect(new Rect(xStart, yStart, xStart + BARCODE_WIDTH, yStart + BARCODE_HEIGHT), whitePaint);

        blackPaint.setStrokeWidth(BARCODE_LINE_WIDTH);

        // Dynamické nastavení x, aby bylo centrované podle šířky kódu
        int codeWidth = (BARCODE_LINE_WIDTH * 7 * 12); // celková šířka čárového kódu
        int x = xStart + (BARCODE_WIDTH - codeWidth) / 2; // dynamicky posune x na střed

        int yTop = yStart + 50;
        int yBottom = yStart + BARCODE_HEIGHT;

        // Kreslení levé části čárového kódu (L)
        for (int i = 0; i < 6; i++) {
            int value = code[i];
            int pattern = L[value];

            for (int bit = 6; bit >= 0; bit--) {
                if ((pattern >> bit & 1) == 1) {
                    canvas.drawLine(x, yTop, x, yBottom, blackPaint);
                }
                x += BARCODE_LINE_WIDTH;
            }
            canvas.drawText(String.valueOf(value), x - (BARCODE_LINE_WIDTH * 7), yBottom + 40, textPaint);
        }

        // Kreslení pravé části čárového kódu (R)
        for (int i = 6; i < 12; i++) {
            int value = code[i];
            int pattern = R[value];

            for (int bit = 6; bit >= 0; bit--) {
                if ((pattern >> bit & 1) == 1) {
                    canvas.drawLine(x, yTop, x, yBottom, blackPaint);
                }
                x += BARCODE_LINE_WIDTH;
            }
            canvas.drawText(String.valueOf(value), x - (BARCODE_LINE_WIDTH * 7), yBottom + 40, textPaint);
        }
    }
}
