package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NosotrosActivity extends AppCompatActivity implements View.OnClickListener{
    Button jbtnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_nosotros) ;
        jbtnAtras = findViewById(R.id.btnAtras) ;

        jbtnAtras.setOnClickListener(this) ; }

    @Override
    public void onClick(View view) {
        switch (view.getId() ){
            case R.id.btnAtras:
                Atras() ;
                break ;
        }
    }

    private void Atras() {
        Intent iPrincipal = new Intent(this, MenuPrincipal.class);
        startActivity(iPrincipal) ;
        finish();
    }
}