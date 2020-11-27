package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String mensajeActivity;
    String mensaje, titulo;
    Button jbtnSalir,jbtnIngresar;
    TextView  jlblRegistrarse,jlblRecuperala;
    EditText jtxtUsuario, jtxtClave;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        jlblRegistrarse=findViewById(R.id.lblRegistrarse);
        jlblRecuperala=findViewById(R.id.lblRecuperala);
        jbtnIngresar=findViewById(R.id.btnIngresar);
        jbtnSalir=findViewById(R.id.btnSalir);
        jtxtUsuario=findViewById(R.id.txtUsuario);
        jtxtClave=findViewById(R.id.txtClave);

        preferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        editor = preferences.edit();

        if(preferences.contains("nombres")){
            Intent iMenuPrincipal = new Intent(getApplicationContext(), MenuPrincipal.class);
            startActivity(iMenuPrincipal);
            finish();
        }

        jtxtUsuario.setOnClickListener(this);
        jtxtClave.setOnClickListener(this);
        jlblRegistrarse.setOnClickListener(this);
        jlblRecuperala.setOnClickListener(this);
        jbtnIngresar.setOnClickListener(this);
        jbtnSalir.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lblRegistrarse :
                registrar();
                break;
            case R.id.lblRecuperala:
                recuperala();
                break;
            case R.id.btnIngresar:
                Ingresar();
                break;
            case R.id.btnSalir:
                Salirrr();
                break;
        }
    }

    private void Salirrr() {
        finish();
        System.exit(1);
    }

    private void Ingresar() {
        String usuario = jtxtUsuario.getText().toString().trim();
        String password = jtxtClave.getText().toString().trim();
        boolean validacion = ValidarCampos(usuario, password);
        if(validacion){
            IniciarSesion(usuario,password);
        }
    }

    private boolean ValidarCampos(String usuario, String password) {
        boolean validacion = true;
        if(usuario.isEmpty()) {
            jtxtUsuario.setError("El campo no puede estar vacío.");
            validacion = false;
        }
        if(password.isEmpty()) {
            jtxtClave.setError("El campo no puede estar vacío.");
            validacion = false;
        }
        return validacion;
    }

    public void IniciarSesion(String usuario, String password) {
        AsyncHttpClient cliente = new AsyncHttpClient();
        Hash hash = new Hash();
        String accion = "Ingresar";

        String sURL ="http://camilodc.site/usuarios.php";

        RequestParams params = new RequestParams();
        params.add("usuario",usuario);
        params.add("contrasena", hash.StringToHash(password, "SHA1"));
        params.add("accion", accion);

        cliente.post(sURL, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        if(jsonArray.length() > 0){
                            String estado = jsonArray.getJSONObject(0).getString("estado");
                            if(estado.equals("-1")){
                                Toast.makeText(getApplicationContext(), "Usuario/Clave incorrecta", Toast.LENGTH_SHORT).show();
                            }else if(estado.equals("habilitado")){
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
                                editor.putInt("id_usuario", us.getId());
                                editor.putString("nombres", us.getNombres());
                                editor.putString("apellidos", us.getApellidos());
                                editor.commit();
                                Intent iMenuPrincipal = new Intent(getApplicationContext(), MenuPrincipal.class);
                                startActivity(iMenuPrincipal);
                                finish();
                            }else if(estado.equals("inhabilitado")){
                                Toast.makeText(getApplicationContext(), "Hola "+ jsonArray.getJSONObject(0).getString("nombres") + ", tu cuenta está inhabilitada", Toast.LENGTH_SHORT).show();
                                String codigo = GenerarCodigo();
                                Usuario us = new Usuario(Integer.parseInt(jsonArray.getJSONObject(0).getString("id_usuario")),
                                        jsonArray.getJSONObject(0).getString("usuario"),
                                        jsonArray.getJSONObject(0).getString("contrasena"),
                                        jsonArray.getJSONObject(0).getString("nombres"),
                                        jsonArray.getJSONObject(0).getString("apellidos"),
                                        jsonArray.getJSONObject(0).getString("telefono"),
                                        jsonArray.getJSONObject(0).getString("correo"),
                                        jsonArray.getJSONObject(0).getString("direccion"));
                                mensaje = "Hola "+us.getNombres()+", "+us.getApellidos()+", este es su código de verificación: "+codigo;
                                titulo = "AngelDent - Valida tu cuenta!";
                                EnviarEmail(us.getCorreo(),titulo,mensaje);
                                mensajeActivity = "Hola "+us.getNombres()+", su cuenta se encuentra inhabilitada.\n";
                                mensajeActivity += "Se le ha enviando un token a su correo: "+us.getCorreo();
                                Intent iValidacion = new Intent(getApplicationContext(), ValidacionActivity.class);
                                iValidacion.putExtra("mensajeActivity", mensajeActivity);
                                iValidacion.putExtra("codigo", codigo);
                                iValidacion.putExtra("id_usuario", us.getId());
                                startActivity(iValidacion);
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

    private void EnviarEmail(String destino, String titulo, String mensaje) {
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, destino, titulo, mensaje);
        javaMailAPI.execute();
    }

    private void recuperala() {
        Intent intent = new Intent(this,RecuperarActivity.class);
        startActivity(intent);
    }

    private void registrar() {
        Intent intent = new Intent(this,RegistroActivity.class);
        startActivity(intent);
    }

    public String GenerarCodigo(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}