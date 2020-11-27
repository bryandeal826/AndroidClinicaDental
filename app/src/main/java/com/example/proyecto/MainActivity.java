package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar jpbCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jpbCarga = findViewById(R.id.pbCarga);

        Thread tMain = new Thread(){
            @Override
            public void run(){
                try {
                    super.run();
                    //metodos de validacion del programa
                    sleep(3000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(iLogin);
                    finish();
                }
            }
        };
        tMain.start();
    }
}