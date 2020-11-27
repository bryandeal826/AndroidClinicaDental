package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class RecuperarActivity extends AppCompatActivity implements View.OnClickListener{
    String mensaje, titulo;
    EditText jtxtEmail;
    Button jbtnEnviar, jbtnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        jtxtEmail = findViewById(R.id.txtEmail);
        jbtnEnviar = findViewById(R.id.btnEnviar);
        jbtnAtras = findViewById(R.id.btnAtras);

        jbtnEnviar.setOnClickListener(this);
        jbtnAtras.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEnviar:
                Enviar();
                break;
            case R.id.btnAtras:
                Atras();
                break;
        }
    }

    private void Enviar() {
        boolean validacion = Campos();
        boolean validarCorreo = false;
        if(validacion){
            validarCorreo = validarEmail(jtxtEmail.getText().toString().trim());
        }
        if(validacion==true && validarCorreo==true){
            AsyncHttpClient ahcEnviar = new AsyncHttpClient();
            String sUrl = "http://camilodc.site/usuarios.php";
            String accion = "Recuperar";
            //llenar parametros
            RequestParams params = new RequestParams();
            params.add("email", jtxtEmail.getText().toString().trim());
            params.add("accion", accion);
            ahcEnviar.post(sUrl, params, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if(statusCode == 200){
                        try {
                            JSONArray jsonArray = new JSONArray(rawJsonResponse);
                            if(jsonArray.length() > 0){
                                String usuario = jsonArray.getJSONObject(0).getString("usuario");
                                if(usuario.equals("-1")){
                                    jtxtEmail.setError("EL correo ingresado no existe en nuestra base de datos.");
                                }else{
                                    String contrasena = jsonArray.getJSONObject(0).getString("contrasena");
                                    titulo = "AngelDent - Recupera tu cuenta!";
                                    mensaje = "Estos son tus datos para iniciar sesión: \n";
                                    mensaje += "Usuario: "+usuario+"\n";
                                    mensaje += "Contraseña: "+contrasena+"\n";
                                    EnviarEmail(jtxtEmail.getText().toString().trim(), titulo, mensaje);
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

    private void EnviarEmail(String destino, String titulo, String mensaje) {
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, destino, titulo, mensaje);
        javaMailAPI.execute();
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean Campos() {
        boolean validacion=true;
        if(jtxtEmail.getText().toString().trim().isEmpty()){validacion=false;jtxtEmail.setError("El campo no puede estar vacío");}
        return  validacion;
    }

    private void Atras() {
        Intent iLogin = new Intent(this, LoginActivity.class);
        startActivity(iLogin);
    }
}