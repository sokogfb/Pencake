package com.timotiusoktorio.pencake.ui.orders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.pencake.data.model.Order;
import com.timotiusoktorio.pencake.databinding.ListItemOrderBinding;

import java.util.ArrayList;
import java.util.List;

class OrdersRvAdapter extends RecyclerView.Adapter<OrdersRvAdapter.ViewHolder> {

    private final List<Order> orders = new ArrayList<>();
    private final OnItemClickListener listener;

    OrdersRvAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    void updateData(List<Order> data) {
        orders.clear();
        orders.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemOrderBinding binding = ListItemOrderBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemOrderBinding binding;

        ViewHolder(ListItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order order = orders.get(getAdapterPosition());
                    listener.onItemClick(order);
                }
            });
        }

        void bind(Order order) {
            binding.setOrder(order);
        }
    }

    interface OnItemClickListener {

        void onItemClick(Order order);
    }
}