package com.example.calendarioescolar.Actividades;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarioescolar.R;

/**
 * Clase para mostrar una breve descripción sobre la aplicación e informar de quien la desarrollo. Se informa al usuario
 * sobre los objetivos de la aplicación. Tambien pone a disposiscion del usuario mi correo electronico.
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 * @see android.app.Activity
 */
public class AcercaDeActivity extends Activity {
    private TextView correo;
    private Context context;

    /**
     * Inicializa los componentes de la actividad. El argumento Bundle
     * contiene el estado ya guardado de la actividad.
     * Si la actividad nunca ha existido, el valor del objeto Bundle es nulo.
     * <p>
     * muestra la configuración básica de la actividad, como declarar
     * la interfaz de usuario (definida en un archivo XML de diseño),
     * definir las variables de miembro y configurar parte de la IU
     * </p>
     *
     * @param savedInstanceState objeto Bundle que contiene el estado de la actividad.
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);
        inicio();
        inicioListeners();
    }

    /**
     * Este metodo es utilizado es utilizado
     * para inicializarlos elementos que se van a utilizar en la actividad de manera interactiva
     * o son nevesario para que funcione.
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    private void inicio() {
        context = this;
        correo = findViewById(R.id.TextViewCorreo);
    }

    /**
     * Este metodo es utilizado para poner los listener a los elementos ya inicializados que lo necesiten
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    private void inicioListeners() {
        correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copiado", correo.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copiado en el portapapeles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}