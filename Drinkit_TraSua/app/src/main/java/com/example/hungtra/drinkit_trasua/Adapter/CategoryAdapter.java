package com.example.hungtra.drinkit_trasua.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.hungtra.drinkit_trasua.DrinkActivity;
import com.example.hungtra.drinkit_trasua.Interface.IItemClickListener;
import com.example.hungtra.drinkit_trasua.Model.Category;
import com.example.hungtra.drinkit_trasua.R;
import com.example.hungtra.drinkit_trasua.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, final int i) {
        //Load Img
        Picasso.with(context)
                .load(categories.get(i).Link)
                .into(categoryViewHolder.img_product);

        categoryViewHolder.txt_menu_name.setText(categories.get(i).Name);

        //Event
        categoryViewHolder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentCategory = categories.get(i);

                //start new Activity
                context.startActivity(new Intent(context,DrinkActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
