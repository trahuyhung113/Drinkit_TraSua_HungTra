package com.example.hungtra.drinkit_trasua.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.hungtra.drinkit_trasua.Database.ModelDB.Cart;
import com.example.hungtra.drinkit_trasua.R;
import com.example.hungtra.drinkit_trasua.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,
                viewGroup, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int i) {

        Picasso.with(context)
                .load(cartList.get(i).link)
                .into(cartViewHolder.img_product);

        cartViewHolder.txt_amount.setNumber(String.valueOf(cartList.get(i).amount));
        cartViewHolder.txt_price.setText(new StringBuilder("$").append(cartList.get(i).price));
        cartViewHolder.txt_product_name.setText(new StringBuilder(cartList.get(i).name)
        .append(" x")
        .append(cartList.get(i).amount)
        .append(cartList.get(i).size == 0 ? " Size M":" Size L"));

        cartViewHolder.txt_sugar_ice.setText(new StringBuilder("Sugar: ")
        .append(cartList.get(i).sugar).append("%").append("\n")
        .append("Ice: ").append(cartList.get(i).ice)
        .append("%").toString());

        //Get Price of one cup with all options
        final double priceOneCup = cartList.get(i).price / cartList.get(i).amount;

        //Tự động lưu mục khi người dùng thay đổi tiền
        cartViewHolder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = cartList.get(i);
                cart.amount = newValue;
                cart.price = Math.floor(priceOneCup*newValue);
                Common.cartRepository.updateCart(cart);

                cartViewHolder.txt_price.setText(new StringBuilder("$").append(cartList.get(i).price));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView img_product;
        TextView txt_product_name, txt_sugar_ice, txt_price;
        ElegantNumberButton txt_amount;

        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = (ImageView)itemView.findViewById(R.id.img_product);
            txt_product_name = (TextView)itemView.findViewById(R.id.txt_product_name);
            txt_sugar_ice = (TextView)itemView.findViewById(R.id.txt_sugar_ice);
            txt_price = (TextView)itemView.findViewById(R.id.txt_price);
            txt_amount = (ElegantNumberButton)itemView.findViewById(R.id.txt_amount);

            view_background = (RelativeLayout)itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout)itemView.findViewById(R.id.view_foreground);

        }
    }
    public void removeItem(int position){
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position){
        cartList.add(position,item);
        notifyItemInserted(position);

    }
}
