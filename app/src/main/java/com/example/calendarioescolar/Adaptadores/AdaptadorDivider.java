package com.example.calendarioescolar.Adaptadores;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorDivider extends RecyclerView.ItemDecoration {
    private Drawable divisor;

    public AdaptadorDivider(Drawable divisor) {
        divisor = divisor;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int izq = parent.getPaddingLeft();
        int der = parent.getWidth() - parent.getPaddingRight();

        int cont = parent.getChildCount();
        for (int i = 0; i <= cont - 2; i++) {
            View hijo = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) hijo.getLayoutParams();

            int arriba = hijo.getBottom() + params.bottomMargin;
            int abajo = arriba + divisor.getIntrinsicHeight();

            divisor.setBounds(izq, arriba, der, abajo);
            divisor.draw(canvas);
        }
    }
}