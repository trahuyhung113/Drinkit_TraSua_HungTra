package com.example.hungtra.drinkit_trasua;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hungtra.drinkit_trasua.Adapter.SliderAdapter;


public class SupportActivity extends AppCompatActivity {

    private ViewPager sliderVP;
    private RelativeLayout mDotLayout;

    private TextView[] mDots;
    public SliderAdapter sliderAdapter;

    private Button nextBtn;
    private Button prevBtn;
    private int mCurentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        sliderVP = (ViewPager)findViewById(R.id.sliderVP);

        nextBtn = (Button)findViewById(R.id.nextBtn);
        prevBtn = (Button)findViewById(R.id.prevBtn);
        mDotLayout = (RelativeLayout)findViewById(R.id.mDotLayout);

        sliderAdapter = new SliderAdapter(this);
        sliderVP.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        sliderVP.addOnPageChangeListener(viewListener);

        //onclickk
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderVP.setCurrentItem(mCurentPage + 1);
                if (mCurentPage == 3){
                    Intent intent = new Intent(SupportActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderVP.setCurrentItem(mCurentPage -1);
            }
        });
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[4];
        mDotLayout.removeAllViews();
        for (int i =0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(30);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWthite));
            mDotLayout.addView(mDots[i]);



        }

        if (mDots.length >0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            mCurentPage = i;

            if (i == 0){
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(false);
                prevBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("NEXT");
                prevBtn.setText("");

            }else if (i == 3){
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("FINISH");
                prevBtn.setText("BACK");

            }else {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("NEXT");
                prevBtn.setText("BACK");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
