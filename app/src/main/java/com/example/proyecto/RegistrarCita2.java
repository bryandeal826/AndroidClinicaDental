package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class RegistrarCita2 extends AppCompatActivity implements View.OnClickListener{
    private HorarioDentista dentista;
    private String turno, fecha;
    private boolean existencia;

    TextView jlblDoctor, jlblFechaSeleccionada, jlblHora, jlblEstado;
    Button jbtnRegistrarCita;

    private SharedPreferences preferences3;
    private SharedPreferences.Editor editor3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cita2);

        jlblDoctor = findViewById(R.id.lblDoctor);
        jlblFechaSeleccionada = findViewById(R.id.lblFechaSeleccionada);
        jlblHora = findViewById(R.id.lblHora);
        jlblEstado = findViewById(R.id.lblEstado);
        jbtnRegistrarCita = findViewById(R.id.btnRegistrarCita);

        preferences3 = getSharedPreferences("Usuario", MODE_PRIVATE);
        editor3 = preferences3.edit();

        dentista = (HorarioDentista) getIntent().getSerializableExtra("datosDentista");
        turno = this.getIntent().getStringExtra("turno");
        fecha = this.getIntent().getStringExtra("fecha");

        MostrarEstado(dentista.getId_dentista(), dentista.getId_dia(), dentista.getId_hora(), fecha);
        jlblDoctor.setText(dentista.getNombres()+", "+dentista.getApellidos());
        jlblFechaSeleccionada.setText(fecha);
        jlblHora.setText(dentista.getHora_inicio()+" a "+dentista.getHora_fin());

        jbtnRegistrarCita.setOnClickListener(this);
    }

    private void MostrarEstado(int id_dentista, int id_dia, int id_hora, String fecha) {
        AsyncHttpClient consultarHora = new AsyncHttpClient();

        String sURL ="http://camilodc.site/ConsultarDispinibilidadHora.php";

        RequestParams params = new RequestParams();
        params.add("id_dentista", ""+id_dentista);
        params.add("id_dia", ""+id_dia);
        params.add("id_hora", ""+id_hora);
        params.add("fecha", fecha);

        consultarHora.post(sURL, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        if(jsonArray.length() > 0){
                            String estado = jsonArray.getJSONObject(0).getString("estado");
                            if(estado.equals("0")){
                                jlblEstado.setText(Html.fromHtml("<font color='#33691E'>Disponible</font>"));
                            }else{
                                jlblEstado.setText(Html.fromHtml("<font color='#EE0000'>Reservado</font>"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    @Override
    public void onClick(View view) {
        ConsultarExistenciaCita();
    }

    private void ConsultarExistenciaCita() {
        AsyncHttpClient consultarExistencia = new AsyncHttpClient();

        String sURL ="http://camilodc.site/ConsultarExistenciaCita.php";

        RequestParams params = new RequestParams();
        params.add("id_paciente", "" + preferences3.getInt("id_usuario", 0));

        consultarExistencia.post(sURL, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        if(jsonArray.length() > 0){
                            String estado = jsonArray.getJSONObject(0).getString("estado");
                            if(estado.equals("0")){
                                AsyncHttpClient ahcRegistrar = new AsyncHttpClient();
                                String sUrl = "http://camilodc.site/RegistrarCita.php";
                                //llenar parametros
                                RequestParams params = new RequestParams();
                                params.add("id_paciente", "" + preferences3.getInt("id_usuario", 0));
                                params.add("id_dentista", "" + dentista.getId_dentista());
                                params.add("id_dia", "" + dentista.getId_dia());
                                params.add("id_hora", "" + dentista.getId_hora());
                                params.add("fecha", fecha);

                                ahcRegistrar.post(sUrl, params, new BaseJsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                                        if (statusCode == 200) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(rawJsonResponse);
                                                if (jsonArray.length() > 0) {
                                                    String id_cita = jsonArray.getJSONObject(0).getString("id_cita");
                                                    if (id_cita.equals("1")) {
                                                        Intent iMenuPrincipal = new Intent(getApplicationContext(), MenuPrincipal.class);
                                                        startActivity(iMenuPrincipal);
                                                        Toast.makeText(getApplicationContext(), "Cita registrada con éxito!", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Lo sentimos, esta cita ya está reservada...", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                                        Toast.makeText(getApplicationContext(), "Vaya! hubo un problema al registrar...", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                                        return null;
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Ya tienes una cita reservada!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }
}