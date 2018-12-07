package com.example.hungtra.drinkit_trasua.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hungtra.drinkit_trasua.Interface.IItemClickListener;
import com.example.hungtra.drinkit_trasua.R;


public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView txt_drink_name, txt_price;
    ImageView img_product;

    IItemClickListener itemClickListener;

    ImageView btn_add_to_cart, btn_favorites;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DrinkViewHolder(@NonNull View itemView) {
        super(itemView);

        img_product = (ImageView)itemView.findViewById(R.id.image_product);
        txt_drink_name = (TextView)itemView.findViewById(R.id.txt_drink_name);
        txt_price = (TextView)itemView.findViewById(R.id.txt_price);
        btn_add_to_cart = (ImageView) itemView.findViewById(R.id.btn_add_cart);
        btn_favorites = (ImageView) itemView.findViewById(R.id.btn_favorite);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view);

    }
}
