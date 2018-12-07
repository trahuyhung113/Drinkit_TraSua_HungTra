package com.example.hungtra.drinkit_trasua.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.Query;

import com.example.hungtra.drinkit_trasua.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT COUNT(*) FROM Cart")
    int countCartItems();

    @Query("SELECT SUM(Price) FROM Cart")
    float sumPrice();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart... carts);

    @Update
    void updateCart(Cart... carts);

    @Delete
    void deleteCartItem(Cart cart);
}
