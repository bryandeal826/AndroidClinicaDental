package com.example.proyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MostrarDentistasDisponiblesAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<HorarioDentista> listDentistasDisponibles;

    public MostrarDentistasDisponiblesAdapter() {
    }

    public MostrarDentistasDisponiblesAdapter(Context context, int layout, ArrayList<HorarioDentista> listDentistasDisponibles) {
        this.context = context;
        this.layout = layout;
        this.listDentistasDisponibles = listDentistasDisponibles;
    }

    @Override
    public int getCount() {
        return listDentistasDisponibles.size();
    }

    @Override
    public Object getItem(int i) {
        return listDentistasDisponibles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView nombres, apellidos, correo;
        ImageView fotoDentista;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View fila = view;
        ViewHolder holder = new ViewHolder();

        if(fila == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            fila = inflater.inflate(layout, null);
            holder.nombres = fila.findViewById(R.id.lblItemNombres);
            holder.apellidos = fila.findViewById(R.id.lblItemApellidos);
            holder.correo = fila.findViewById(R.id.lblItemCorreo);
            holder.fotoDentista = fila.findViewById(R.id.ivItemDentista);
            fila.setTag(holder);
        }
        else
            holder = (ViewHolder)fila.getTag();

        HorarioDentista dentista = listDentistasDisponibles.get(i);

        holder.nombres.setText(dentista.getNombres());
        holder.apellidos.setText(dentista.getApellidos());
        holder.correo.setText(dentista.getCorreo());
        String sFoto = dentista.getFoto();
        byte[] bFoto = Base64.decode(sFoto, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(bFoto,0, bFoto.length);
        holder.fotoDentista.setImageBitmap(decodedImage);
        return fila;
    }


}
