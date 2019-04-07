package com.example.mymoviecatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemClickSupport {
    private final RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null){
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(v);
                onItemClickListener.onItemCLicked(recyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener attachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (onItemClickListener != null){
                view.setOnClickListener(onClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
        }
    };

    private ItemClickSupport(RecyclerView view){
        recyclerView = view;
        recyclerView.setTag(R.id.item_click_support, this);
        recyclerView.addOnChildAttachStateChangeListener(attachStateChangeListener);
    }

    static ItemClickSupport addTo(RecyclerView view){
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null){
            support = new ItemClickSupport(view);
        }
        return support;
    }

    public static ItemClickSupport removeFrom(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    private void detach(RecyclerView view){
        view.removeOnChildAttachStateChangeListener(attachStateChangeListener);
        view.setTag(R.id.item_click_support, null);
    }

    public interface OnItemClickListener{
        void onItemCLicked(RecyclerView recyclerView, int position, View v);
    }
}
