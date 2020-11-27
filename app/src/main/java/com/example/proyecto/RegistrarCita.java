package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RegistrarCita extends AppCompatActivity {
    private HorarioDentista dentista;
    private String turno;
    private String fecha;
    ListView jlvMostrarHorasDisponibles;
    ArrayList<HorarioDentista> lista;
    MostrarHorasDentistaAdapter adapter = null;

    TextView jlblItemDatoPersonal, jlblItemCorreoPersonal;
    ImageView jivItemFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cita);

        dentista = (HorarioDentista) getIntent().getSerializableExtra("datosDentista");
        turno = this.getIntent().getStringExtra("turno");
        fecha = this.getIntent().getStringExtra("fecha");

        jlvMostrarHorasDisponibles = findViewById(R.id.lvMostrarHorasDisponibles);
        jlblItemDatoPersonal = findViewById(R.id.lblItemDatoPersonal);
        jlblItemCorreoPersonal = findViewById(R.id.lblItemCorreoPersonal);
        jivItemFoto = findViewById(R.id.ivItemFoto);

        jlblItemDatoPersonal.setText(dentista.getNombres()+", "+dentista.getApellidos());
        jlblItemCorreoPersonal.setText(dentista.getCorreo());
        String sFoto = dentista.getFoto();
        byte[] bFoto = Base64.decode(sFoto, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(bFoto,0, bFoto.length);
        jivItemFoto.setImageBitmap(decodedImage);

        lista = new ArrayList<>();
        adapter = new MostrarHorasDentistaAdapter(this, R.layout.item_lv_seleccionar_hora, lista);
        jlvMostrarHorasDisponibles.setAdapter(adapter);

        MostrarHoras(dentista.getId_dentista(),dentista.getId_dia(), turno);

        jlvMostrarHorasDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RegistrarCita.this, RegistrarCita2.class);
                intent.putExtra("turno",turno);
                intent.putExtra("fecha",fecha);
                intent.putExtra("datosDentista", lista.get(i));
                startActivity(intent);
            }
        });
    }

    private void MostrarHoras(int id_dentista, int id_dia, String turno) {
        AsyncHttpClient ahcMostrar = new AsyncHttpClient();

        String sUrl = "http://camilodc.site/DentistaHoraDisponible.php";
        RequestParams params = new RequestParams();
        params.add("id_dentista",""+id_dentista);
        params.add("id_dia",""+id_dia);
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
                                    jsonArray.getJSONObject(i).getString("dia"),
                                    jsonArray.getJSONObject(i).getString("hora_inicio"),
                                    jsonArray.getJSONObject(i).getString("hora_fin"),
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