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
 * Created by alextrujillo on 20/03/17.
 */

public class EventoAdapter extends ArrayAdapter<Evento> {
    private ArrayList<Evento> eventos;


    public EventoAdapter(Context context, ArrayList<Evento> eventos) {
        super(context,0, eventos);
        this.eventos = eventos;
    }

    /**
     * Get the list of eventos
     * @return eventos
     */
    public ArrayList<Evento> getEventos() {
        return this.eventos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evento evento = eventos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_eventos,parent,false);
        }

        ImageView ivCover = (ImageView) convertView.findViewById(R.id.image_cover);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.text_noticia);
        TextView tvDescrip = (TextView) convertView.findViewById(R.id.text_description);


        ivCover.setImageResource(evento.getIdImage());
        tvTitle.setText(evento.getTitle());
        tvDescrip.setText(evento.getDescription());

        return convertView;
    }
}
