package itesm.mx.perritos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jorgevazquez on 3/20/17.
 */

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

        ImageView ivCover = (ImageView) convertView.findViewById(R.id.image_evento);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.text_title);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.text_description);

        ivCover.setImageResource(eventos1.getIdImage());
        tvTitle.setText(eventos1.getTitle());
        tvDescription.setText(eventos1.getDescription());

        return convertView;
    }
}
