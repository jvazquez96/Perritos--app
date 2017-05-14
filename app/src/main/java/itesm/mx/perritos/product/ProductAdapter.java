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
package itesm.mx.perritos.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import itesm.mx.perritos.R;
import itesm.mx.perritos.Utils.CurrentUser;

/**
 * Created by DELL1 on 28/03/2017.
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> arrayList;


    public ProductAdapter(Context context, ArrayList<Product> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
    }

    public ArrayList<Product> getArrayList() {
        return arrayList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = arrayList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_info, parent, false);
        }

        ImageView ivProductFav = (ImageView) convertView.findViewById(R.id.Product_Fav);
        final ImageView ivFoto = (ImageView) convertView.findViewById(R.id.image_product);
        TextView tvNombre = (TextView) convertView.findViewById(R.id.text_productName);
        TextView tvDescrip = (TextView) convertView.findViewById(R.id.text_description);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.text_productPrecio);

        // Glide library using circular image crop
        Glide.with(ivFoto.getContext()).load(product.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivFoto) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(ivFoto.getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivFoto.setImageDrawable(circularBitmapDrawable);
            }
        });

        ImageView ivVisible = (ImageView) convertView.findViewById(R.id.productVisibleItem);

        if(product.getIsVisible()){
            ivVisible.setVisibility(View.INVISIBLE);
        }else{
            ivVisible.setVisibility(View.VISIBLE);
        }

        tvNombre.setText(product.getsName());
        tvDescrip.setText(product.getDescription());
        tvPrice.setText(String.valueOf("$" + product.getdPrice()));
        //Fav button
        if(product.isUserInList(CurrentUser.getmInstance().getUserEmail())) {
            ivProductFav.setVisibility(View.VISIBLE);
        }else {
            ivProductFav.setVisibility(View.INVISIBLE);
        }
        return convertView;

    }
}