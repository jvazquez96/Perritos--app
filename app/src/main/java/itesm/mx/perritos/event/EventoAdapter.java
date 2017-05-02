package itesm.mx.perritos.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;

import itesm.mx.perritos.R;


public class EventoAdapter extends ArrayAdapter<Evento> {

    private ArrayList<Evento> eventos;


    public EventoAdapter(Context context, ArrayList<Evento> eventos) {
        super(context,0,eventos);
        this.eventos = eventos;
    }

    public ArrayList<Evento> getEvento() {
        return this.eventos;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evento eventos1 = eventos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.evento_info,parent,false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.text_title);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.text_description);
        TextView tvDireccion = (TextView) convertView.findViewById(R.id.direccion);
        TextView tvTiempoFal = (TextView) convertView.findViewById(R.id.tiempo_faltante);

        tvTitle.setText(eventos1.getTitle());
        tvDescription.setText(eventos1.getDescription());
        tvDireccion.setText(eventos1.getLugar());
        tvTiempoFal.setText(eventos1.getStartDate() + " Dias");

        return convertView;
    }
}
