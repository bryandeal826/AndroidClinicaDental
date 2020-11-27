package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class ValidacionActivity extends AppCompatActivity implements View.OnClickListener{
    String codigo;
    int id_usuario;
    TextView jlblMensajeValida;
    EditText jtxtCodigo;
    Button jbtnValidar, jbtnAtras;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacion);

        jlblMensajeValida = findViewById(R.id.lblMensajeValida);
        jtxtCodigo = findViewById(R.id.txtCodigo);
        jbtnValidar = findViewById(R.id.btnValidar);
        jbtnAtras = findViewById(R.id.btnAtras);

        jbtnValidar.setOnClickListener(this);
        jbtnAtras.setOnClickListener(this);

        String mensajeActivity = this.getIntent().getStringExtra("mensajeActivity");
        codigo = this.getIntent().getStringExtra("codigo");
        id_usuario = this.getIntent().getIntExtra("id_usuario",0);

        jlblMensajeValida.setText(mensajeActivity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnValidar:
                Validar();
                break;
            case R.id.btnAtras:
                Atras();
                break;
        }
    }

    private void Validar() {
        boolean validacion = ValidarCampos();
        if(validacion){
            if(jtxtCodigo.getText().toString().trim().equals(codigo)){
                AsyncHttpClient usuario = new AsyncHttpClient();
                String accion = "Validar";

                String sURL ="http://camilodc.site/usuarios.php";

                RequestParams params = new RequestParams();
                params.add("accion",accion);
                params.add("id_usuario", ""+id_usuario);

                usuario.post(sURL, params, new BaseJsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                        if(statusCode == 200){
                            try {
                                JSONArray jsonArray = new JSONArray(rawJsonResponse);
                                if(jsonArray.length() > 0){
                                    Toast.makeText(getApplicationContext(), "Bienvenido "+ jsonArray.getJSONObject(0).getString("nombres"), Toast.LENGTH_SHORT).show();
                                    Usuario us = new Usuario(Integer.parseInt(jsonArray.getJSONObject(0).getString("id_usuario")),
                                                jsonArray.getJSONObject(0).getString("usuario"),
                                                jsonArray.getJSONObject(0).getString("contrasena"),
                                                jsonArray.getJSONObject(0).getString("nombres"),
                                                jsonArray.getJSONObject(0).getString("apellidos"),
                                                jsonArray.getJSONObject(0).getString("telefono"),
                                                jsonArray.getJSONObject(0).getString("correo"),
                                                jsonArray.getJSONObject(0).getString("direccion"));
                                    //Creamos el archivo para almacenar los datos esenciales de la sesión
                                    preferences = getSharedPreferences("Usuario", MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putInt("id_usuario", us.getId());
                                    editor.putString("nombres", us.getNombres());
                                    editor.putString("apellidos", us.getApellidos());
                                    editor.commit();
                                    Intent iMenuPrincipal = new Intent(getApplicationContext(), MenuPrincipal.class);
                                    startActivity(iMenuPrincipal);
                                    finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Vaya! Hubo problemas...", Toast.LENGTH_SHORT).show();
                                    }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                        Toast.makeText(getApplicationContext(), "Vaya! Hubo problemas...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                        return null;
                    }
                });
            }else{
                jtxtCodigo.setError("Código inválido!");
            }
        }
    }

    private boolean ValidarCampos(){
        boolean validacion = true;
        if(jtxtCodigo.getText().toString().trim().isEmpty()){validacion=false;jtxtCodigo.setError("El campo no puede estar vacío.");}
        return validacion;
    }

    private void Atras() {
        Intent iLogin = new Intent(this, LoginActivity.class);
        startActivity(iLogin);
    }
}