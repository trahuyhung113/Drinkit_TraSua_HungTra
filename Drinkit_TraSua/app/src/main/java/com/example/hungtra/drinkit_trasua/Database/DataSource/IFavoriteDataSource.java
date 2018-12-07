package com.example.hungtra.drinkit_trasua.Database.DataSource;



import com.example.hungtra.drinkit_trasua.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavItems();

    int isFavorite(int itemId);

    void insertFav(Favorite... favorites);

    void delete(Favorite favorite);
}
