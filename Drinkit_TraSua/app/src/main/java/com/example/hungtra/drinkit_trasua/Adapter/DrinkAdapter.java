package com.example.hungtra.drinkit_trasua.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.hungtra.drinkit_trasua.Database.ModelDB.Cart;
import com.example.hungtra.drinkit_trasua.Database.ModelDB.Favorite;
import com.example.hungtra.drinkit_trasua.Interface.IItemClickListener;
import com.example.hungtra.drinkit_trasua.Model.Drink;
import com.example.hungtra.drinkit_trasua.R;
import com.example.hungtra.drinkit_trasua.Utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    Context context;
    List<Drink> drinkList;

    int poss;

    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.drink_item_layout,null);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrinkViewHolder drinkViewHolder, final int i) {

        drinkViewHolder.txt_price.setText(new StringBuilder("$")
                .append(drinkList.get(i).Price).toString());
        drinkViewHolder.txt_drink_name.setText(drinkList.get(i).Name);

        drinkViewHolder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddToCartDialog(i);
                poss = i;
            }
        });

        Picasso.with(context)
                .load(drinkList.get(i).Link)
                .into(drinkViewHolder.img_product);

        drinkViewHolder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //Yêu thích
        if (Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(i).ID)) == 1)
            drinkViewHolder.btn_favorites.setImageResource(R.drawable.ic_favorite_white_24dp);
        else
            drinkViewHolder.btn_favorites.setImageResource(R.drawable.ic_favorite_border_white_24dp);

        drinkViewHolder.btn_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(i).ID)) != 1) {

                    addOrRemoveFavorite(drinkList.get(i), true);
                    drinkViewHolder.btn_favorites.setImageResource(R.drawable.ic_favorite_white_24dp);

                }else {

                    addOrRemoveFavorite(drinkList.get(i), false);
                    drinkViewHolder.btn_favorites.setImageResource(R.drawable.ic_favorite_border_white_24dp);

                }
            }
        });

    }

    private void addOrRemoveFavorite(Drink drink, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.id = drink.ID;
        favorite.link = drink.Link;
        favorite.name = drink.Name;
        favorite.price = drink.Price;
        favorite.menuId = drink.MenuId;

        if (isAdd)
            Common.favoriteRepository.insertFav(favorite);
        else
            Common.favoriteRepository.delete(favorite);
    }

    private void showAddToCartDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout,null);

        //View
        ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txt_count = (ElegantNumberButton)itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = (TextView)itemView.findViewById(R.id.txt_cart_product_name);

        EditText edt_comment = (EditText) itemView.findViewById(R.id.edt_comment);

        //Radio
        RadioButton rdi_sizeM = (RadioButton)itemView.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = (RadioButton)itemView.findViewById(R.id.rdi_sizeL);

        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sizeOfCup = 0;
            }
        });
        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sizeOfCup = 1;
            }
        });

        RadioButton rdi_sugar_100 = (RadioButton)itemView.findViewById(R.id.rdi_sugar_100);
        RadioButton rdi_sugar_70 = (RadioButton)itemView.findViewById(R.id.rdi_sugar_70);
        RadioButton rdi_sugar_50 = (RadioButton)itemView.findViewById(R.id.rdi_sugar_50);
        RadioButton rdi_sugar_30 = (RadioButton)itemView.findViewById(R.id.rdi_sugar_30);
        RadioButton rdi_sugar_free = (RadioButton)itemView.findViewById(R.id.rdi_sugar_free);

        rdi_sugar_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 30;
            }
        });
        rdi_sugar_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 50;
            }
        });
        rdi_sugar_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 70;
            }
        });
        rdi_sugar_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 100;
            }
        });
        rdi_sugar_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 0;
            }
        });


        RadioButton rdi_ice_100 = (RadioButton)itemView.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70 = (RadioButton)itemView.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50 = (RadioButton)itemView.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30 = (RadioButton)itemView.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free = (RadioButton)itemView.findViewById(R.id.rdi_ice_free);

        rdi_ice_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 30;
            }
        });
        rdi_ice_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 50;
            }
        });
        rdi_ice_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 70;
            }
        });
        rdi_ice_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 100;
            }
        });
        rdi_ice_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 0;
            }
        });

        RecyclerView recycle_topping = (RecyclerView) itemView.findViewById(R.id.recycler_topping);
        recycle_topping.setLayoutManager(new LinearLayoutManager(context));
        recycle_topping.setHasFixedSize(true);

        MultiChoiceAdapter adapter = new MultiChoiceAdapter(context, Common.toppingList);
        recycle_topping.setAdapter(adapter);

        //Set data
        Picasso.with(context)
                .load(drinkList.get(i).Link)
                .into(img_product_dialog);
        txt_product_dialog.setText(drinkList.get(i).Name);

        builder.setView(itemView);
        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (Common.sizeOfCup == -1){
                    Toast.makeText(context, "Please choose size of cup", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.sugar == -1){
                    Toast.makeText(context, "Please choose sugar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.ice == -1){
                    Toast.makeText(context, "Please choose ice", Toast.LENGTH_SHORT).show();
                    return;
                }
                showConfirmDialog(i, txt_count.getNumber());
            }
        });
        builder.show();
    }

    private void showConfirmDialog(int i, final String number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout,null);
        //view
        ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_product);
        final TextView txt_product_dialog = (TextView)itemView.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price = (TextView)itemView.findViewById(R.id.txt_cart_product_price);
        TextView txt_sugar = (TextView)itemView.findViewById(R.id.txt_sugar);
        TextView txt_ice = (TextView)itemView.findViewById(R.id.txt_ice);
        final TextView txt_topping_extra = (TextView)itemView.findViewById(R.id.txt_topping_extra);


        //set Data
        Picasso.with(context).load(drinkList.get(poss).Link).into(img_product_dialog);
        txt_product_dialog.setText(new StringBuilder(drinkList.get(poss).Name).append(" x")
                .append(Common.sizeOfCup == 0 ? " Size M":" Size L")
                .append(number).toString());

        txt_ice.setText(new StringBuilder("Ice: ").append(Common.ice).append("%").toString());
        txt_sugar.setText(new StringBuilder("Sugar: ").append(Common.sugar).append("%").toString());

        double price = (Double.parseDouble(drinkList.get(poss).Price)*Double.parseDouble(number)) +
                (Common.toppingPrice*Double.parseDouble(number));

        if (Common.sizeOfCup == 1)
            price+=0.5*Double.parseDouble(number);


        StringBuilder topping_final_comment = new StringBuilder("");
        for (String line:Common.toppingAdded)
            topping_final_comment.append(line).append("\n");

        txt_topping_extra.setText(topping_final_comment);

        final double finalPrice = Math.round(price);

        txt_product_price.setText(new StringBuilder("$").append(finalPrice));

        builder.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //thêm vào SQL
                //Tạo item Cart
                try {

                    Cart cartItem = new Cart();
                    cartItem.name = drinkList.get(poss).Name;
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.ice = Common.ice;
                    cartItem.sugar = Common.sugar;
                    cartItem.price = finalPrice;
                    cartItem.size = Common.sizeOfCup;
                    cartItem.toppingExtras = txt_topping_extra.getText().toString();
                    cartItem.link = drinkList.get(poss).Link;

                    //Add to DB
                    Common.cartRepository.insertToCart(cartItem);

                    Log.d("HungTra_DEBUG", new Gson().toJson(cartItem));

                    Toast.makeText(context, "Save item to cart Success", Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setView(itemView);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
