package com.example.hungtra.drinkit_trasua;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;

import com.example.hungtra.drinkit_trasua.Adapter.CartAdapter;
import com.example.hungtra.drinkit_trasua.Database.ModelDB.Cart;
import com.example.hungtra.drinkit_trasua.Retrofit.IDrinkShopAPI;
import com.example.hungtra.drinkit_trasua.Utils.Common;
import com.example.hungtra.drinkit_trasua.Utils.RecyclerItemTouchHelper;
import com.example.hungtra.drinkit_trasua.Utils.RecyclerItemTouchHelperListenner;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListenner {
    private static final int PAYMENT_REQUEST_CODE = 7777;
    RecyclerView recycler_cart;
    Button btn_place_oder;

    CompositeDisposable compositeDisposable;

    List<Cart> cartList = new ArrayList<>();
    CartAdapter cartAdapter;

    RelativeLayout rootLayout;

    IDrinkShopAPI mService;
    IDrinkShopAPI mServiceScalars;

    //Global String
    String token,amount,orderAddress,orderComment;
    HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();
        mServiceScalars = Common.getScalarsAPI();

        recycler_cart = (RecyclerView)findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        btn_place_oder = (Button)findViewById(R.id.btn_place_oder);
        btn_place_oder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new
                RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        loadCartItems();

        loadToken();
    }

    private void loadToken() {
        final android.app.AlertDialog waitingDialog = new SpotsDialog(CartActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Please Wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Common.API_TOKEN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                waitingDialog.dismiss();
                btn_place_oder.setEnabled(false);

                Toast.makeText(CartActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                waitingDialog.dismiss();

                token = responseString;
                btn_place_oder.setEnabled(true);
            }
        });
    }

    private void placeOrder() {
        //Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");

        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

        final EditText edt_comment = (EditText)submit_order_layout.findViewById(R.id.edt_comment);
        final EditText edt_other_address = (EditText)submit_order_layout.findViewById(R.id.edt_other_address);

        final RadioButton rdi_user_address = (RadioButton)submit_order_layout.findViewById(R.id.rdi_user_address);
        final RadioButton rdi_other_address = (RadioButton)submit_order_layout.findViewById(R.id.rdi_other_address);

        final RadioButton rdi_credit_card = (RadioButton)submit_order_layout.findViewById(R.id.rdi_credit_card);
        final RadioButton rdi_cod = (RadioButton)submit_order_layout.findViewById(R.id.rdi_cod);

        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edt_other_address.setEnabled(false);
            }
        });

        rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edt_other_address.setEnabled(true);
            }
        });
        builder.setView(submit_order_layout);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (rdi_credit_card.isChecked()) {

                    orderComment = edt_comment.getText().toString();
                    if (rdi_user_address.isChecked())
                        orderAddress = Common.currentUser.getAddress();
                    else if (rdi_other_address.isChecked())
                        orderAddress = edt_other_address.getText().toString();
                    else
                        orderAddress = "";

                    //Payment
                    DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                    startActivityForResult(dropInRequest.getIntent(CartActivity.this),
                            PAYMENT_REQUEST_CODE);


                }
                else if (rdi_cod.isChecked())
                {
                    orderComment = edt_comment.getText().toString();
                    if (rdi_user_address.isChecked())
                        orderAddress = Common.currentUser.getAddress();
                    else if (rdi_other_address.isChecked())
                        orderAddress = edt_other_address.getText().toString();
                    else
                        orderAddress = "";

                    //Submit order
                    compositeDisposable.add(
                            Common.cartRepository.getCartItems()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Consumer<List<Cart>>() {
                                        @Override
                                        public void accept(List<Cart> carts) throws Exception {

                                            if (!TextUtils.isEmpty(orderAddress))
                                                sendOrderToServer(Common.cartRepository.sumPrice(),
                                                        carts,
                                                        orderComment,orderAddress,
                                                        "COD");
                                            else
                                                Toast.makeText(CartActivity.this,
                                                        "Order address can't null",
                                                        Toast.LENGTH_SHORT).show();

                                        }
                                    })
                    );
                }
            }
        });
        builder.show();

    }

    //Ctrl+O


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYMENT_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (Common.cartRepository.sumPrice() > 0)
                {
                    amount = String.valueOf(Common.cartRepository.sumPrice());
                    params = new HashMap<>();

                    params.put("amount",amount);
                    params.put("nonce",strNonce);

                    sendPayment();
                }
                else {
                    Toast.makeText(this, "Payment amount is 0", Toast.LENGTH_SHORT).show();
                }
            }
            else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
            else
            {
                Exception error = (Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("HungTra_Error_Payment",error.getMessage());
            }
        }
    }

    private void sendPayment() {

        mServiceScalars.payment(params.get("nonce"),params.get("amount"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().toString().contains("Successful"))
                        {
                            Toast.makeText(CartActivity.this, "Transaction successful",
                                    Toast.LENGTH_SHORT).show();

                            //gửi oder
                            compositeDisposable.add(
                                    Common.cartRepository.getCartItems()
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Consumer<List<Cart>>() {
                                                @Override
                                                public void accept(List<Cart> carts) throws Exception {

                                                    if (!TextUtils.isEmpty(orderAddress))
                                                        sendOrderToServer(Common.cartRepository.sumPrice(),
                                                                carts,
                                                                orderComment,orderAddress,
                                                                "Braintree");
                                                    else
                                                        Toast.makeText(CartActivity.this,
                                                                "Order address can't null",
                                                                Toast.LENGTH_SHORT).show();

                                                }
                                            })
                            );
                        }
                        else
                        {
                            Toast.makeText(CartActivity.this, "Transaction failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        Log.d("HungTra_INFO",response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("HungTra_INFO",t.getMessage());
                    }
                });
    }

    private void sendOrderToServer(float sumPrice, List<Cart> carts, String orderComment,
                                   String orderAddress, String paymentMethod) {
        if (carts.size() > 0){
            String orderDetail = new Gson().toJson(carts);

            mService.submitOrder(sumPrice,orderDetail,orderComment,orderAddress,Common.currentUser
                    .getPhone(),paymentMethod)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(CartActivity.this, "Order Submit",
                                    Toast.LENGTH_SHORT).show();

                            //xóa giỏ hàng
                            Common.cartRepository.emptyCart();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR",t.getMessage());

                        }
                    });
        }


    }

    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartItem(carts);
                    }
                })
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    private void displayCartItem(List<Cart> carts) {

        cartList = carts;
        cartAdapter = new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder){

            String name = cartList.get(viewHolder.getAdapterPosition()).name;
            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            //xóa item trong adapter
            cartAdapter.removeItem(deletedIndex);

            //Xóa item từ Database
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name)
                            .append(" - Remove from Cart List").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }
}
