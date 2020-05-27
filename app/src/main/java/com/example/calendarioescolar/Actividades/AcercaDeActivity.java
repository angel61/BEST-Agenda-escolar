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

public class AcercaDeActivity extends Activity {
    private TextView correo;
    private Context context;

@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);
        inicio();
        inicioListeners();
    }

    private void inicio() {
        context=this;
    correo=findViewById(R.id.TextViewCorreo);
    }

    private void inicioListeners() {
    correo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copiado", correo.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context,"Copiado en el portapapeles", Toast.LENGTH_SHORT).show();
        }
    });
    }
}