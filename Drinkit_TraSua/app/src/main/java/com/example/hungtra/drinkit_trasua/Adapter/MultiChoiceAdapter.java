package com.example.hungtra.drinkit_trasua.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import com.example.hungtra.drinkit_trasua.Model.Drink;
import com.example.hungtra.drinkit_trasua.R;
import com.example.hungtra.drinkit_trasua.Utils.Common;

import java.util.List;

public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.MultiChoiceViewHolder>{

    Context context;
    List<Drink> optionList;

    public MultiChoiceAdapter(Context context, List<Drink> optionList) {
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public MultiChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.multi_check_layout, null);
        return new MultiChoiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceViewHolder multiChoiceViewHolder, final int i) {

        multiChoiceViewHolder.checkBox.setText(optionList.get(i).Name);
        multiChoiceViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Common.toppingAdded.add(compoundButton.getText().toString());
                    Common.toppingPrice+=Double.parseDouble(optionList.get(i).Price);
                }
                else {
                    Common.toppingAdded.remove(compoundButton.getText().toString());
                    Common.toppingPrice-=Double.parseDouble(optionList.get(i).Price);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    class MultiChoiceViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public MultiChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox)itemView.findViewById(R.id.ckb_topping);
        }
    }
}
