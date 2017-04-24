package itesm.mx.perritos.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import itesm.mx.perritos.R;

/**
 * Created by DELL1 on 28/03/2017.
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> arrayList;

    public ProductAdapter(Context context, ArrayList<Product> arrayList){
        super(context, 0, arrayList);
        this.arrayList = arrayList;
    }

    public ArrayList<Product> getArrayList(){
        return arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = arrayList.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_info, parent, false);
        }

        ImageView ivFoto = (ImageView) convertView.findViewById(R.id.image_product);
        TextView tvNombre = (TextView) convertView.findViewById(R.id.text_productName);

        ivFoto.setImageResource(product.getiPicture());
        tvNombre.setText(product.getsName());

        return convertView;
    }
}
