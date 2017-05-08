package itesm.mx.perritos.event;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import itesm.mx.perritos.R;
import itesm.mx.perritos.Utils.CurrentUser;

import static android.R.attr.resource;


public class EventoAdapter extends ArrayAdapter<Evento> {

    private ArrayList<Evento> eventos;


    public EventoAdapter(Context context, ArrayList<Evento> eventos) {
        super(context,0,eventos);
        this.eventos = eventos;
    }

    public ArrayList<Evento> getEvento() {
        return this.eventos;
    }

    public String Borrar_Año(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length()-5);
        }
        return str;
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
        ImageView ivEventFav = (ImageView) convertView.findViewById(R.id.EventFavBtn);


        tvTitle.setText(eventos1.getTitle());
        tvDescription.setText(eventos1.getDescription());
        tvDireccion.setText(eventos1.getLugar());
        String FechaCorta = Borrar_Año(eventos1.getStartDate());
        tvTiempoFal.setText(FechaCorta);

        ImageView ivVisible = (ImageView) convertView.findViewById(R.id.eventVisibleItem);

        //VISIBLE
        if(eventos1.getLugarVisible()){
            ivVisible.setVisibility(View.INVISIBLE);
        }else{
            ivVisible.setVisibility(View.VISIBLE);
        }

        final ImageView ivCover = (ImageView) convertView.findViewById(R.id.evento_icons);
        if(eventos1.getphotoURL() != null) {
            // Glide library using circular image crop
            Glide.with(ivCover.getContext()).load(eventos1.getphotoURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivCover) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(ivCover.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivCover.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        //Fav image para eventos  POR IMPLEMENTAR
        if(eventos1.isUserInList(CurrentUser.getmInstance().getUserEmail())) {
            ivEventFav.setVisibility(View.VISIBLE);
        }else {
            ivEventFav.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
