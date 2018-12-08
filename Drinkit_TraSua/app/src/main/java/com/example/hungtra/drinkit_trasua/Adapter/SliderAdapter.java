package com.example.hungtra.drinkit_trasua.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hungtra.drinkit_trasua.R;


public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater1;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //array
    public  int[] slider_image = {
            R.drawable.group1,
            R.drawable.group2,
            R.drawable.group3,
            R.drawable.group4,
    };

    public String[] slider_heading = {
            "SUPPORT ADDRESS",
            "DELETE ITEM CART",
            "SIMSIMI CHAT",
            "ORDER DETAIL",
    };

    public String[] slider_desc = {
            "Facebook address"+"\nhttps://www.facebook.com/huy.hung.52438"+"\ncontact me if your want ",
            "Swipe horizontal"+"\nYou will delete the drink in the cart"+"\ngood luck!" ,
            "Time went by slowly"+"\nWelcome to Simsimi Chat"+"\nit is interesting",
            "Clicked Your Order item"+"\nYou will see you order detail"+"\na great function",
    };
    @Override
    public int getCount() {
        return slider_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater1 = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater1.inflate(R.layout.slider_support_layout, container, false);

        ImageView slideImageView = (ImageView)view.findViewById(R.id.slider_image);
        TextView slideHeading = (TextView)view.findViewById(R.id.slider_heading);
        TextView slideDescription = (TextView)view.findViewById(R.id.slider_desc);

        slideImageView.setImageResource(slider_image[position]);
        slideHeading.setText(slider_heading[position]);
        slideDescription.setText(slider_desc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
