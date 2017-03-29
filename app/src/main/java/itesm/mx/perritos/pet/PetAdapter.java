package itesm.mx.perritos.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import itesm.mx.perritos.R;

/**
 * Created by jorgevazquez on 3/17/17.
 */

public class PetAdapter extends ArrayAdapter<Pet> {

    private ArrayList<Pet> pets;

    /**
     * Constructor
     * @param context Application context
     * @param pets List of pets
     */
    public PetAdapter(Context context, ArrayList<Pet> pets) {
        super(context,0, pets);
        this.pets = pets;
    }

    /**
     * Get the list of pets
     * @return pets
     */
    public ArrayList<Pet> getPets() {
        return this.pets;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pet pet = pets.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pet_info,parent,false);
        }

        ImageView ivCover = (ImageView) convertView.findViewById(R.id.image_cover);
        TextView tvName = (TextView) convertView.findViewById(R.id.text_name);
        TextView tvGender = (TextView) convertView.findViewById(R.id.text_gender);
        TextView tvAge = (TextView) convertView.findViewById(R.id.text_age);
        TextView tvRequests = (TextView) convertView.findViewById(R.id.text_request);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.text_description);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
        byte[] decodedString = Base64.decode(pet.getEncodedImage(),Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
        ivCover.setImageBitmap(decodedByte);
        //ivCover.setLayoutParams(params);
        //ivCover.setImageResource(pet.getIdImage());
        tvName.setText(pet.getName());
        tvGender.setText("Genero: " +pet.getGender());
        tvAge.setText("Edad: " + String.valueOf(pet.getAge()) + "anos");
        tvRequests.setText(String.valueOf(pet.getRequest()) + " solicitudes");


        return convertView;
    }
}
