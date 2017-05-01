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
        tvNombre.setText(product.getsName());
        tvDescrip.setText(product.getDescription());
        //Fav button
        if(product.getFav()) {
            ivProductFav.setVisibility(View.VISIBLE);
        }else {
            ivProductFav.setVisibility(View.INVISIBLE);
        }
        return convertView;

    }
}