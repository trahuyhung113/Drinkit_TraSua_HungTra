package com.example.hungtra.drinkit_trasua;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import com.example.hungtra.drinkit_trasua.Adapter.CategoryAdapter;
import com.example.hungtra.drinkit_trasua.CartActivity;
import com.example.hungtra.drinkit_trasua.Database.DataSource.CartRepository;
import com.example.hungtra.drinkit_trasua.Database.DataSource.FavoriteRepository;
import com.example.hungtra.drinkit_trasua.Database.Local.CartDataSource;
import com.example.hungtra.drinkit_trasua.Database.Local.FavoriteDataSource;
import com.example.hungtra.drinkit_trasua.Database.Local.HungTraRoomDatabase;
import com.example.hungtra.drinkit_trasua.FavoriteListActivity;
import com.example.hungtra.drinkit_trasua.MainActivity;
import com.example.hungtra.drinkit_trasua.MapsActivity;
import com.example.hungtra.drinkit_trasua.Model.Banner;
import com.example.hungtra.drinkit_trasua.Model.Category;
import com.example.hungtra.drinkit_trasua.Model.Drink;
import com.example.hungtra.drinkit_trasua.R;
import com.example.hungtra.drinkit_trasua.Retrofit.IDrinkShopAPI;
import com.example.hungtra.drinkit_trasua.SearchActivity;
import com.example.hungtra.drinkit_trasua.ShowOrderActivity;
import com.example.hungtra.drinkit_trasua.SimsimiActivity;
import com.example.hungtra.drinkit_trasua.Utils.Common;
import com.example.hungtra.drinkit_trasua.Utils.ProgressRequestBody;
import com.example.hungtra.drinkit_trasua.Utils.UploadCallBack;
import com.facebook.accountkit.AccountKit;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,UploadCallBack {

    private static final int PICK_FILE_REQUEST = 1222;
    TextView txt_name, txt_phone;
    SliderLayout sliderLayout;

    IDrinkShopAPI mService;

    RecyclerView lst_menu;

    NotificationBadge badge;

    ImageView cart_icon;

    CircleImageView img_avatar;

    Uri selectedFileUrl;


    //Rxjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mService = Common.getAPI();


        lst_menu = (RecyclerView)findViewById(R.id.lst_menu);
        lst_menu.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        lst_menu.setHasFixedSize(true);

        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txt_name = (TextView)headerView.findViewById(R.id.txt_name);
        txt_phone = (TextView)headerView.findViewById(R.id.txt_phone);
        img_avatar = (CircleImageView)headerView.findViewById(R.id.img_avatar);

        //Event
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        //SetInfo
        txt_name.setText(Common.currentUser.getName());
        txt_phone.setText(Common.currentUser.getPhone());

        //set Avatar
        if (!TextUtils.isEmpty(Common.currentUser.getAvatarUrl())){
            Picasso.with(this)
                    .load(new StringBuilder(Common.BASE_URL)
                            .append("user_avatar/")
                            .append(Common.currentUser.getAvatarUrl()).toString())
                    .into(img_avatar);
        }

        //getBanner
        getBannerImage();

        //getMenu
        getMenu();

        //Save newest Topping List
        getToppingList();

        //init Database
        initDB();
    }

    private void chooseImage() {
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select a file"),
                PICK_FILE_REQUEST);
    }

    //Ctrl + O


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_FILE_REQUEST){
                if (data != null){
                    selectedFileUrl = data.getData();
                    if (selectedFileUrl != null && !selectedFileUrl.getPath().isEmpty()){
                        img_avatar.setImageURI(selectedFileUrl);
                        uploadFile();
                    }
                    else
                        Toast.makeText(this, "Cannot upload file to sever", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile() {
        if (selectedFileUrl != null){
            File file = FileUtils.getFile(this, selectedFileUrl);

            String fileName = new StringBuilder(Common.currentUser.getPhone())
                    .append(FileUtils.getExtension(file.toString()))
                    .toString();

            ProgressRequestBody requestFile = new ProgressRequestBody(file,this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file",fileName,requestFile);
            final MultipartBody.Part userPhone = MultipartBody.Part.createFormData("phone",Common.currentUser.getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.uploadFile(userPhone,body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(HomeActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }).start();
        }
    }

    private void initDB() {
        Common.hungTraRoomDatabase = HungTraRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.hungTraRoomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.hungTraRoomDatabase.favoriteDAO()));
    }

    private void getToppingList() {
        compositeDisposable.add(mService.getDrink(Common.TOPPING_MENU_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        Common.toppingList = drinks;
                    }
                }));
    }

    private void getMenu() {
        compositeDisposable.add(mService.getMenu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        displayMenu(categories);

                    }
                }));
    }

    private void displayMenu(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this,categories);
        lst_menu.setAdapter(adapter);
    }

    private void getBannerImage() {
        compositeDisposable.add(mService.getBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImage(banners);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {
        HashMap<String,String> bannerMap = new HashMap<>();
        for (Banner item:banners)
            bannerMap.put(item.getName(),item.getLink());

        for (String name:bannerMap.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }
    }

    //khi nhấn back sẽ nhắc nhở
    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackButtonClicked){
                super.onBackPressed();
                return;
            }
            this.isBackButtonClicked = true;
            Toast.makeText(this, "Please click BACK again to Exit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = (NotificationBadge) view.findViewById(R.id.badge);
        cart_icon = (ImageView)view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        UpdateCartCount();
        return true;
    }

    private void UpdateCartCount() {
        if (badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems() == 0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_menu) {
            return true;
        }
        else if (id == R.id.search_menu) {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            // Mục yêu thích
            if (Common.currentUser != null)
            {
                startActivity(new Intent(HomeActivity.this,FavoriteListActivity.class));
            }
            else
            {
                Toast.makeText(this, "Please login to user this features", Toast.LENGTH_SHORT).show();
            }

        }
        else if (id == R.id.nav_sign_out) {
            // Tạo dialog thoát
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit Application");
            builder.setMessage("Do you want exit this Application?");

            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AccountKit.logOut();

                    // Xóa toàn bộ Cativity đang mở của ứng dụng
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        else if (id == R.id.nav_show_order) {
            // Mục yêu thích
            if (Common.currentUser != null)
            {
                startActivity(new Intent(HomeActivity.this,ShowOrderActivity.class));
            }
            else
            {
                Toast.makeText(this, "Please login to user this features", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.nav_nearby_store) {
            // Map
            startActivity(new Intent(HomeActivity.this,MapsActivity.class));

        }
        else if (id == R.id.nav_simsimi) {
            // Símimi
            startActivity(new Intent(HomeActivity.this,SimsimiActivity.class));

        }
        else if (id == R.id.nav_support) {
            // Support
            startActivity(new Intent(HomeActivity.this,SupportActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateCartCount();
        isBackButtonClicked = false;
    }

    @Override
    public void onProgressUpdate(int pertantage) {

    }


}
