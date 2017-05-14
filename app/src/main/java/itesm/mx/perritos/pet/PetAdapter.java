/*
        Copyright (C) 2017  Jorge Armando Vazquez Ortiz, Valentin Alexandro Trujillo, Santiago Sandoval
        Treviño and Gerardo Suarez Martinez
        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package itesm.mx.perritos.pet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import itesm.mx.perritos.R;
import itesm.mx.perritos.Utils.CurrentUser;

import static java.lang.System.load;

/**
 * Created by jorgevazquez on 3/17/17.
 */

public class PetAdapter extends ArrayAdapter<Pet> {

    private ArrayList<Pet> pets;
    private Application app;
    // Firebase Objects
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


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
        final Pet pet = pets.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pet_info,parent,false);
        }
        ImageView ivPetFav = (ImageView) convertView.findViewById(R.id.petFavBtn);
        TextView tvName = (TextView) convertView.findViewById(R.id.text_name);
        TextView tvGender = (TextView) convertView.findViewById(R.id.text_gender);
        TextView tvAge = (TextView) convertView.findViewById(R.id.text_age);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.text_description);
        TextView tvGenderStatic = (TextView) convertView.findViewById(R.id.textView2);

        tvName.setText(pet.getName());
        tvGender.setText(pet.getGender());
        tvAge.setText(String.valueOf(pet.getAge()));
        tvDescription.setText(pet.getDescription());
        final ImageView ivCover = (ImageView) convertView.findViewById(R.id.image_cover);


        // Glide library using circular image crop
       Glide.with(ivCover.getContext()).load(pet.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivCover) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(ivCover.getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivCover.setImageDrawable(circularBitmapDrawable);
            }
        });

        //Visible PET
        final ImageView ivVisible = (ImageView) convertView.findViewById(R.id.petVisibleItem);

        if(pet.getIsVisible() == true){
            ivVisible.setVisibility(View.INVISIBLE);
        }else{
            ivVisible.setVisibility(View.VISIBLE);
        }


        //Fav button

        if(pet.isUserInList(CurrentUser.getmInstance().getUserEmail())) {
            ivPetFav.setVisibility(View.VISIBLE);
        }else {
            ivPetFav.setVisibility(View.INVISIBLE);
        }

        if(tvGender.getText().equals("Hembra")){
            tvGender.setTextColor(Color.parseColor("#ff6659"));
            tvGenderStatic.setTextColor(Color.parseColor("#ff6659"));
        }else{
            tvGender.setTextColor(Color.parseColor("#768fff"));
            tvGenderStatic.setTextColor(Color.parseColor("#768fff"));
        }

        return convertView;
    }
}
