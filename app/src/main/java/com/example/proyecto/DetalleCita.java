package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class DetalleCita extends AppCompatActivity implements View.OnClickListener{
    private Cita cita;
    TextView jlblIDCita, jlblDoctor, jlblPaciente, jlblFechaSeleccionada, jlblHora, jlblEstado;
    Button jbtnAnularCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cita);

        jlblIDCita = findViewById(R.id.lblIDCita);
        jlblDoctor = findViewById(R.id.lblDoctor);
        jlblPaciente = findViewById(R.id.lblPaciente);
        jlblFechaSeleccionada = findViewById(R.id.lblFechaSeleccionada);
        jlblHora = findViewById(R.id.lblHora);
        jlblEstado = findViewById(R.id.lblEstado);
        jbtnAnularCita = findViewById(R.id.btnAnularCita);

        cita = (Cita) getIntent().getSerializableExtra("datosCita");

        jlblIDCita.setText(""+cita.getId_cita());
        jlblDoctor.setText(cita.getNombre_de()+", "+cita.getApellidos_de());
        jlblPaciente.setText(cita.getNombre_pa()+", "+cita.getApellidos_pa());
        jlblFechaSeleccionada.setText(cita.DevolverFormatoFecha());
        jlblHora.setText(cita.getHora_inicio()+" a "+cita.getHora_final());
        jlblEstado.setText(cita.getEstado());

        jbtnAnularCita.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AsyncHttpClient ahcAnular = new AsyncHttpClient();
        String sUrl = "http://camilodc.site/AnularCita.php";
        //llenar parametros
        RequestParams params = new RequestParams();
        params.add("id_cita", ""+cita.getId_cita());


        ahcAnular.post(sUrl, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int iRetVal = (rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse));
                    if(iRetVal == 1){
                        Toast.makeText(getApplicationContext(), "Cita anulada!", Toast.LENGTH_SHORT).show();
                        Intent iMenuPrincipal = new Intent(getApplicationContext(), MenuPrincipal.class);
                        startActivity(iMenuPrincipal);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "Error al anular.", Toast.LENGTH_SHORT).show();

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }
}