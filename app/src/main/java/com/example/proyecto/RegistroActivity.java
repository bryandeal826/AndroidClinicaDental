package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{
    String mensajeActivity;
    String mensaje, titulo;
    EditText jtxtCorreo, jtxtUsuario, jtxtClave, jtxtNombre, jtxtApellido, jtxtTelefono, jtxtDireccion;

    Button jbtnContinuar , jbtnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        jtxtCorreo = findViewById(R.id.txtCorreo);
        jtxtUsuario = findViewById(R.id.txtUsuario);
        jtxtClave = findViewById(R.id.txtClave);
        jtxtNombre = findViewById(R.id.txtNombre);
        jtxtApellido = findViewById(R.id.txtApellido);
        jtxtTelefono = findViewById(R.id.txtTelefono);
        jtxtDireccion = findViewById(R.id.txtDireccion);
        jbtnContinuar = findViewById(R.id.btnContinuar);
        jbtnAtras = findViewById(R.id.btnAtras);

        jbtnContinuar.setOnClickListener(this);
        jbtnAtras.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnContinuar:
                Registrar();
                break;
            case R.id.btnAtras:
                Atras();
                break;
        }

    }

    private void Registrar() {
        //Validaciones antes de registrar
        boolean validacion = Campos();
        boolean validarCorreo = false;
        boolean LongitudTelf = false;
        if(validacion){
            validarCorreo = validarEmail(jtxtCorreo.getText().toString().trim());
            if(validarCorreo){
                LongitudTelf = TelefonoLongitud();
            }else{
                jtxtCorreo.setError("Ingrese un email válido.");
            }
        }
        if(validarCorreo==true && validacion==true && LongitudTelf==true){
            AsyncHttpClient ahcRegistrar = new AsyncHttpClient();
            Hash hash = new Hash();
            String sUrl = "http://camilodc.site/pacientes.php";
            //llenar parametros
            RequestParams params = new RequestParams();
            params.add("usuario", jtxtUsuario.getText().toString().trim());
            params.add("contrasena", hash.StringToHash(jtxtClave.getText().toString().trim(), "SHA1"));
            params.add("nombre", jtxtNombre.getText().toString().trim());
            params.add("apellido", jtxtApellido.getText().toString().trim());
            params.add("telefono", jtxtTelefono.getText().toString().trim());
            params.add("email",jtxtCorreo.getText().toString().trim());
            params.add("direccion", jtxtDireccion.getText().toString().trim());

            ahcRegistrar.post(sUrl, params, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if(statusCode == 200){
                        try {
                            JSONArray jsonArray = new JSONArray(rawJsonResponse);
                            if(jsonArray.length() > 0) {
                                String sCorreo = jsonArray.getJSONObject(0).getString("correo");
                                if (sCorreo.equals("-1")) {
                                    jtxtCorreo.setError("El correo ya existe en nuestra base de datos!");
                                } else {
                                    Usuario us = new Usuario(Integer.parseInt(jsonArray.getJSONObject(0).getString("id_usuario")),
                                            jsonArray.getJSONObject(0).getString("usuario"),
                                            jsonArray.getJSONObject(0).getString("contrasena"),
                                            jsonArray.getJSONObject(0).getString("nombres"),
                                            jsonArray.getJSONObject(0).getString("apellidos"),
                                            jsonArray.getJSONObject(0).getString("telefono"),
                                            jsonArray.getJSONObject(0).getString("correo"),
                                            jsonArray.getJSONObject(0).getString("direccion"));
                                    String codigo = GenerarCodigo();
                                    mensaje = "Hola "+us.getNombres()+", "+us.getApellidos()+", este es su código de verificación: "+codigo;
                                    titulo = "AngelDent - Valida tu cuenta!";
                                    EnviarEmail(us.getCorreo(),titulo,mensaje);
                                    mensajeActivity = "Hola "+us.getNombres()+", para poder continuar valide su cuenta.\n";
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
                    Toast.makeText(getApplicationContext(), "Vaya! problemas al registrar...", Toast.LENGTH_SHORT).show();
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

    public String GenerarCodigo(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean TelefonoLongitud() {
        String telefono = jtxtTelefono.getText().toString().trim();
        boolean validacion=true;
        if(telefono.length()<7 || telefono.length()>9){
            validacion=false;
            jtxtTelefono.setError("El teléfono debe tener entre 7 a 9 caracteres.");
        }
        return validacion;
    }

    private boolean Campos() {
        boolean validacion=true;
        if(jtxtCorreo.getText().toString().trim().isEmpty()){validacion=false;jtxtCorreo.setError("El campo no puede estar vacío");}
        if(jtxtUsuario.getText().toString().trim().isEmpty()){validacion=false;jtxtUsuario.setError("El campo no puede estar vacío");}
        if(jtxtClave.getText().toString().trim().isEmpty()){validacion=false;jtxtClave.setError("El campo no puede estar vacío");}
        if(jtxtNombre.getText().toString().trim().isEmpty()){validacion=false;jtxtNombre.setError("El campo no puede estar vacío");}
        if(jtxtApellido.getText().toString().trim().isEmpty()){validacion=false;jtxtApellido.setError("El campo no puede estar vacío");}
        if(jtxtTelefono.getText().toString().trim().isEmpty()){validacion=false;jtxtTelefono.setError("El campo no puede estar vacío");}
        if(jtxtDireccion.getText().toString().trim().isEmpty()){validacion=false;jtxtDireccion.setError("El campo no puede estar vacío");}
        return  validacion;
    }
    private void Atras() {
        Intent iLogin = new Intent(this, LoginActivity.class);
        startActivity(iLogin);
    }
}