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

public class MostrarHorasDentistaAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<HorarioDentista> listHoras;

    public MostrarHorasDentistaAdapter() {
    }

    public MostrarHorasDentistaAdapter(Context context, int layout, ArrayList<HorarioDentista> listHoras) {
        this.context = context;
        this.layout = layout;
        this.listHoras = listHoras;
    }

    @Override
    public int getCount() {
        return listHoras.size();
    }

    @Override
    public Object getItem(int i) {
        return listHoras.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView horas;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View fila = view;
        ViewHolder holder = new ViewHolder();

        if(fila == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            fila = inflater.inflate(layout, null);
            holder.horas = fila.findViewById(R.id.lblHoras);
            fila.setTag(holder);
        }
        else
            holder = (MostrarHorasDentistaAdapter.ViewHolder)fila.getTag();

        HorarioDentista dentista = listHoras.get(i);

        holder.horas.setText(dentista.getHora_inicio()+" a "+dentista.getHora_fin());
        return fila;
    }
}
