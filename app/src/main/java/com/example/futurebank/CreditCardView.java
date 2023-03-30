package com.example.futurebank;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;


public class CreditCardView extends View {
    private Paint mBackgroundPaint;
    private Paint mLogoPaint;
    private Paint mNumberPaint;
    private Paint mExpirationPaint;
    private Paint mNamePaint;

    private String mCardNumber;
    private String mExpiration;
    private String mName;

    public CreditCardView(Context context) {
        super(context);
        init();
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mBackgroundPaint.setStyle(Paint.Style.FILL);



        mNumberPaint = new Paint();
        mNumberPaint.setColor(Color.BLACK);
        mNumberPaint.setStyle(Paint.Style.FILL);
        mNumberPaint.setTextSize(40);

        mExpirationPaint = new Paint();
        mExpirationPaint.setColor(Color.BLACK);
        mExpirationPaint.setStyle(Paint.Style.FILL);
        mExpirationPaint.setTextSize(31);

        mNamePaint = new Paint();
        mNamePaint.setColor(Color.BLACK);
        mNamePaint.setStyle(Paint.Style.FILL);
        mNamePaint.setTextSize(31);

        mCardNumber = "****   ****   ****   1234";
        mExpiration = "12/23";
        mName = "Ahmed Anwer";

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the card background
        RectF cardRect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(cardRect, 16, 16, mBackgroundPaint);

        // Draw the Mastercard logo
        float logoSize = getHeight() / 4;
        float logoPadding = 50;
        float logoX = getWidth() - logoSize - logoPadding;
        float logoY = getHeight() - logoSize - logoPadding;

        Drawable logoDrawable = VectorDrawableCompat.create(getResources(), R.drawable.mastercard, null);
        logoDrawable.setBounds((int)logoX, (int)logoY+25, (int)(logoX + logoSize), (int)(logoY + logoSize));
        logoDrawable.draw(canvas);


        // Draw the card number
        float numberPaddingX =getWidth()/3;
        float numberPaddingY = getHeight() / (float)2.5;

        canvas.drawText(mCardNumber, numberPaddingX, numberPaddingY, mNumberPaint);


        // Draw the expiration date
        float expirationPaddingX = 150;
        float expirationPaddingY = logoY - logoPadding+50;
        canvas.drawText("Valid Till\n"+mExpiration, getWidth() - mExpirationPaint.measureText(mExpiration) - expirationPaddingX, expirationPaddingY, mExpirationPaint);

        // Draw the card holder name
        float namePadding = 0.1f * getWidth();
        float nameX = namePadding;
        float nameY = 0.7f * getHeight()-10;
        canvas.drawText(mName, nameX, nameY, mNamePaint);
    }

}
