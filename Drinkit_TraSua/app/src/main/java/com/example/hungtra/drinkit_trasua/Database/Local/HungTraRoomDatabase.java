package com.example.hungtra.drinkit_trasua.Database.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.hungtra.drinkit_trasua.Database.ModelDB.Cart;
import com.example.hungtra.drinkit_trasua.Database.ModelDB.Favorite;

@Database(entities = {Cart.class, Favorite.class}, version = 1)
public abstract class HungTraRoomDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();

    private static HungTraRoomDatabase instance;

    public static HungTraRoomDatabase getInstance(Context context){
        if (instance == null)
            instance = Room.databaseBuilder(context, HungTraRoomDatabase.class,
                    "HungTra_DrinkShopDB")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
