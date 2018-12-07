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
            "Maxim HT",
            "BILL GATES",
            "MARK ZUCKERBERG",
            "JACK MA",
    };

    public String[] slider_desc = {
            "Ứng dụng hay"+"\nCác câu châm ngôn về cuộc sông"+"\ncủa những người nỗi tiếng",
            "Từng học ở Harvard,"+"\nlà người giàu nhất thế giới,"+"\nông chủ của Microsoft." ,
            "Ông chủ của Facebook,"+"\nNhân vật của năm năm 2010,"+"\nđạt được thành tựu AI.",
            "Tên thật là Mã Vân,"+"\nSáng lập thương mại điện tử Alibaba"+"\nlà người giàu nhất Châu Á.",
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
