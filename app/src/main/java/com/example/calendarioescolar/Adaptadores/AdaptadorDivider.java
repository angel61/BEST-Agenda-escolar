package com.example.calendarioescolar.Adaptadores;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Clase para adaptar los divisores para los recyclerView de la pagina de inicio
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 * @see RecyclerView.ItemDecoration
 */
public class AdaptadorDivider extends RecyclerView.ItemDecoration {
    private Drawable divisor;

    /**
     * Constructor del adaptador que recive como parametro el Drawable para usargo como divisor en el recyclerView
     *
     * @param divisor Drawable utilizado en los recyclerView
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public AdaptadorDivider(Drawable divisor) {
        divisor = divisor;
    }

    /**
     * Cuando se dibuja el divisor se establecen los parametros necesarios
     *
     * @param canvas
     * @param parent
     * @param state
     * @author Angel Lopez Palacios
     * @version 1.0
     */
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