package com.example.hungtra.drinkit_trasua.Utils;

import android.support.v7.widget.RecyclerView;

public interface RecyclerItemTouchHelperListenner {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
