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

    //UPC-A code

    //http://en.wikipedia.org/wiki/EAN_code
    //http://www.terryburton.co.uk/barcodewriter/generator/


    static final int[] L = {0x0D,  //000 1101
            0x19,  //001 1001
            0x13,  //001 0011
            0x3D,  //011 1101
            0x23,  //010 0011
            0x31,  //011 0001
            0x2F,  //010 1111
            0x3B,  //011 1011
            0x37,  //011 0111
            0x0B   //000 1011
    };

    static final int[] R = {0x72, //111 0010
            0x66, //110 0110
            0x6C, //110 1100
            0x42, //100 0010
            0x5C, //101 1100
            0x5E, //100 1110
            0x50, //101 0000
            0x44, //100 0100
            0x48, //100 1000
            0x74  //111 0100
    };

    final static int BARCODE_WIDTH =  600;
    final static int BARCODE_HEIGHT = 200;
    final static int BARCODE_LINE_WIDTH = 5;

    // čísla čárového kódu
    int code[] = new int[12];

    public BarcodeView(Context context) {
        super(context);
        setDefaults();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDefaults();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaults();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setDefaults();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // při změně velikosti view,  w a h obsahují novou velikost
    }

    // nastaví výchozí hodnoty
    void setDefaults() {
        int copyFrom[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2};
        System.arraycopy(copyFrom, 0, code, 0, copyFrom.length);
    }

    public void setCode(int[] code) {
        this.code = code;
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

        // Vykresli bílý obdelník pro pozadí čárového kódu
        canvas.drawRect(new Rect(0, 0, BARCODE_WIDTH, BARCODE_HEIGHT), whitePaint);

        // Tloušťka čáry čárového kódu
        blackPaint.setStrokeWidth(BARCODE_LINE_WIDTH);

        int x = 20; // Počáteční pozice pro vykreslení čárového kódu
        int yTop = 50; // Horní pozice čar čárového kódu
        int yBottom = BARCODE_HEIGHT; // Dolní pozice čar čárového kódu

        // Kresli levou stranu čárového kódu (L)
        for (int i = 0; i < 6; i++) {
            int value = code[i];
            int pattern = L[value];

            for (int bit = 6; bit >= 0; bit--) {
                if ((pattern >> bit & 1) == 1) {
                    // Kresli černou čáru
                    canvas.drawLine(x, yTop, x, yBottom, blackPaint);
                }
                x += BARCODE_LINE_WIDTH;
            }
            // Kresli číslice pod čárovým kódem
            canvas.drawText(String.valueOf(value), x - (BARCODE_LINE_WIDTH * 7), BARCODE_HEIGHT + 40, textPaint);
        }

        // Kresli pravou stranu čárového kódu (R)
        for (int i = 6; i < 12; i++) {
            int value = code[i];
            int pattern = R[value];

            for (int bit = 6; bit >= 0; bit--) {
                if ((pattern >> bit & 1) == 1) {
                    // Kresli černou čáru
                    canvas.drawLine(x, yTop, x, yBottom, blackPaint);
                }
                x += BARCODE_LINE_WIDTH;
            }
            // Kresli číslice pod čárovým kódem
            canvas.drawText(String.valueOf(value), x - (BARCODE_LINE_WIDTH * 7), BARCODE_HEIGHT + 40, textPaint);
        }
    }






}

