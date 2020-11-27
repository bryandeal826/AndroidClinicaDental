package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class ReservaDeCita extends AppCompatActivity implements View.OnClickListener{
    EditText jtxtfechaReserva;
    ImageView jiconfechaReserva;
    Button jbtnSeguir;
    Spinner jspTurno;
    int dia, mes, anio;
    int diaSeleccion, mesSeleccion, anioSeleccion;
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_de_cita);

        jtxtfechaReserva = findViewById(R.id.txtfechaReserva);
        jiconfechaReserva = findViewById(R.id.iconfechaReserva);
        jbtnSeguir = findViewById(R.id.btnSeguir);
        jspTurno = findViewById(R.id.spTurno);

        jiconfechaReserva.setOnClickListener(this);
        jbtnSeguir.setOnClickListener(this);


        anio = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        final String selectedDate = twoDigits(dia) + "-" + twoDigits(mes+1) + "-" + anio;
        jtxtfechaReserva.setText(selectedDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iconfechaReserva :
                showDatePickerDialog();
                break;
            case R.id.btnSeguir :
                Continuar();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                if(year>=anio && year<=anio+1){
                    if(month>=mes){
                        if(day>=dia){
                            jtxtfechaReserva.setError(null);
                            final String selectedDate = twoDigits(day) + "-" + twoDigits(month+1) + "-" + year;
                            jtxtfechaReserva.setText(selectedDate);
                            diaSeleccion=day;
                            mesSeleccion=month;
                            anioSeleccion=year;
                        }else{
                            jtxtfechaReserva.setError("");
                            Toast.makeText(getApplicationContext(), "Día incorrecto", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        jtxtfechaReserva.setError("");
                        Toast.makeText(getApplicationContext(), "Mes incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Año incorrecto", Toast.LENGTH_SHORT).show();
                    jtxtfechaReserva.setError("");
                }
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private void Continuar() {
        boolean validacion = ValidarCampos();
        if(validacion){
            String fechaSeleccionada = jtxtfechaReserva.getText().toString().trim();
            int anioActual = c.get(Calendar.YEAR);
            int mesActual = c.get(Calendar.MONTH);
            int diaActual = c.get(Calendar.DAY_OF_MONTH);
            String fechaActual = twoDigits(diaActual) + "-" + twoDigits(mesActual+1) + "-" + anio;
            if(fechaSeleccionada.equals(fechaActual)){
                String turno = jspTurno.getSelectedItem().toString().trim();
                int HoraActual = c.get(Calendar.HOUR_OF_DAY);
                if(turno.equals("Mañana") && HoraActual<8 || turno.equals("Tarde") && HoraActual<12){
                    String[] parts = fechaSeleccionada.split("-");
                    int diaAux = Integer.parseInt(parts[0]);
                    int mesAux = Integer.parseInt(parts[1])-1;
                    int anioAux = Integer.parseInt(parts[2]);
                    c.set(anioAux, mesAux, diaAux);
                    int diaSemana = c.get(Calendar.DAY_OF_WEEK);
                    Intent iDentistasDisponibles = new Intent(getApplicationContext(), MostrarDesntistasDisponibles.class);
                    iDentistasDisponibles.putExtra("fecha", fechaSeleccionada);
                    iDentistasDisponibles.putExtra("dia_id", diaSemana);
                    iDentistasDisponibles.putExtra("turno", turno);
                    startActivity(iDentistasDisponibles);
                }else{
                    Toast.makeText(getApplicationContext(), "Lo sentimos, ya no admitimos reservas por hoy en el turno seleccionado.", Toast.LENGTH_SHORT).show();
                }
            }else{
                String turno = jspTurno.getSelectedItem().toString().trim();
                String[] parts = fechaSeleccionada.split("-");
                int diaAux = Integer.parseInt(parts[0]);
                int mesAux = Integer.parseInt(parts[1])-1;
                int anioAux = Integer.parseInt(parts[2]);
                c.set(anioAux, mesAux, diaAux);
                int diaSemana = c.get(Calendar.DAY_OF_WEEK);
                Intent iDentistasDisponibles = new Intent(getApplicationContext(), MostrarDesntistasDisponibles.class);
                iDentistasDisponibles.putExtra("fecha", fechaSeleccionada);
                iDentistasDisponibles.putExtra("dia_id", diaSemana);
                iDentistasDisponibles.putExtra("turno", turno);
                startActivity(iDentistasDisponibles);
            }
        }
    }

    private boolean ValidarCampos() {
        boolean validacion = true;
        if (jspTurno.getSelectedItem().toString().trim().equals("Seleccione una opción")) {
            validacion = false;
            Toast.makeText(getApplicationContext(), "Seleccione un turno!", Toast.LENGTH_SHORT).show();
        }
        return validacion;
    }
}