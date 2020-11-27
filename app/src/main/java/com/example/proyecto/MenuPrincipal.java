package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener{
    ImageButton jibubicacion, jibnosotros, jibReservarCita, jibDetalleCita;
    Button jbtnCerrarSesion;
    ArrayList<Cita> lista;

    private SharedPreferences preferences2;
    private SharedPreferences.Editor editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        jibubicacion = findViewById(R.id.Ubicacion);
        jibnosotros = findViewById(R.id.Nosotros);
        jibReservarCita = findViewById(R.id.btnReservarCita);
        jibDetalleCita = findViewById(R.id.btnDetalleCita);
        jbtnCerrarSesion = findViewById(R.id.btnCerrarSesion);


        jibnosotros.setOnClickListener(this);
        jibubicacion.setOnClickListener(this);
        jibReservarCita.setOnClickListener(this);
        jibDetalleCita.setOnClickListener(this);
        jbtnCerrarSesion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReservarCita :
                reservacita();
                break;
            case R.id.btnDetalleCita :
                detallecita();
                break;
            case R.id.Nosotros :
                nosotros();
                break;
            case R.id.Ubicacion :
                ubicacion();
                break;
            case R.id.btnCerrarSesion :
                CerrarSesion();
                break;
        }

    }

    private void detallecita() {
        AsyncHttpClient ahcMostrar = new AsyncHttpClient();
        preferences2 = getSharedPreferences("Usuario", MODE_PRIVATE);
        editor2 = preferences2.edit();

        String sUrl = "http://camilodc.site/ConsultarCita.php";
        RequestParams params = new RequestParams();
        params.add("id_paciente", "" + preferences2.getInt("id_usuario", 0));

        ahcMostrar.post(sUrl, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        if(jsonArray.length() > 0) {
                            String id_cita = jsonArray.getJSONObject(0).getString("id_cita");
                            if (id_cita.equals("-1")) {
                                Toast.makeText(getApplicationContext(), "No tienes citas reservadas", Toast.LENGTH_SHORT).show();
                            } else {
                                String Nombre_pa = jsonArray.getJSONObject(0).getString("nombres");
                                String Apellidos_pa = jsonArray.getJSONObject(0).getString("apellidos");
                                String Nombre_dent = jsonArray.getJSONObject(0).getString("dentista");
                                String Apellidos_dent = jsonArray.getJSONObject(0).getString("dentista_ape");
                                String fecha = jsonArray.getJSONObject(0).getString("fecha");
                                String dia = jsonArray.getJSONObject(0).getString("dia");
                                String Hora_inicio = jsonArray.getJSONObject(0).getString("hora_inicio");
                                String Hora_fin = jsonArray.getJSONObject(0).getString("hora_fin");
                                String estado = jsonArray.getJSONObject(0).getString("estado");
                                Cita cita = new Cita(Integer.parseInt(id_cita),Nombre_pa,Apellidos_pa,Nombre_dent,Apellidos_dent,fecha,dia,Hora_inicio,Hora_fin,estado);
                                Intent intent = new Intent(MenuPrincipal.this, DetalleCita.class);
                                intent.putExtra("datosCita", cita);
                                startActivity(intent);
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

    private void reservacita() {
        Intent iReservaCita = new Intent(getApplicationContext(), ReservaDeCita.class);
        startActivity(iReservaCita);
    }

    private void CerrarSesion() {
        preferences2 = getSharedPreferences("Usuario", MODE_PRIVATE);
        editor2 = preferences2.edit();
        editor2.clear();
        editor2.commit();
        Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(iLogin);
        finish();
    }

    private void nosotros() {
        Intent intent = new Intent(this,NosotrosActivity.class);
        startActivity(intent);
    }

    private void ubicacion() {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}