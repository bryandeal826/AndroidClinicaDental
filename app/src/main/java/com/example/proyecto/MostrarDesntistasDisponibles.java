package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MostrarDesntistasDisponibles extends AppCompatActivity {
    ListView jlvMostrarDentistasDisponibles;
    ArrayList<HorarioDentista> lista;
    MostrarDentistasDisponiblesAdapter adapter = null;
    private String turno;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_desntistas_disponibles);

        jlvMostrarDentistasDisponibles = findViewById(R.id.lvMostrarDentistasDisponibles);
        lista = new ArrayList<>();
        adapter = new MostrarDentistasDisponiblesAdapter(this, R.layout.item_lv_dentistas_disponibles, lista);
        jlvMostrarDentistasDisponibles.setAdapter(adapter);

        turno = this.getIntent().getStringExtra("turno");
        int dia_id = this.getIntent().getIntExtra("dia_id",0);
        fecha = this.getIntent().getStringExtra("fecha");

        MostrarDentistas(dia_id,turno);

        jlvMostrarDentistasDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MostrarDesntistasDisponibles.this, RegistrarCita.class);
                intent.putExtra("turno",turno);
                intent.putExtra("fecha",fecha);
                intent.putExtra("datosDentista", lista.get(i));
                startActivity(intent);
            }
        });
    }

    private void MostrarDentistas(int dia_id, String turno) {
        AsyncHttpClient ahcMostrar = new AsyncHttpClient();

        String sUrl = "http://camilodc.site/DentistasDisponibles.php";
        RequestParams params = new RequestParams();
        params.add("dia_id",""+dia_id);
        params.add("turno",turno);

        ahcMostrar.post(sUrl, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        lista.clear();
                        for(int i=0; i < jsonArray.length(); i++){
                            lista.add(new HorarioDentista(jsonArray.getJSONObject(i).getInt("id_dentista"),
                                    jsonArray.getJSONObject(i).getString("foto"),
                                    jsonArray.getJSONObject(i).getString("nombres"),
                                    jsonArray.getJSONObject(i).getString("apellidos"),
                                    jsonArray.getJSONObject(i).getString("correo"),
                                    jsonArray.getJSONObject(i).getInt("id_dia"),
                                    jsonArray.getJSONObject(i).getInt("id_hora")));
                        }
                        adapter.notifyDataSetChanged();
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