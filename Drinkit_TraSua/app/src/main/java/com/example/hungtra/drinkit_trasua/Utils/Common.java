package com.example.hungtra.drinkit_trasua.Utils;

import com.example.hungtra.drinkit_trasua.Database.DataSource.CartRepository;
import com.example.hungtra.drinkit_trasua.Database.DataSource.FavoriteRepository;
import com.example.hungtra.drinkit_trasua.Database.Local.HungTraRoomDatabase;
import com.example.hungtra.drinkit_trasua.Model.Category;
import com.example.hungtra.drinkit_trasua.Model.Drink;
import com.example.hungtra.drinkit_trasua.Model.Order;
import com.example.hungtra.drinkit_trasua.Model.User;
import com.example.hungtra.drinkit_trasua.Retrofit.IDrinkShopAPI;
import com.example.hungtra.drinkit_trasua.Retrofit.RetrofitClient;
import com.example.hungtra.drinkit_trasua.Retrofit.RetrofitScalarsClient;

import java.util.ArrayList;
import java.util.List;

//192.168.1.6
//10.18.101.15
public class Common {
    public static final String BASE_URL = "http://192.168.1.7/drinkshop/";
    public static final String API_TOKEN_URL = "http://192.168.1.7/drinkshop/braintree/main.php";

    public static final String TOPPING_MENU_ID = "7";

    public static User currentUser = null;
    public static Category currentCategory = null;
    public static Order currentOrder = null;

    public static List<Drink> toppingList = new ArrayList<>();

    public static double toppingPrice = 0.0;
    public static List<String> toppingAdded = new ArrayList<>();

    public static int sizeOfCup = -1;
    public static int sugar = -1;
    public static int ice = -1;

    //Database
    public static HungTraRoomDatabase hungTraRoomDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;

    public static IDrinkShopAPI getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);//
    }

    public static IDrinkShopAPI getScalarsAPI(){
        return RetrofitScalarsClient.getScalarsClient(BASE_URL).create(IDrinkShopAPI.class);
    }

    public static String convertCodeToStatus(int orderStatus) {
        switch (orderStatus)
        {
            case 0:
                return "Placed";
            case 1:
                return "Processing";
            case 2:
                return "Shipping";
            case 3:
                return "Shiped";
            case -1:
                return "Cancel";
                default:
                    return "Order Error";
        }

    }
}
