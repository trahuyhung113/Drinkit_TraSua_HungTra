package com.example.hungtra.drinkit_trasua;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;


import com.example.hungtra.drinkit_trasua.Adapter.DrinkAdapter;
import com.example.hungtra.drinkit_trasua.Model.Drink;
import com.example.hungtra.drinkit_trasua.Retrofit.IDrinkShopAPI;
import com.example.hungtra.drinkit_trasua.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {

    IDrinkShopAPI mService;

    RecyclerView lst_drink;

    TextView txt_banner_name;

    //Rxjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        mService = Common.getAPI();

        lst_drink = (RecyclerView)findViewById(R.id.recycler_drinks);
        lst_drink.setLayoutManager(new GridLayoutManager(this,2));
        lst_drink.setHasFixedSize(true);

        txt_banner_name = (TextView)findViewById(R.id.txt_menu_name);
        txt_banner_name.setText(Common.currentCategory.Name);

        loadListDrink(Common.currentCategory.ID);
    }

    private void loadListDrink(String menuId) {
        compositeDisposable.add(mService.getDrink(menuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayDrinkist(drinks);
                    }
                }));

    }

    private void displayDrinkist(List<Drink> drinks) {
        DrinkAdapter adapter = new DrinkAdapter(this,drinks);
        lst_drink.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
